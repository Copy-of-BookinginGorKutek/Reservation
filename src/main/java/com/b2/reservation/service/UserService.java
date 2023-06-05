package com.b2.reservation.service;

import com.b2.reservation.model.User;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Generated
public class UserService {
    @Autowired
    RestTemplate restTemplate;

    public List<User> getAllUser(String token){
        HttpHeaders requestHeaders = getHttpHeaders(token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(requestHeaders);
        String url = "http://34.142.212.224:100/api/v1/user/get-all";
        try {
            ResponseEntity<User[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, User[].class);
            return List.of(Objects.requireNonNull(responseEntity.getBody()));
        }catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    private HttpHeaders getHttpHeaders(String token){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(token);
        return requestHeaders;
    }
}
