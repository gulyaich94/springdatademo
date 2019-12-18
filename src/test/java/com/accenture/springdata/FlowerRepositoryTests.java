package com.accenture.springdata;

import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.repository.FlowerRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    public void getTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        Flower newFlower = flowerRepository.getByName(flower.getName());
        isFlowersEquals(flower, newFlower);

        flowerRepository.deleteAll();
    }

    @Test
    public void readTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        Flower newFlower = flowerRepository.readByColor(flower.getColor());
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

        getPageAndCompare(flowers, 0, 3);
        getPageAndCompare(flowers, 1, 5);
        getPageAndCompare(flowers, 3, 7);

        flowerRepository.deleteAll();
    }

    @Test
    public void getFlowerSortingTest() {
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

        Sort sort = Sort.by("price").ascending();
        List<Flower> sortedFlowers = flowerRepository.findAll(sort);
        for (int i = 1; i < listSize; i++) {
            assertTrue(sortedFlowers.get(i - 1).getPrice().compareTo(sortedFlowers.get(i).getPrice()) <= 0);
        }

        flowerRepository.deleteAll();
    }

    // TODO: 10.12.2019 not for all properties
    @Test
    public void getFlowerPagingSortingTest() {
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

        getPageSortAndCompare(
                flowers
                , 0
                , 3
                , "price"
                , "asc"
                , (Flower f1, Flower f2) ->
                        f1.getPrice().compareTo(f2.getPrice()));

        getPageSortAndCompare(
                flowers
                , 2
                , 5
                , "price"
                , "desc"
                , (Flower f1, Flower f2) ->
                        f2.getPrice().compareTo(f1.getPrice()));

        getPageSortAndCompare(
                flowers
                , 3
                , 2
                , "price"
                , "desc"
                , (Flower f1, Flower f2) ->
                        f2.getPrice().compareTo(f1.getPrice()));

        flowerRepository.deleteAll();
    }

    @Test
    public void getFlowerByExampleTest() {
        List<Flower> flowers = new ArrayList<>();
        Flower flower1 = Flower.builder()
                .color("красный")
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("тюльпан")
                .price(randomPrice(100))
                .build();

        flowers.add(flower1);

        Flower flower2 = Flower.builder()
                .color("зеленый")
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("роза")
                .price(randomPrice(100))
                .build();

        flowers.add(flower2);

        flowerRepository.saveAll(flowers);

        Flower exampleFlower1 = Flower.builder()
                .name(flower1.getName().toUpperCase())
                .color(flower1.getColor().toUpperCase())
                .build();
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAny().withIgnoreCase();
        Example<Flower> example1 = Example.of(exampleFlower1, caseInsensitiveExampleMatcher);
        Flower result1 = flowerRepository.findOne(example1).orElse(null);

        isFlowersEquals(flower1, result1);

        Flower exampleFlower2 = Flower.builder()
                .name("рОз")
                .color("белый")
                .build();
        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
                .withIgnorePaths("color");
        Example<Flower> example2 = Example.of(exampleFlower2, ignoringExampleMatcher);
        Flower result2 = flowerRepository.findOne(example2).orElse(null);

        isFlowersEquals(flower2, result2);

        Example<Flower> example3 = Example.of(Flower.builder().name("Одуванчик").build(), ExampleMatcher.matchingAny());
        Flower result3 = flowerRepository.findOne(example3).orElse(null);

        assertNull(result3);

        flowerRepository.deleteAll();
    }

    @Test
    public void exampleMatchingAllTest() {
        Flower flower = Flower.builder()
                .color("красный")
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("тюльпан")
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("color")
                .withIgnorePaths("count")
                .withIgnorePaths("description")
                .withIgnorePaths("price")
                .withIgnorePaths("createdDate")
                .withIgnorePaths("lastModifiedDate")
                .withIgnorePaths("createdBy")
                .withIgnorePaths("modifiedBy");
        Flower exFl = Flower.builder()
                .name(flower.getName())
                .build();
        Example<Flower> ex = Example.of(exFl, matcher);
        Flower res = flowerRepository.findOne(ex).orElse(null);

        isFlowersEquals(flower, res);

        flowerRepository.deleteAll();
    }

    @Test
    public void getMaxPriceFlowerTest() {
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

        flowers.sort((Flower f1, Flower f2) ->
                f2.getPrice().compareTo(f1.getPrice()));

        Flower maxPriceFlower = flowerRepository.getFlowerWithMaxPrice().get(0);
        isFlowersEquals(flowers.get(0), maxPriceFlower);

        flowerRepository.deleteAll();
    }

    @Test
    public void getFlowerCountTest() {
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

        List<Flower> flowerList = flowerRepository.findCountFlowerRandom(5);
        assertEquals(5, flowerList.size());

        flowerRepository.deleteAll();
    }

    @Test
    public void auditFlowerTest() {
        int listSize = 10;
        List<Flower> flowers = new ArrayList<>();
        long dateBeforeCreation = System.currentTimeMillis();
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
        long dateAfterCreation = System.currentTimeMillis();

        for (Flower flower : flowers) {
            long createdDate = flower.getCreatedDate();
            assertTrue(createdDate >= dateBeforeCreation);
            assertTrue(createdDate <= dateAfterCreation);
            assertEquals("Gulyaich", flower.getCreatedBy());
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long dateBeforeModification = System.currentTimeMillis();
        for (Flower flower : flowers) {
            flower.setCount(flower.getCount() + 1);
        }
        flowerRepository.saveAll(flowers);
        long dateAfterModification = System.currentTimeMillis();

        List<Flower> foundedFlowers = flowerRepository.findAll(); // required get from db
        for (Flower flower : foundedFlowers) {
            long modifiedDate = flower.getLastModifiedDate();
            assertTrue(modifiedDate >= dateBeforeModification);
            assertTrue(modifiedDate <= dateAfterModification);
            assertEquals("Gulyaich", flower.getModifiedBy());
        }

        flowerRepository.deleteAll();
    }

    @Test
    public void saveUpdateTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);
        System.out.println(flower);
        flower.setCount(flower.getCount() + 1);
        flowerRepository.save(flower);
        System.out.println(flower);
        List<Flower> flowerList = flowerRepository.findAll();
        System.out.println(flowerList);
        assertEquals(1, flowerList.size());

        flowerRepository.deleteAll();
    }

    @Test
    public void deleteByNameTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        int count = flowerRepository.deleteByName(flower.getName());
        assertEquals(1, count);
        assertTrue(flowerRepository.findAll().isEmpty());
    }

    @Test
    public void removeByNameTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name(randomName())
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        int count = flowerRepository.removeByName(flower.getName());
        assertEquals(1, count);
        assertTrue(flowerRepository.findAll().isEmpty());
    }

    @Test
    public void countByNameTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("тюльпан")
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        Flower flower1 = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("роза")
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower1);

        assertEquals(1, flowerRepository.countByName(flower.getName()));

        flowerRepository.deleteAll();
    }

    @Test
    public void existsByNameTest() {
        Flower flower = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("тюльпан")
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower);

        Flower flower1 = Flower.builder()
                .color(randomColor())
                .count(RANDOM.nextInt(50))
                .description(RandomStringUtils.randomAlphabetic(40))
                .name("роза")
                .price(randomPrice(100))
                .build();

        flowerRepository.save(flower1);

        assertTrue(flowerRepository.existsByName(flower.getName()));
        assertFalse(flowerRepository.existsByName("одуванчик"));

        flowerRepository.deleteAll();
    }

    private void getPageSortAndCompare(List<Flower> flowers, int page, int size, String property, String direction,
                                       Comparator<Flower> comparator) {
        int listSize = flowers.size();

        Sort sort = null;
        if ("desc".equals(direction)) {
            sort = Sort.by(property).descending();
        } else {
            sort = Sort.by(property).ascending();
        }
        Pageable pageRequest = PageRequest.of(page, size, sort);
        Page<Flower> result = flowerRepository.findAll(pageRequest);

        int expectedSize = (int) Math.ceil((float) listSize / (float) size);
        assertEquals(expectedSize, result.getTotalPages());
        assertEquals(listSize, result.getTotalElements());

        List<Flower> expectedList = null;
        if (page * size + size > flowers.size()) {
            expectedList = Collections.emptyList();
        } else {
            List<Flower> sortedFlowers = new ArrayList<>(flowers);
            sortedFlowers.sort(comparator);
            expectedList = sortedFlowers.subList(page * size, page * size + size);
        }

        List<Flower> flowerList = result.toList();
        isFlowersEquals(expectedList, flowerList);
        for (int i = 1; i < flowerList.size(); i++) {
            if ("desc".equals(direction)) {
                assertTrue(comparator.compare(flowerList.get(i - 1), flowerList.get(i)) <= 0);
            } else {
                assertTrue(comparator.compare(flowerList.get(i), flowerList.get(i - 1)) >= 0);
            }
        }
    }

    private void getPageAndCompare(List<Flower> flowers, int page, int size) {
        int listSize = flowers.size();
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Flower> result = flowerRepository.findAll(pageRequest);

        int expectedSize = (int) Math.ceil((float) listSize / (float) size);
        assertEquals(expectedSize, result.getTotalPages());
        assertEquals(listSize, result.getTotalElements());

        List<Flower> expectedList = null;
        try {
            expectedList = flowers.subList(page * size, page * size + size);
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
                    "Arrays sizes are different: expected = " + expectedList.size() + ", actual = " +
                            actualList.size());
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
