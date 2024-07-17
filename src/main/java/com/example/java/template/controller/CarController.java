package com.example.java.template.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.java.template.controller.util.ValidationUtil;
import com.example.java.template.repository.entity.Car;
import com.example.java.template.service.CarService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return new ResponseEntity<>(carService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCars(@RequestParam(required = false, defaultValue = "") String find,
                                             @RequestParam(required = false, defaultValue = "id:asc") String sort) {
        ValidationUtil.validateSortRegex(sort);

        String[] strings = sort.split(":");
        List<Car> cars = carService.findAll(find, strings[0], Sort.Direction.valueOf(strings[1].toUpperCase()));
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/cars")
    public ResponseEntity<List<Car>> getUserCars(@PathVariable Long id) {
        return new ResponseEntity<>(carService.findByPersonId(id), HttpStatus.OK);
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> save(@RequestBody Car car) {
        Car savedCar = carService.save(car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        carService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
