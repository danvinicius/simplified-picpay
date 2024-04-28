package com.danvinicius.simplifiedpicpay.dto;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal amount, Long senderId, Long receiverId) {
    
}
