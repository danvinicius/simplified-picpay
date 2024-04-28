package com.danvinicius.simplifiedpicpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danvinicius.simplifiedpicpay.domain.transaction.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
