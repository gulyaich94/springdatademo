package com.accenture.springdata.controller;

import com.accenture.springdata.entity.Flower;
import com.accenture.springdata.exeption.FlowerNotFoundException;
import com.accenture.springdata.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/flower")
public class FlowerController {

    @Autowired
    private FlowerRepository flowerRepository;

    @GetMapping("/greeting")
    public ResponseEntity<String> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new ResponseEntity<>("Hello " + name, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Flower getFlowerById(@PathVariable Long id) {
        return flowerRepository.findById(id).orElseThrow(FlowerNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteFlower(@PathVariable Long id) {
        flowerRepository.deleteById(id);
    }

    @GetMapping("/paging/{page}/{count}")
    public Page<Flower> getFlowerPaging(@PathVariable Integer page, @PathVariable Integer count) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, count);
        return flowerRepository.findAll(firstPageWithTwoElements);
    }

    @GetMapping("sorting/{field}/{direction}")
    public List<Flower> getFlowerSorting(@PathVariable String field, @PathVariable String direction) {
        Sort sort = null;
        if ("desc".equals(direction)) {
            sort = Sort.by(field).descending();
        } else {
            sort = Sort.by(field).ascending();
        }
        return flowerRepository.findAll(sort);
    }

}
