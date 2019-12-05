package com.accenture.springdata;

import com.accenture.springdata.controller.FlowerController;
import com.accenture.springdata.repository.FlowerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringdataApplicationTests {

	@Autowired
	private FlowerController flowerController;

	@Autowired
	private FlowerRepository flowerRepository;

	@Test
	void contextLoads() {
		assertThat(flowerController).isNotNull();
		assertThat(flowerRepository).isNotNull();
	}
}
