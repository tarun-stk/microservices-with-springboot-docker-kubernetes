package com.eazybytes.accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;


//using third approach of configuring spring boot app
//which is using @ConfigurationProperties anno
//using this approach you can create any number of java pojo's for your properties, which eradicates use of hardcoded values
//this approach provides better way then using @Value & Environment (where we were using hardcoding)
//when using this approach u must use @EnableConfigurationProperties(value = {AccountsContactInfoDto.class}) on top of main class
//so that spring can recognise this class and provide a bean to work with
//@ConfigurationProperties(prefix = "accounts") when using this, all the values of params inside AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport)
//will be assigned from prefix accounts from application.yml file, and they will be final, cannot be changed


//later updated this class to java class
//so that we can update fields inside this class
//in future if we want to change properties inside yml file, those should be changed it can only be possible using setters
//so change this record to java class
@ConfigurationProperties(prefix = "accounts")
@Getter @Setter
public class AccountsContactInfoDto {

    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
