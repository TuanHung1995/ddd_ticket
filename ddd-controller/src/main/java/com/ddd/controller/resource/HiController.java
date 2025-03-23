package com.ddd.controller.resource;

import com.ddd.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/hi")
public class HiController {

    @Autowired
    private EventAppService eventAppService;

//    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/hello")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String hello() {
        return eventAppService.sayHi("world");
    }

    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hi() {
        return eventAppService.sayHi("Hi");
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    @GetMapping("/hi/v1")
    @CircuitBreaker(name = "checkRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker() {
        int productId = secureRandom.nextInt(20) + 1;

        String url = "https://fakestoreapi.com/products/" + productId;

        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackHello(Throwable t) {
        return "Too many requests, please try again later.";
    }

    public String fallbackCircuitBreaker(Throwable t) {
        return t.getMessage();
    }

}
