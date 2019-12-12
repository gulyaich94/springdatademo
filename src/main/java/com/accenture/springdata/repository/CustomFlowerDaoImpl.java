package com.accenture.springdata.repository;

import com.accenture.springdata.entity.Flower;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomFlowerDaoImpl implements CustomFlowerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Flower> getFlowerWithMaxPrice() {
        return em.createQuery("from Flower where price = (select max(price) from Flower )", Flower.class)
                .getResultList();
    }
}
