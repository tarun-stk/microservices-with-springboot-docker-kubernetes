package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.service.IAccountsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IAccountsService accountsService;

    @Test
    public void fetchAccountHttpRequest() throws Exception {

//      set up
        String mobileNumber = "1234657890";
        AccountsDto theAccountsDto = new AccountsDto(1235467890l, "Savings", AccountsConstants.ADDRESS);
        CustomerDto theCustomerDto = new CustomerDto("Tarun", "tarun@hotmail.com", mobileNumber, theAccountsDto);

        when(accountsService.fetchAccount(mobileNumber)).thenReturn(theCustomerDto);

        assertEquals(theCustomerDto, accountsService.fetchAccount(mobileNumber));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

    }

}
