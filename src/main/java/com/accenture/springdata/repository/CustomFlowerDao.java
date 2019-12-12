package com.accenture.springdata.repository;

import java.util.List;

public interface CustomFlowerDao<Flower> {

    List<Flower> getFlowerWithMaxPrice();
}
