package com.training.banking.service;

import com.training.banking.entity.Customer;
import com.training.banking.entity.CustomerAccountCnfSetup;
import com.training.banking.repository.CustomerJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class CustomerAccountService {

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @Autowired
    private CustomerAccountCnfService  customerAccountCnfService;

    @Transactional
    public Customer registerCustomerAccount(Customer customerIn) throws Exception {
        try {
            log.info("Calling registerCustomerAccount");

            CustomerAccountCnfSetup customerAccountCnfSetup = customerAccountCnfService.generateCustomerRelNumberAndAccount(customerIn.getCountryCode(),customerIn.getCustomerAccount().getCurrencyCode());

            log.info("Relationship Number and Account Numbers  generated Successfully:"+customerAccountCnfSetup);

            customerIn.setRelationshipNumber(customerAccountCnfSetup.getRelationshipNumber());

            customerIn.getCustomerAccount().setCountryCode(customerIn.getCountryCode());
            customerIn.getCustomerAccount().setMobileNumber(customerIn.getMobileNumber());
            customerIn.getCustomerAccount().setRelationshipNumber(customerIn.getRelationshipNumber());
            customerIn.getCustomerAccount().setAccountNumber(customerAccountCnfSetup.getAccountNumber());

            log.info("Register Customer for :"+customerIn);

            customerJpaRepository.save(customerIn);
            return customerIn;

        } catch (Exception e) {
            log.error("Exception Occurs while registerCustomerAccount",e);
            throw new Exception("registerCustomer Exception:" + e.getMessage(), e);
        }
    }
}
