package com.example.stockexchangeapplication.util;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil {

    private RestTemplate restTemplate;

    public RestTemplateUtil(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
