package com.training.banking.repository;

import com.training.banking.entity.CustomerAccount;
import com.training.banking.entity.id.CustomerAccountId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountJpaRepository extends JpaRepository<CustomerAccount, CustomerAccountId> {

    CustomerAccount findByAccountNumberAndCurrencyCode(String accountNumner,String accountCurrency);

}
