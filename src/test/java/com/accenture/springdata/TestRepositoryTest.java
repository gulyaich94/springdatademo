package com.accenture.springdata;

import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.repository.TestRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.accenture.springdata.common.Color.randomColor;
import static com.accenture.springdata.common.FlowerName.randomName;
import static com.accenture.springdata.common.FlowerUtils.randomPrice;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRepositoryTest {

    private static final Random RANDOM = new Random();

    @Autowired
    private TestRepository testRepository;

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
        testRepository.saveAll(flowers);
        for (Flower flower : flowers) {
            assertNotNull(flower.getId());
        }

        Iterable<Flower> flowerIterable = testRepository.findAll();
        for (Flower f : flowerIterable) {
            System.out.println(f.getId() + " " + f.getName() + " " + f.getColor());
        }

        testRepository.deleteAll();
    }

}
