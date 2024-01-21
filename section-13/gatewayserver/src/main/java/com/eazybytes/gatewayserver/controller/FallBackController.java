package com.eazybytes.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//adding fallback support, if any microservice fails, then it comes here and based on partiular
//api call it will generate response.
@RestController
public class FallBackController {

    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error occurred. Please try after some time or contact support team!!!");
    }

}
