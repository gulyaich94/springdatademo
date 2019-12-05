package com.accenture.springdata.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Color {
    RED("красный"),
    WHITE("белый"),
    GREEN("зеленый");

    private String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static String randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE)).getName();
    }
}
