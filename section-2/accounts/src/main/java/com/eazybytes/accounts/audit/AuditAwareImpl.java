package com.eazybytes.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {  //since createdBy & UpdatedBy are strings
//    we're using AuditorAware<String> in class definition


    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
//    currently returning hard coded value
//    will be implemented when working with spring security
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNTS_MS");
    }
}
