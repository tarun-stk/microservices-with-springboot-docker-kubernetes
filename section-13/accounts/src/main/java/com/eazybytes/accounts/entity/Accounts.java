package com.eazybytes.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Accounts extends BaseEntity{
    private Long customerId;

    @Id
    private Long accountNumber;

    private String accountType;

    private String branchAddress;

    @Column(name = "communication_sw")
    private Boolean communicationSw;
}
