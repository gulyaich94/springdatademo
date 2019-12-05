package com.accenture.springdata.common;

import java.math.BigDecimal;

public class FlowerUtils {

    public static BigDecimal randomPrice(int range) {
        BigDecimal max = new BigDecimal(range);
        BigDecimal randFromDouble = new BigDecimal(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        actualRandomDec = actualRandomDec
                .setScale(2, BigDecimal.ROUND_DOWN);
        return actualRandomDec;
    }
}
