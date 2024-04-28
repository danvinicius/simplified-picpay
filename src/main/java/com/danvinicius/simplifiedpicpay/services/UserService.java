package com.danvinicius.simplifiedpicpay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;
import com.danvinicius.simplifiedpicpay.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) throws UserNotFoundException {
        return this.userRepository.findUserById(id).orElseThrow(UserNotFoundException::new);
    }

    public void save(User user) {
        this.userRepository.save(user);
    }
}
