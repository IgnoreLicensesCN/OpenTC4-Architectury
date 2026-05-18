package com.linearity.opentc4.utils;

import net.minecraft.core.Direction;

public class DirectionHelper {
    //if you modify it i will kick your ass
    //at least it's right-handed
    private static final Direction[][] DIRECTION_NOT_OPPOSITE = new Direction[Direction.values().length][Direction.values().length - 2];
    static {
        DIRECTION_NOT_OPPOSITE[Direction.UP.ordinal()] = new Direction[]{Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
        DIRECTION_NOT_OPPOSITE[Direction.DOWN.ordinal()] = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        DIRECTION_NOT_OPPOSITE[Direction.NORTH.ordinal()] = new Direction[]{Direction.DOWN, Direction.WEST, Direction.UP, Direction.EAST};
        DIRECTION_NOT_OPPOSITE[Direction.SOUTH.ordinal()] = new Direction[]{Direction.DOWN, Direction.EAST, Direction.UP, Direction.WEST};
        DIRECTION_NOT_OPPOSITE[Direction.WEST.ordinal()] = new Direction[]{Direction.NORTH, Direction.DOWN, Direction.SOUTH, Direction.UP};
        DIRECTION_NOT_OPPOSITE[Direction.EAST.ordinal()] = new Direction[]{Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN};
    }
    public static Direction[] getDirectionAroundNotOpposite(Direction direction){
        return DIRECTION_NOT_OPPOSITE[direction.ordinal()];
    }
}
