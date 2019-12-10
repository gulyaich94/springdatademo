package com.accenture.springdata;

import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.repository.FlowerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.accenture.springdata.common.Color.randomColor;
import static com.accenture.springdata.common.FlowerName.randomName;
import static com.accenture.springdata.common.FlowerUtils.randomPrice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlowerRepositoryTests {

    private static final Random RANDOM = new Random();

    @Autowired
    private FlowerRepository flowerRepository;

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
        for (Flower flower : flowers) {
            assertNotNull(flower.getId());
        }
        flowerRepository.deleteAll();
    }

    @Test
    public void findByIdTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        Flower newFlower = flowerRepository.findById(flower.getId()).get();
        isFlowersEquals(flower, newFlower);

        flowerRepository.deleteAll();
    }

    @Test
    public void getFlowerPagingTest() {
        int listSize = 10;
        List<Flower> flowers = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
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

        getPageAndCompare(listSize, flowers, 0, 3);
        getPageAndCompare(listSize, flowers, 1, 5);
        getPageAndCompare(listSize, flowers, 3, 7);

        flowerRepository.deleteAll();
    }

    private void getPageAndCompare(int listSize, List<Flower> flowers, int page, int size) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, size);
        Page<Flower> result = flowerRepository.findAll(firstPageWithTwoElements);

        int expectedSize = (int) Math.ceil((float) listSize / (float) size);
        assertEquals(expectedSize, result.getTotalPages());
        assertEquals(listSize, result.getTotalElements());

        List<Flower> expectedList = null;
        try {
            expectedList = flowers.subList(page*size, page*size + size);
        } catch (IndexOutOfBoundsException e) {
            expectedList = Collections.emptyList();
        }
        isFlowersEquals(expectedList, result.toList());
    }

    private void isFlowersEquals(Flower expected, Flower actual) {
        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getCount(), actual.getCount());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getId(), actual.getId());
    }

    private void isFlowersEquals(List<Flower> expectedList, List<Flower> actualList) {
        if (expectedList.size() != actualList.size()) {
            throw new RuntimeException(
                    "Arrays sizes are different: expected = " + expectedList.size() + ", actual = " + actualList.size());
        }
        for (int i = 0; i < expectedList.size(); i++) {
            Flower expected = expectedList.get(i);
            Flower actual = actualList.get(i);

            assertEquals(expected.getColor(), actual.getColor());
            assertEquals(expected.getCount(), actual.getCount());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getPrice(), actual.getPrice());
            assertEquals(expected.getId(), actual.getId());
        }
    }
}
