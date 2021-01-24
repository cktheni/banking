package com.training.banking.endpoint;

import com.training.banking.entity.Customer;
import com.training.banking.service.CustomerAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customeraccount")
public class CustomerAccountEndpoint {

    @Autowired
    private CustomerAccountService customerAccountService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity registerCustomerAccount(@RequestBody Customer customerIn) {
        try {
            Customer customer = customerAccountService.registerCustomerAccount(customerIn);
            if (customer != null && CollectionUtils.isEmpty(customerIn.getErrors()) ) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(customer);
            }
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body( e.getMessage());
        }
    }
}
