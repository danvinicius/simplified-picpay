package com.danvinicius.simplifiedpicpay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.dto.NotificationDTO;
import com.danvinicius.simplifiedpicpay.exceptions.NotificationException;

public class NotificationService {
    
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws NotificationException {
        String email = user.getEmail();
        NotificationDTO notification = new NotificationDTO(email, message);

        ResponseEntity<String> response = this.restTemplate.postForEntity("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6", notification, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error on sending notification");
            throw new NotificationException("Notification service is down");
        }
    }
}
