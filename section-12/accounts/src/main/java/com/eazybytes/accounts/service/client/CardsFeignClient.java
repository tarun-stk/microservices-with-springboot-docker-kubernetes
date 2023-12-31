package com.eazybytes.accounts.service.client;

import com.eazybytes.accounts.dto.CardsDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("microservice_name_as_on_eurekaserver") -> inside parenthesis, the name should match as on eurekaserver
//so that FeignClient will search for that particular instance on eurekaserver, & will communicate
//Notes on FeignClient:
//    In traditional architecture to communicate betwen microservices, we use RestTemplate/WebClient
//    But using those appraoches we have to write a lot of code, like defining url, httpMethod, response type
//    Using Fiegnclient we don't have to write any code, we need to just write method names in declerative way
//    like in repository how we use SpringDataJpa
@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeignClient {

//    when using the abstract methods
//    make sure that the API method signature remains same as in CardsController
//    like number & type of params, return type, access modifier
//    you can have a different method name as in CardsController, mentioning same name here for now
//    also use the same HttpMEthod with same endpoints as in CArdsController
//    this method fetches corresponding card details based on mobileNumber of the customer
    @GetMapping(value = "/api/cards/", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestParam String mobileNumber);
}
