package com.mycompany.app;

public enum Direction {
    north(0, "north"),
    south(1, "south"),
    east(2, "east"),
    west(3, "west");

    public static final int none_index = 4;
    public final int value;
    public final String name;

    private Direction(int value, String name) {
        this.value = value;
        this.name = name;
    }

    // class method
    static public Direction get_direction_by_name(String name) {
        for (Direction dir : Direction.values()) {
            if (dir.name.equals(name))
                return dir;
        }
        return null;
    }

    // class method
    static public Direction get_direction_by_value(int value) {
        for (Direction dir : Direction.values()) {
            if (dir.value == value)
                return dir;
        }
        return null;
    }
}