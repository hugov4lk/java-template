package com.example.java.template.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.java.template.exception.NotFoundException;
import com.example.java.template.repository.UserRepository;
import com.example.java.template.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No data found with requested id."));
    }

    public List<User> findAll(String find, String sortValue, Sort.Direction sortOrder) {
        if (find == null) {
            return userRepository.findAll(Sort.by(sortOrder, sortValue));
        }
        return userRepository.findAll(convertToValidSearch(find), Sort.by(sortOrder, sortValue));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException exception) {
            throw new NotFoundException("No user found with requested id.", exception);
        }
    }

    private static String convertToValidSearch(String find) {
        return "%" + find.toUpperCase() + "%";
    }
}