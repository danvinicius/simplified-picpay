package com.danvinicius.simplifiedpicpay.dto;

import java.math.BigDecimal;

import com.danvinicius.simplifiedpicpay.domain.user.UserType;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType type) {
    
}
