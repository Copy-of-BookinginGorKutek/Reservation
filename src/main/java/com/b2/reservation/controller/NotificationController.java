package com.b2.reservation.controller;

import com.b2.reservation.request.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/notifikasi")
public class NotificationController {
    @Autowired
    RestTemplate restTemplate;

    @Operation(summary = "Send a new notification (microservice call)")
    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity<Object> createNotification(@RequestBody NotificationRequest notificationRequest,
                                                     @CookieValue(name = "token", defaultValue = "") String token){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationRequest> http = new HttpEntity<>(notificationRequest, requestHeaders);
        try{
            return restTemplate.postForEntity("http://34.142.212.224:40/api/v1/notification/send", http, Object.class);
        }catch(HttpServerErrorException | HttpClientErrorException e){
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
