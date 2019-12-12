package com.accenture.springdata.repository;

import com.accenture.springdata.entity.Flower;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends CrudRepository<Flower, Long> {

    List<Flower> findByColor(String color);
}
