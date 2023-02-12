package com.example.homework.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.homework.controller.util.ValidationUtil;
import com.example.homework.repository.entity.User;
import com.example.homework.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String find,
                                               @RequestParam(required = false, defaultValue = "id:asc") String sort) {
        ValidationUtil.validateSortRegex(sort);

        String[] strings = sort.split(":");
        List<User> users = userService.findAll(find, strings[0], Sort.Direction.valueOf(strings[1].toUpperCase()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
