package v1;
import battlecode.common.*;

public class Constants {
	public static final PassabilityType DEFAULT_PASSABILITY = PassabilityType.DAMSOKAY;
	public static final int MIN_ENEMIES_FOR_EXPL = 3;
	public static final Direction[] DIRECTIONS = { Direction.NORTH, Direction.NORTHEAST, Direction.EAST,
			Direction.SOUTHEAST, Direction.SOUTH, Direction.SOUTHWEST, Direction.WEST,
			Direction.NORTHWEST };
	public static final Direction[] DIAG_DIRECTIONS = { Direction.NORTHEAST, Direction.SOUTHEAST,
			Direction.SOUTHWEST, Direction.NORTHWEST };
	public static final Direction[] CARD_DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST };
}
