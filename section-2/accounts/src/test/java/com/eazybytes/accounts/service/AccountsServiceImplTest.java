package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.impl.AccountsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(value = AccountsServiceImpl.class)
public class AccountsServiceImplTest {

    @MockBean
    private AccountsRepository accountsRepository;
    @MockBean
    private CustomerRepository customerRepository;
    @Autowired
    private IAccountsService accountsService;
    private CustomerDto customerDto;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("1234567890")
                .accountsDto(new AccountsDto())
                .build();
        customer = Customer.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("1234567890")
                .build();
    }

    /*below tests customer already exists scenario*/
    @Test
    public void testCreateAccount_01() {
        Mockito.when(customerRepository.findByMobileNumber(Mockito.anyString())).thenReturn(Optional.of(customer));
        assertThrows(CustomerAlreadyExistsException.class, () -> accountsService.createAccount(customerDto));
    }

    /*below tests customer does not exist scenario*/
    @Test
    public void testCreateAccount_02() {
        Mockito.when(customerRepository.findByMobileNumber(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(new Customer());
        Mockito.when(accountsRepository.save(Mockito.any())).thenReturn(new Accounts());
        accountsService.createAccount(customerDto);
    }
}
