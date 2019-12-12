package com.accenture.springdata;

import com.accenture.springdata.common.FlowerFilter;
import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.repository.FlowerRepository;
import com.accenture.springdata.service.FlowerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.accenture.springdata.common.Color.randomColor;
import static com.accenture.springdata.common.FlowerName.randomName;
import static com.accenture.springdata.common.FlowerUtils.randomPrice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlowerServiceTests {

    private static final Random RANDOM = new Random();

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private FlowerService flowerService;

    @Test
    public void createFlowerTest() {
        List<Flower> flowers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Flower flower = Flower.builder()
                    .color(randomColor())
                    .count(RANDOM.nextInt(50))
                    .description(RandomStringUtils.randomAlphabetic(40))
                    .name(randomName())
                    .price(randomPrice(100))
                    .build();

            flowers.add(flower);
        }
        flowerRepository.saveAll(flowers);

        Long id = flowers.get(0).getId();
        Integer countOld = flowers.get(0).getCount();
        Integer count = RANDOM.nextInt(countOld);
        flowerService.changeFlowerCount(id, count);

        Flower flower = flowerRepository.findById(id).get();
        assertEquals(countOld - count, (long) flower.getCount());

        flowerRepository.deleteAll();
    }

    @Test
    public void findAllByPredicateTest() {
        List<Flower> flowers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Flower flower = Flower.builder()
                    .color(randomColor())
                    .count(RANDOM.nextInt(50))
                    .description(RandomStringUtils.randomAlphabetic(40))
                    .name(randomName())
                    .price(randomPrice(100))
                    .build();

            flowers.add(flower);
        }
        flowerRepository.saveAll(flowers);

        List<Flower> result = flowerService.findAllByPredicate(flowers.get(0).getName(), flowers.get(1).getColor());
        assertFalse(result.isEmpty());

        flowerRepository.deleteAll();
    }

    @Test
    public void findAllByPredicateFilterTest() {
        List<Flower> flowers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Flower flower = Flower.builder()
                    .color(randomColor())
                    .count(RANDOM.nextInt(50))
                    .description(RandomStringUtils.randomAlphabetic(40))
                    .name(randomName())
                    .price(randomPrice(100))
                    .build();

            flowers.add(flower);
        }
        Flower flower1 = Flower.builder()
                .color("белый")
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("Роза")
                .price(new BigDecimal(15))
                .build();

        Flower flower2 = Flower.builder()
                .color("белый")
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("Ромашка")
                .price(new BigDecimal(17))
                .build();
        flowers.add(flower1);
        flowers.add(flower2);

        flowerRepository.saveAll(flowers);

        FlowerFilter flowerFilter =
                FlowerFilter.builder()
                        .color("белый")
                        .minPrice(new BigDecimal(10))
                        .maxPrice(new BigDecimal(20))
                        .containsName("ро")
                        .build();

        List<Flower> result = flowerService.findAllByPredicateFilter(flowerFilter);
        assertFalse(result.isEmpty());

        flowerRepository.deleteAll();
    }

    private void isFlowersEquals(Flower expected, Flower actual) {
        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getCount(), actual.getCount());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getId(), actual.getId());
    }
}
