package com.example.java.template.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.java.template.exception.NotFoundException;
import com.example.java.template.repository.CarRepository;
import com.example.java.template.repository.entity.Car;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No data found with requested id."));
    }

    public List<Car> findByPersonId(Long id) {
        return carRepository.findByPersonId(id);
    }

    public List<Car> findAll(String find, String sortValue, Sort.Direction sortOrder) {
        if (find == null) {
            return carRepository.findAll(Sort.by(sortOrder, sortValue));
        }
        return carRepository.findAll(convertToValidSearch(find), Sort.by(sortOrder, sortValue));
    }

    @Transactional
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public void delete(Long id) {
        try {
            carRepository.deleteById(id);
        } catch (IllegalArgumentException exception) {
            throw new NotFoundException("No car found with requested id.", exception);
        }
    }

    private static String convertToValidSearch(String find) {
        return "%" + find.toUpperCase() + "%";
    }
}
