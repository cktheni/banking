package com.training.banking.repository;

import com.training.banking.entity.Customer;
import com.training.banking.entity.id.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerJpaRepository  extends JpaRepository<Customer, CustomerId> {

    Customer findByPanNumber(String panNumber);
    Customer findByMobileNumber(String mobileNumber);
}
