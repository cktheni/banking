package com.training.banking.repository;

import com.training.banking.entity.Customer;
import com.training.banking.entity.id.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerJpaRepository  extends JpaRepository<Customer, CustomerId> {

    Customer findByCountryCodeAndRelationshipNumber(String countryCode, String relationshipNumber);
    void deleteByCountryCodeAndRelationshipNumber(String countryCode, String relationshipNumber);

}
