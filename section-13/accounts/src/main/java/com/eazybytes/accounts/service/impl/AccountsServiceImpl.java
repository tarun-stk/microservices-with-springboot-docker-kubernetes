package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.AccountsMsgDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class AccountsServiceImpl implements IAccountsService {

//    Don't need @Autowired anno because we're using @AllArgsConstructor which acts as constructor injection for
//    all the fields in this class
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto) {

//        find if customer is already registered
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer with mobile number: " + customerDto.getMobileNumber()
             + " already exists");
        }

//        mapping dto to customer entity
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
//        saving the entity.
        Customer savedCustomer = customerRepository.save(customer);
        Accounts createdAccount = createNewAccount(savedCustomer);

        accountsRepository.save(createdAccount);

//        sending communication to rabbit MQ that account has been created with a message
        sendCommunication(createdAccount, savedCustomer);

    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
//        sending the message, accountsMsgDto
//        to the destination, sendCommunication-out-0
//        using streamBridge
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

//        retrieving customer details using mobileNumber
//        if not present then will throw an exception ResourceNotFoundException
        Customer theCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Long customerId = theCustomer.getCustomerId();
//        retrieving accounts details using customer id
//        if not present then will throw an exception ResourceNotFoundException
        Accounts theAccount = accountsRepository.findByCustomerId(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customerId.toString())
        );

//        Mapping entities to dtos
        CustomerDto theCustomerDto = CustomerMapper.mapToCustomerDto(theCustomer, new CustomerDto());
        AccountsDto theAccountsDto = AccountsMapper.mapToAccountsDto(theAccount, new AccountsDto());

//        setting theAccountsDto to theCustomerDto
        theCustomerDto.setAccountsDto(theAccountsDto);

        return theCustomerDto;
    }

    @Override
    public boolean updateAccountDetails(CustomerDto theCustomerDto) {

        boolean isUpdated = false;
//        fetchging AccountsDto
        AccountsDto theAccountsDto = theCustomerDto.getAccountsDto();
        if(theAccountsDto != null){
//            if there is no record based on passed accountNumber then throw ResourceNotFoundException
            Accounts theAccounts = accountsRepository.findById(theAccountsDto.getAccountNumber())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "accountNumber", theAccountsDto.getAccountNumber().toString())
                    );
//            Now it means we've a account record with passed accountNumber
//            map the dto to account entity
            AccountsMapper.mapToAccounts(theAccountsDto, theAccounts);
//            update the accounts
            accountsRepository.save(theAccounts);

            Long customerId = theAccounts.getCustomerId();
//            if there is no record based on customerId then throw ResourceNotFoundException
            Customer theCustomer = customerRepository.findById(customerId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Customer", "mobileNumber", theCustomerDto.getMobileNumber())
                    );

//            map the customer dto to customer entity
            CustomerMapper.mapToCustomer(theCustomerDto, theCustomer);
//            /update the customer entity
            customerRepository.save(theCustomer);

//            update isUpdated to true;
            isUpdated = true;

        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
//        retrieving customer details using mobileNumber
//        if not present then will throw an exception ResourceNotFoundException
        Customer theCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

//        delete account
        accountsRepository.deleteByCustomerId(theCustomer.getCustomerId());
//        delete customer
        customerRepository.deleteById(theCustomer.getCustomerId());
        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }

    //    creating new account based on customer entity
    private Accounts createNewAccount(Customer savedCustomer) {

        Accounts newAccount = new Accounts();
//        generating a random id of length 10 digits
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCustomerId(savedCustomer.getCustomerId());
        return newAccount;
    }
}
















