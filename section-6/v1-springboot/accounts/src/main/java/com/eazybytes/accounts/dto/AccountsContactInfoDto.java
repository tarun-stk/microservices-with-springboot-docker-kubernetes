package com.eazybytes.accounts.dto;

//Notes on java record:
//observe that this is not class but record
//records were introduced in java 17
//to work with final fields, and no setters
//fields that you've inside params, are by default final, and java by default creates those final fields, getters & constructor for them
//make sure field types match with those that you've mentioned in application.yml file
//also fieldnames should match with application.yml keys(fieldname)

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

@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
}
