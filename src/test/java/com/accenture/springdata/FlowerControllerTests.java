package com.accenture.springdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlowerControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() {
        String result =
                this.restTemplate.getForObject("http://localhost:" + port + "/flower/greeting",
                        String.class);

        assertTrue(result.contains("Hello World"));
    }

    @Test
    public void greetingShouldReturnMessage() {
        String result =
                this.restTemplate.getForObject("http://localhost:" + port + "/flower/greeting?name=N",
                        String.class);

        assertTrue(result.contains("Hello N"));
    }
}
