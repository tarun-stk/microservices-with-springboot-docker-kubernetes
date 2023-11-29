package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ICustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
