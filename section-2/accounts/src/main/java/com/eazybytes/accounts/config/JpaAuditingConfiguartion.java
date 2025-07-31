package com.eazybytes.accounts.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*moved below annotation from main class because of below issue:
http://stackoverflow.com/questions/60606861/spring-boot-jpa-metamodel-must-not-be-empty-when-trying-to-run-junit-integrat*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl") // -> enabling jpa auditing,  //-> give who is your auditor
public class JpaAuditingConfiguartion {
}
