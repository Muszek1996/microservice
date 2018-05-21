package com.muszek.microservice.restConsumerIMDB;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerAndErrorChecker extends RestTemplate {
    int i = 0;

    public <T> T get(String URL, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> responseEntity = restTemplate.getForEntity(URL, responseType);
        try {
            if(responseEntity.getStatusCode()!=HttpStatus.OK)
            System.out.println(responseEntity.getStatusCode().toString());
            while (responseEntity.getStatusCode() != HttpStatus.OK) {
                responseEntity = restTemplate.getForEntity(URL, responseType);
                if (++i > 5) {
                    throw new Exception("5 razy nieudana próba nawiązania połączenia z serwerem kisim");
                }
            }
        } catch (Exception e) {
            e.notify();
        }
        return responseEntity.getBody();
    }
}