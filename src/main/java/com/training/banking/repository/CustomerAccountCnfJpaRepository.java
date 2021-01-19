package com.training.banking.repository;

import com.training.banking.entity.CustomerAccountCnfSetup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountCnfJpaRepository extends JpaRepository<CustomerAccountCnfSetup, Long> {

    CustomerAccountCnfSetup findTopByOrderByIdDesc();


}
