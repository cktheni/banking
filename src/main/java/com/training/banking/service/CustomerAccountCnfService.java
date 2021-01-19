package com.training.banking.service;

import com.training.banking.constant.AppConstants;
import com.training.banking.entity.CustomerAccountCnfSetup;
import com.training.banking.repository.CustomerAccountCnfJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerAccountCnfService {

    @Autowired
    private CustomerAccountCnfJpaRepository customerAccountCnfJpaRepository;

    public CustomerAccountCnfSetup generateCustomerRelNumberAndAccount(String countryCode, String currency) throws Exception{
        try {
            log.info("Calling generateCustomerRelNumberAndAccount");

            CustomerAccountCnfSetup customerAccountCnfSetupTemp = new CustomerAccountCnfSetup();

            CustomerAccountCnfSetup customerAccountCnfSetup =customerAccountCnfJpaRepository.findTopByOrderByIdDesc();

            if(customerAccountCnfSetup !=null){

                Long   tempRelNumber     = Long.parseLong(customerAccountCnfSetup.getRelationshipNumber()) +1;
                Long   tempAccountNumber =  Long.parseLong(customerAccountCnfSetup.getAccountNumber())+1;

                customerAccountCnfSetupTemp.setCountryCode(countryCode);
                customerAccountCnfSetupTemp.setRelationshipNumber(String.valueOf(tempRelNumber));
                customerAccountCnfSetupTemp.setAccountNumber(String.valueOf(tempAccountNumber));
                customerAccountCnfSetupTemp.setCurrencyCode(currency);
            }
            else
            {
                customerAccountCnfSetupTemp.setCountryCode(countryCode);
                customerAccountCnfSetupTemp.setRelationshipNumber(AppConstants.DEFAULT_RELNUMBER);
                customerAccountCnfSetupTemp.setAccountNumber(String.valueOf(AppConstants.DEFAULT_ACCOUNTNUMBER));
                customerAccountCnfSetupTemp.setCurrencyCode(currency);
            }
            log.info("CustomerAccountCnfSetup :"+customerAccountCnfSetupTemp);
            customerAccountCnfSetupTemp =customerAccountCnfJpaRepository.save(customerAccountCnfSetupTemp);
            return customerAccountCnfSetupTemp;

        }catch(Exception e){
            log.error("Exception Occurs while generating  generateCustomerRelNumberAndAccount",e);
            throw new Exception("generateCustomerRelNumberAndAccount Exception:" + e.getMessage(), e);

        }

    }
}
