package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.controller.CustomerController;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.ICustomerService;
import com.eazybytes.accounts.service.client.CardsFeignClient;
import com.eazybytes.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private LoansFeignClient loansFeignClient;
    private CardsFeignClient cardsFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer theCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Long customerId = theCustomer.getCustomerId();
        Accounts theAccount = accountsRepository.findByCustomerId(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customerId.toString())
        );

//        setting customer details
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(theCustomer, new CustomerDetailsDto());
//        setting accountsdto
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(theAccount, new AccountsDto()));

//        setting cardsdto with help of feignclient
        customerDetailsDto.setCardsDto(cardsFeignClient.fetchCardDetails(correlationId, mobileNumber).getBody());
//        setting loansdto with help of feignclient
        customerDetailsDto.setLoansDto(loansFeignClient.fetchLoanDetails(correlationId, mobileNumber).getBody());
        return customerDetailsDto;
    }
}
