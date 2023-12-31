package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exception.LoanAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoansService {

    private LoansRepository loansRepository;
    @Override
    public void createLoan(String mobileNumber) {

        Optional<Loans> OptionalLoan = loansRepository.findByMobileNumber(mobileNumber);
        if(OptionalLoan.isPresent()){
            throw new LoanAlreadyExistsException("Loan already registered with mobile number: " + mobileNumber);
        }
        Loans theCreatedLoan = createNewLoan(mobileNumber);
        loansRepository.save(theCreatedLoan);
    }

    @Override
    public LoansDto fetchLoanDetails(String mobileNumber) {

        Loans theLoan = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
//        map loan to laondto
        LoansDto loansDto = LoansMapper.mapToLoansDto(theLoan, new LoansDto());
//        return loansDtos;
        return loansDto;
    }

    @Override
    public boolean updateLoanDetails(LoansDto theLoansDto) {

        Loans loan = loansRepository.findByLoanNumber(theLoansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loans", "loanNumber", theLoansDto.getLoanNumber())
        );
        LoansMapper.mapToLoans(theLoansDto, loan);
        loansRepository.save(loan);
        return true;
    }

    @Override
    public boolean deleteLoanDetails(String mobileNumber) {

        Loans theLoan = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(theLoan.getLoanId());
        return true;
    }

    private Loans createNewLoan(String mobileNumber) {

        Loans theLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        theLoan.setLoanNumber(Long.toString(randomLoanNumber));
        theLoan.setMobileNumber(mobileNumber);
        theLoan.setLoanType(LoansConstants.HOME_LOAN);
        theLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        theLoan.setAmountPaid(0);
        theLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);

        return theLoan;

    }
}




















