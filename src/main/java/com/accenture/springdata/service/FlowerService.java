package com.accenture.springdata.service;

import com.accenture.springdata.common.FlowerFilter;
import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.entity.QFlower;
import com.accenture.springdata.repository.FlowerRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class FlowerService {
    
    @Autowired
    private FlowerRepository flowerRepository;

    public Flower findFlowersByExample(Flower exampleFlower) {
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<Flower> example = Example.of(exampleFlower, caseInsensitiveExampleMatcher);
        Optional<Flower> result = flowerRepository.findOne(example);

//        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
//            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
//            .withIgnorePaths("color", "count");
//        Example<Flower> example1 = Example.of(exampleFlower, ignoringExampleMatcher);
//        List<Flower> result1 = flowerRepository.findAll(example1);

        //Example<Flower> fl = Example.of(new Flower("Rose"), ExampleMatcher.matchingAny());
        return result.get();
    }

    
    public List<Flower> findAllByPredicate(String name, String color) {
        QFlower qFlower = QFlower.flower;

        Predicate flowerPredicate = qFlower.name.eq(name).or(qFlower.color.eq(color));

        List<Flower> result = new ArrayList<>();
        flowerRepository.findAll(flowerPredicate).forEach(result::add);
        return result;
    }

    
    public List<Flower> findAllByPredicateFilter(FlowerFilter filter) {
//        FlowerFilter filter = createFilter();

        QFlower qFlower = QFlower.flower;
        BooleanBuilder bb = new BooleanBuilder();
        if (isNotBlank(filter.getName())) {
            bb = bb.and(qFlower.name.equalsIgnoreCase(filter.getName()));
        }
        if (isNotBlank(filter.getStartsName())) {
            bb = bb.and(qFlower.name.startsWithIgnoreCase(filter.getStartsName()));
        }
        if (isNotBlank(filter.getContainsName())) {
            bb = bb.and(qFlower.name.containsIgnoreCase(filter.getContainsName()));
        }
        if (isNotBlank(filter.getColor())) {
            bb = bb.and(qFlower.color.eq(filter.getColor()));
        }
        if (filter.getMaxPrice() != null) {
            bb = bb.and(qFlower.price.loe(filter.getMaxPrice()));
        }
        if (filter.getMinPrice() != null) {
            bb = bb.and(qFlower.price.goe(filter.getMinPrice()));
        }

//        bb = bb.and(qFlower.description.likeIgnoreCase("%б%"));

        Sort sort = Sort.by("id");
        if (isNotBlank(filter.getOrderField()) && isNotBlank(filter.getOrderDirection())) {
            if ("ASC".equals(filter.getOrderDirection())) {
                sort = Sort.by(filter.getOrderField()).ascending();
            } else {
                sort = Sort.by(filter.getOrderField()).descending();
            }
        } else if (isNotBlank(filter.getOrderField())) {
            sort = Sort.by(filter.getOrderField());
        }
        else {
            if ("ASC".equals(filter.getOrderDirection())) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }
        }
        List<Flower> result = new ArrayList<>();
        flowerRepository.findAll(bb.getValue(), sort).forEach(result::add);
        return result;
    }

//    private FlowerFilter createFilter() {
//        FlowerFilter filter = new FlowerFilter();
////        filter.setName("Роза");
//        filter.setColor("красный");
////        filter.setMinPrice(new BigDecimal(2));
////        filter.setMaxPrice(new BigDecimal(20));
//        filter.setOrderField("name");
//        filter.setOrderDirection("ASC");
//
//        return filter;
//    }
    
    @Transactional
    public void changeFlowerCount(Long id, Integer count) {
        Flower flowerToChange = flowerRepository.findFlowerToOrder(id);
        flowerToChange.setCount(flowerToChange.getCount() - count);
        flowerRepository.save(flowerToChange);
    }
}
