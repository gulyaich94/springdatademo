package com.accenture.springdata.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flower")
public class FlowerController {

    @GetMapping("/greeting")
    public ResponseEntity<String> hello(@RequestParam(value="name", defaultValue="World") String name) {
        return new ResponseEntity<>("Hello " + name, HttpStatus.OK);
    }
}
