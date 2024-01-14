package v1;
import battlecode.common.*;
import static v1.Constants.*;
import java.util.*;

/**
 * Code for ALL ducks
 * @author Milo
 */
/*
 * Currently implemented:
 * - Pick up flag and carry back to base
 * - Heal if no enemies
 * - Attack if in range of enemies
 *      - If backup, close in. Otherwise, back off.

 *  Important todos
 *  - Implement building
 *  - Improve micro and micro
 *  - Macro through comms
 */
public class Duck extends RobotPlayer {
	static BugPathfinding pathfinding;

	public static void setup() throws GameActionException {

	}

	// ---------------------------------------------- MAIN LOOP  ------------------------------------------------
	public static void run() throws GameActionException {
		// No pathfinding, create one with default passability
		if (pathfinding == null) pathfinding = new BugPathfinding(rc, DEFAULT_PASSABILITY);

		// We haven't spawned yet, attempt to spawn
		if (!rc.isSpawned()){ if(!trySpawn()) return; }

		Roles: {
			// Determine roles
			RobotInfo[] enemyDucks = rc.senseNearbyRobots(-1, oppTeam);
			RobotInfo[] allyDucks = rc.senseNearbyRobots(-1, myTeam);
			FlagInfo[] flagsInfo = rc.senseNearbyFlags(-1, oppTeam);

			// Found a flag! First priority is to pick up and carry back
			for (FlagInfo flag : flagsInfo) {
				if (rc.canPickupFlag(flag.getLocation())) {
					rc.pickupFlag(flag.getLocation());
					pathfinding.setDestination(spawn);
				}
			}
			// Heal when beside allies and no enemies
			if (allyDucks.length > 0 && enemyDucks.length == 0) {
				RobotInfo leastHealthDuck = allyDucks[0];
				for (RobotInfo allyDuck : allyDucks) {
					if ((allyDuck.health < 350) && (allyDuck.health < leastHealthDuck.health)) {
						leastHealthDuck = allyDuck;
					}
				}
				if (rc.canHeal(leastHealthDuck.getLocation())) {
					rc.heal(leastHealthDuck.getLocation());
				}
			}

			// Attack when in range of enemies
			if(enemyDucks.length > 0) {
				RobotInfo enemy = enemyDucks[rng.nextInt(enemyDucks.length)];
				if(rc.canAttack(enemy.getLocation())) rc.attack(enemy.getLocation());

				if(!rc.hasFlag()) {
					// If not with allies, attack and back off
					// TODO: try to back off only from their attack range, not all of vision range
					if(allyDucks.length < 2) pathfinding.moveOnce(rc.getLocation().directionTo(enemy.getLocation()).opposite());

					// If with allies, attack and move towards
					// TODO: make more macro-y. (Units should still move away when they're done attacking)
					else pathfinding.moveOnce(rc.getLocation().directionTo(enemy.getLocation()));
				}
			}
		}

		pathfinding.step();
	}

	// ---------------------------------------------- HELPERS  ------------------------------------------------
	public static boolean trySpawn() throws GameActionException {
		MapLocation[] spawnLocations = rc.getAllySpawnLocations();

		// TODO: Deterministic spawning
		MapLocation randomLoc = spawnLocations[rng.nextInt(spawnLocations.length)];
		if (rc.canSpawn(randomLoc)) {
			rc.spawn(randomLoc);
			spawn = rc.getLocation();
			pathfinding.setDestination(new MapLocation(mapWidth - spawn.x, mapHeight - spawn.y));

			return true;
		} else return false;
  }
}
	//---------------------------------------BUILDERS------------------------------------------------------------
//Build traps if enemies aren't close
//TODO: Defend our flags with explosive traps
	public static void checkFriendlyFlag(FlagInfo flag) {
		if (flag.getTeam().equals(myTeam())) return True;
		build (TrapType.EXPLOSIVE, isAdjacentTo(FlagInfo));
	}
	public static void checkFriendlyFlagBearer(RobotInfo duck) {
		if (duck.hasFlag() && duck.getTeam().equals(myTeam())) return True;
	}
