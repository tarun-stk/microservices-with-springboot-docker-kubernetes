package com.eazybytes.accounts.repository;

import com.eazybytes.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(Long customerId);


//    @Modifying -> whenever we're modifying data inside database, this anno shoyld be mentioned
//    to retreive records from db using find methods like findByCustomerId, doesn't require this anno
//    as we're not modifying data, but just retreiving.
//    @Transactional -> whenever we're modifying data inside database, this anno shoyld be mentioned
//    if some exception happens during the operation, and data being partially modified, in that
//    scenario the partially modified data will be rolled back (rollback transaction)

//    Overall combination -> modify db in a transaction
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
