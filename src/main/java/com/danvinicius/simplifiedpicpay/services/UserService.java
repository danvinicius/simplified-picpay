package com.danvinicius.simplifiedpicpay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.dto.UserDTO;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;
import com.danvinicius.simplifiedpicpay.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) throws UserNotFoundException {
        return this.userRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(UserDTO userData) {
        User user = new User(userData);
        return this.userRepository.save(user);
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
