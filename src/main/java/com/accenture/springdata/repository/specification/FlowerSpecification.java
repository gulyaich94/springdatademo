package com.accenture.springdata.repository.specification;

import com.accenture.springdata.entity.Flower;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FlowerSpecification {

//    public static Specification<Flower> flowerHasColor(String color) {
//        return new Specification<Flower>() {
//            @Override
//            public Predicate toPredicate(Root<Flower> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get(Flower_.color), color);
//            }
//        };
//    }
}
