package com.example.java.template.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.java.template.repository.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByPersonId(Long id);

    @Query("SELECT c FROM Car c WHERE UPPER(c.make) LIKE ?1 OR UPPER(c.model) LIKE ?1 OR UPPER(c.numberplate) LIKE ?1")
    List<Car> findAll(String search, Sort sort);
}