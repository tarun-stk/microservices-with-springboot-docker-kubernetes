package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.service.IAccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(value = AccountsController.class)
public class AccountsControllerTest {

    @MockBean
    private IAccountsService accountsService;

    @Autowired
    private MockMvc mockMvc;

    /*below tests happy path*/
    @Test
    public void testCreateAccount_01() throws Exception {
        Mockito.doNothing().when(accountsService).createAccount(Mockito.any());
        CustomerDto customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("1234567890")
                .accountsDto(new AccountsDto())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(customerDto);

//        calling the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.post("/api/accounts/").contentType("application/json").content(jsonRequest);

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        assertEquals(201, status);

    }

    /*below tests when mobile number validation fails, throws message: Mobile number must be of size 10 and can contain only digits */
    @Test
    public void testCreateAccount_02() throws Exception {
        Mockito.doNothing().when(accountsService).createAccount(Mockito.any());
        CustomerDto customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("123456789")
                .accountsDto(new AccountsDto())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(customerDto);

//        creating the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.post("/api/accounts/").contentType("application/json").content(jsonRequest);

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        String contentAsString = response.getContentAsString();
        System.out.println("contentAsString: " + contentAsString);
        assertEquals(400, status);

    }

    /*below to test happy path of fetchAccount*/
    @Test
    public void testFetchAccount_01() throws Exception {
        String mobileNumber = "1234567890";
        CustomerDto customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber(mobileNumber)
                .accountsDto(new AccountsDto())
                .build();
        Mockito.when(accountsService.fetchAccount(mobileNumber)).thenReturn(customerDto);
        //        creating the get request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.get("/api/accounts/?mobileNumber=" + mobileNumber).contentType("application/json");

        //perfogrm
        MvcResult result = mockMvc.perform(reqBuilder).andReturn();
//        get result
        MockHttpServletResponse response = result.getResponse();
//        get status
        int status = response.getStatus();
        String content = response.getContentAsString();
        assertEquals(200, status);
        assertTrue(content.contains(mobileNumber));
    }

    /*below to test invalid mobilenumber scenario*/
    @Test
    public void testFetchAccount_02() throws Exception {
        String mobileNumber = "123456790";
        //        creating the get request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.get("/api/accounts/?mobileNumber=" + mobileNumber).contentType("application/json");

        //perfogrm
        MvcResult result = mockMvc.perform(reqBuilder).andReturn();
//        get result
        MockHttpServletResponse response = result.getResponse();
//        get status
        int status = response.getStatus();
        assertEquals(500, status);
    }

    /*below checks for update success*/
    @Test
    public void testUpdateAccountDetails_01() throws Exception {
        Mockito.when(accountsService.updateAccountDetails(Mockito.any())).thenReturn(true);
        CustomerDto customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("1234567890")
                .accountsDto(new AccountsDto())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(customerDto);

//        calling the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.put("/api/accounts/").contentType("application/json").content(jsonRequest);

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        assertEquals(200, status);
    }

    /*below checks for update failed*/
    @Test
    public void testUpdateAccountDetails_02() throws Exception {
        Mockito.when(accountsService.updateAccountDetails(Mockito.any())).thenReturn(false);
        CustomerDto customerDto = CustomerDto.builder()
                .name("somename").
                email("email@email.com")
                .mobileNumber("1234567890")
                .accountsDto(new AccountsDto())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(customerDto);

//        calling the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.put("/api/accounts/").contentType("application/json").content(jsonRequest);

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        assertEquals(417, status);
    }

    /*below checks for delete success*/
    @Test
    public void testDeleteAccount_01() throws Exception {
        String mobileNumber = "1234567890";
        Mockito.when(accountsService.deleteAccount(Mockito.any())).thenReturn(true);

//        calling the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.delete("/api/accounts/?mobileNumber=" + mobileNumber).contentType("application/json");

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        assertEquals(200, status);
    }

    /*below checks for delete fails*/
    @Test
    public void testDeleteAccount_02() throws Exception {
        String mobileNumber = "1234567890";
        Mockito.when(accountsService.deleteAccount(Mockito.any())).thenReturn(false);

//        calling the post request:
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.delete("/api/accounts/?mobileNumber=" + mobileNumber).contentType("application/json");

//        performing rest call
        MvcResult mvcResult = mockMvc.perform(reqBuilder).andReturn();

//        get the response
        MockHttpServletResponse response = mvcResult.getResponse();

        int status = response.getStatus();
        assertEquals(417, status);
    }

}
