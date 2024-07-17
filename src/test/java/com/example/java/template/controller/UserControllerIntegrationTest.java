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

import com.example.java.template.repository.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String USER_POST_REQUEST_BODY = readFileToString("controller/user/post-request-body.json");
    private static final String USER_PUT_REQUEST_BODY = readFileToString("controller/user/put-request-body.json");

    @Override
    protected String getPath() {
        return "/users";
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUser_success() {
        ResponseEntity<User> response = restTemplate.exchange("/users/1",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Teet Järveküla", response.getBody().getName());
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUsers_findBySearchAndSortByNameDesc_success() {
        ResponseEntity<List<User>> response = restTemplate.exchange("/users?find=Teet&sort=name:desc",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Teet Kruus", response.getBody().get(0).getName());
        assertEquals("Teet Järveküla", response.getBody().get(1).getName());
    }

    @Test
    @Sql("/db/controller/user/create-data.sql")
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUsers_findBySearchAndSortByNameAsc_success() {
        ResponseEntity<List<User>> response = restTemplate.exchange("/users?find=Teet&sort=name:asc",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Teet Järveküla", response.getBody().get(0).getName());
        assertEquals("Teet Kruus", response.getBody().get(1).getName());
    }


    @Test
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveUser_success() {
        ResponseEntity<User> response = restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(USER_POST_REQUEST_BODY, getHeaders()),
                User.class);

        assertNotNull(response.getBody());
        assertEquals("Boss", response.getBody().getName());
    }

    @Test
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser_success() {
        ResponseEntity<User> saveResponse = restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(USER_POST_REQUEST_BODY, getHeaders()),
                User.class);

        assertNotNull(saveResponse.getBody());
        assertEquals("Boss", saveResponse.getBody().getName());

        ResponseEntity<User> response = restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(USER_PUT_REQUEST_BODY, getHeaders()),
                User.class);

        assertNotNull(response.getBody());
        assertEquals("BossBoss", response.getBody().getName());
    }

    @Test
    @Sql(scripts = "/db/controller/user/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUser_success() {
        ResponseEntity<User> saveResponse = restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(USER_POST_REQUEST_BODY, getHeaders()),
                User.class);

        assertNotNull(saveResponse.getBody());
        assertTrue(saveResponse.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> response = restTemplate.exchange("/users/" + saveResponse.getBody().getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
