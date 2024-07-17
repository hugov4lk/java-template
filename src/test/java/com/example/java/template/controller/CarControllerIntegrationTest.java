package com.example.java.template.controller;

import static com.example.java.template.AbstractTest.readFileToString;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.example.java.template.repository.entity.Car;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String CAR_POST_REQUEST_BODY = readFileToString("controller/car/post-request-body.json");
    private static final String CAR_PUT_REQUEST_BODY = readFileToString("controller/car/put-request-body.json");

    @Override
    protected String getPath() {
        return "/cars";
    }

    @Test
    @Sql("/db/controller/car/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCar_success() {
        ResponseEntity<Car> response = restTemplate.exchange("/cars/1",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Car.class);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Lada", response.getBody().getMake());
    }

    @Test
    @Sql("/db/controller/car/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCars_findBySearchAndSortByModelDesc_success() {
        ResponseEntity<List<Car>> response = restTemplate.exchange("/cars?find=Bmw&sort=model:desc",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("BMW", response.getBody().get(0).getMake());
        assertEquals("760", response.getBody().get(0).getModel());
        assertEquals("BMW", response.getBody().get(1).getMake());
        assertEquals("740", response.getBody().get(1).getModel());
    }

    @Test
    @Sql("/db/controller/car/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCars_findBySearchAndSortByModelAsc_success() {
        ResponseEntity<List<Car>> response = restTemplate.exchange("/cars?find=Bmw&sort=model:asc",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("BMW", response.getBody().get(0).getMake());
        assertEquals("740", response.getBody().get(0).getModel());
        assertEquals("BMW", response.getBody().get(1).getMake());
        assertEquals("760", response.getBody().get(1).getModel());
    }

    @Test
    @Sql("/db/controller/car/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserCars_success() {
        ResponseEntity<List<Car>> response = restTemplate.exchange("/users/1/cars",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Lada", response.getBody().get(0).getMake());
        assertEquals("Kia", response.getBody().get(1).getMake());
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveCar_success() {
        ResponseEntity<Car> response = restTemplate.exchange("/cars",
                HttpMethod.POST,
                new HttpEntity<>(CAR_POST_REQUEST_BODY, getHeaders()),
                Car.class);

        assertNotNull(response.getBody());
        assertEquals("BMW", response.getBody().getMake());
        assertEquals("320d", response.getBody().getModel());
        assertEquals("515DBD", response.getBody().getNumberplate());
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCar_success() {
        ResponseEntity<Car> saveResponse = restTemplate.exchange("/cars",
                HttpMethod.POST,
                new HttpEntity<>(CAR_POST_REQUEST_BODY, getHeaders()),
                Car.class);

        assertNotNull(saveResponse.getBody());
        assertEquals("BMW", saveResponse.getBody().getMake());
        assertEquals("320d", saveResponse.getBody().getModel());
        assertEquals("515DBD", saveResponse.getBody().getNumberplate());

        ResponseEntity<Car> response = restTemplate.exchange("/cars",
                HttpMethod.POST,
                new HttpEntity<>(CAR_PUT_REQUEST_BODY, getHeaders()),
                Car.class);

        assertNotNull(response.getBody());
        assertEquals("BMW", response.getBody().getMake());
        assertEquals("320d", response.getBody().getModel());
        assertEquals("999VLK", response.getBody().getNumberplate());
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/car/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCar_success() {
        ResponseEntity<Car> saveResponse = restTemplate.exchange("/cars",
                HttpMethod.POST,
                new HttpEntity<>(CAR_POST_REQUEST_BODY, getHeaders()),
                Car.class);

        assertNotNull(saveResponse.getBody());
        assertTrue(saveResponse.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> response = restTemplate.exchange("/cars/" + saveResponse.getBody().getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
