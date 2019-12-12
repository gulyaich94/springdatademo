package com.accenture.springdata.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowerFilter {

    private String orderField;
    private String orderDirection;
    private String name;
    private String startsName;
    private String containsName;
    private String color;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
