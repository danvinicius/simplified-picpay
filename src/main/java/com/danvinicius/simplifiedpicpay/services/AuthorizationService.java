package com.danvinicius.simplifiedpicpay.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.danvinicius.simplifiedpicpay.domain.user.User;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${picpay.authorizerService.url}")
    private String authorizerServiceUrl;

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
    // mocked authorizer service
    ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(this.authorizerServiceUrl, Map.class);
    String message = (String) authorizationResponse.getBody().get("message");
    return authorizationResponse.getStatusCode() == HttpStatus.OK && message.equalsIgnoreCase("Autorizado");
    }
}
