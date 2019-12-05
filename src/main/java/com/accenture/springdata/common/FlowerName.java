package com.accenture.springdata.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum FlowerName {
    ROSE("роза"),
    TULIP("тюльпан"),
    CHRYSANTHEMUM("хризантема");

    private String name;

    FlowerName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final List<FlowerName> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static String randomName()  {
        return VALUES.get(RANDOM.nextInt(SIZE)).getName();
    }
}
