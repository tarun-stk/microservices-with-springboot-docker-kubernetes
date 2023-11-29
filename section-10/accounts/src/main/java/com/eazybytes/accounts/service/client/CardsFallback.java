package com.eazybytes.accounts.service.client;

import com.eazybytes.accounts.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
//this is a fallback class for CardsFeignClient, if CardsFeignClient fails to send response
//then this class will be invoked, and will send null response for cards related info
//so that overall response will not break, and partial grace response can be seen by the client
public class CardsFallback implements CardsFeignClient{
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
