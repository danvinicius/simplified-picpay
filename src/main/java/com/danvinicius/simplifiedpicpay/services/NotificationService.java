package com.danvinicius.simplifiedpicpay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.dto.NotificationDTO;
import com.danvinicius.simplifiedpicpay.exceptions.NotificationException;

@Service
public class NotificationService {
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${picpay.notificationService.url}")
    private String notificationServiceUrl;


    public void sendNotification(User user, String message) throws NotificationException {
        String email = user.getEmail();
        NotificationDTO notification = new NotificationDTO(email, message);

        ResponseEntity<String> response = this.restTemplate.postForEntity(this.notificationServiceUrl, notification, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error on sending notification");
            throw new NotificationException();
        }
    }
}
