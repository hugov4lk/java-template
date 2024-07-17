package com.example.java.template.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import com.example.java.template.exception.ExceptionDto;
import com.example.java.template.exception.ServiceErrorCode;

abstract class AbstractControllerIntegrationTest {

    protected abstract String getPath();

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    void getData_noDataFoundWithGivenId_notFoundStatusCodeReturned() {
        ResponseEntity<ExceptionDto> response = restTemplate.exchange(String.format("%s/1", getPath()),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not found", response.getBody().message());
        assertEquals("No data found with requested id.", response.getBody().exception());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidSortRequestParametersArgumentsProvider.class)
    void getData_invalidSortRequestParameter_badRequestStatusCodeReturned(String sortParameter) {
        ResponseEntity<ExceptionDto> response = restTemplate.exchange(String.format("%s?sort=%s", getPath(), sortParameter),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad request", response.getBody().message());
        assertEquals(String.format("Sort parameter '%s' is not valid.", sortParameter), response.getBody().exception());
        Assertions.assertEquals(ServiceErrorCode.INVALID_SORT_REGEX, response.getBody().errorCode());
    }

    @Test
    void doInvalidPutRequest_internalServerErrorStatusCodeReturned() {
        ResponseEntity<ExceptionDto> response = restTemplate.exchange(getPath(),
                HttpMethod.PUT,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().message());
        assertEquals("Request method 'PUT' is not supported", response.getBody().exception());
    }


    protected static HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static class InvalidSortRequestParametersArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("name:ascr"),
                    Arguments.of("name::asc"),
                    Arguments.of("2na2me:asc")
            );
        }
    }
}
