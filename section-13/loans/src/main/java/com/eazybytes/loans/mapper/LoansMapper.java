package com.eazybytes.loans.mapper;

import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;

public class LoansMapper {

    public static Loans  mapToLoans(LoansDto loansDto, Loans loan){
        loan.setLoanNumber(loansDto.getLoanNumber());
        loan.setLoanType(loansDto.getLoanType());
        loan.setTotalLoan(loansDto.getTotalLoan());
        loan.setOutstandingAmount(loansDto.getOutstandingAmount());
        loan.setAmountPaid(loansDto.getAmountPaid());
        loan.setMobileNumber(loansDto.getMobileNumber());
        return  loan;
    }

    public static LoansDto mapToLoansDto(Loans loan, LoansDto loansDto){
        loansDto.setLoanNumber(loan.getLoanNumber());
        loansDto.setLoanType(loan.getLoanType());
        loansDto.setTotalLoan(loan.getTotalLoan());
        loansDto.setOutstandingAmount(loan.getOutstandingAmount());
        loansDto.setAmountPaid(loan.getAmountPaid());
        loansDto.setMobileNumber(loan.getMobileNumber());
        return  loansDto;
    }
}
