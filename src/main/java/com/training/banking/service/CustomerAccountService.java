package com.training.banking.service;

import com.training.banking.entity.BankCnfValidator;
import com.training.banking.entity.Customer;
import com.training.banking.entity.CustomerAccount;
import com.training.banking.entity.CustomerAccountCnfSetup;
import com.training.banking.repository.CustomerJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerAccountService {

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @Autowired
    private CustomerAccountCnfService customerAccountCnfService;

    @Autowired
    private MetaDataService metaDataService;


    @Transactional
    public Customer registerCustomerAccount(Customer customerIn) throws Exception {
        try {
            log.info("Calling registerCustomerAccount");

            //customerIn = ValidateCustomer(customerIn);

            List<String> errors = validateCustomer(customerIn);

            if (!CollectionUtils.isEmpty(errors)) {
                customerIn.setErrors(new ArrayList());
                customerIn.getErrors().addAll(errors);
            }

            if (CollectionUtils.isEmpty(customerIn.getErrors())) {

                CustomerAccountCnfSetup customerAccountCnfSetup = customerAccountCnfService.generateCustomerRelNumberAndAccount(customerIn.getCountryCode(), customerIn.getCustomerAccount().getCurrencyCode());
                log.info("Relationship Number and Account Numbers  generated Successfully:" + customerAccountCnfSetup);

                customerIn.setRelationshipNumber(customerAccountCnfSetup.getRelationshipNumber());

                customerIn.getCustomerAccount().setCountryCode(customerIn.getCountryCode());
                customerIn.getCustomerAccount().setMobileNumber(customerIn.getMobileNumber());
                customerIn.getCustomerAccount().setRelationshipNumber(customerIn.getRelationshipNumber());
                customerIn.getCustomerAccount().setAccountNumber(customerAccountCnfSetup.getAccountNumber());
                customerIn.setResponse("Customer Relationship Number:" + customerIn.getRelationshipNumber() + " and  Account:" + customerIn.getCustomerAccount().getAccountNumber() + " Registered Successfully");
                log.info("Register Customer for :" + customerIn);
                customerJpaRepository.save(customerIn);
            } else {
                log.info("Validation Error!!");
                customerIn.setResponse("Unable to Register Customer!!");
            }
            return customerIn;

        } catch (Exception e) {
            log.error("Exception Occurs while registerCustomerAccount", e);
            throw new Exception("Unable to Register Customer !! Please contact Support Team..");
        }
    }

    /*private Customer ValidateCustomer(Customer customerIn)
            throws ServiceException, IllegalAccessException, InvocationTargetException {
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(customerIn.getCountryCode())) {
            errors.add("Country Code Should be mandatory");
        }
        if (StringUtils.isEmpty(customerIn.getFirstName())) {
            errors.add("First Name should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getLastName())) {
            errors.add("Last Name should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getMobileNumber())) {
            errors.add("Mobile Number should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getEmailId())) {
            errors.add("Email ID should be mandatory");
        }

        if (customerIn.getDateOfBirth() == null) {
            errors.add("Date of Birth should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getPanNumber())) {
            errors.add("Last Name should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getCustomerAddress())) {
            errors.add("Last Name should be mandatory");
        }

        if (customerIn.getRegistrationDate() == null) {
            errors.add("Registration Date should  be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getCustomerAccount().getCurrencyCode())) {
            errors.add("Customer Account Currency should be mandatory");
        }

        if (StringUtils.isEmpty(customerIn.getCustomerAccount().getBranch())) {
            errors.add("Customer Account Branch should be mandatory");
        }

        if (customerIn.getCustomerAccount().getAvailableBalance() < 0) {
            errors.add("Account Balance should not be Negative");
        }
        if (!CollectionUtils.isEmpty(errors)) {
            customerIn.setErrors(new ArrayList());
            customerIn.getErrors().addAll(errors);
        }

        return customerIn;
    }*/


    private List<String> validateCustomer(Customer customer) throws Exception {
        List<String> errors = new ArrayList<>();

        List<BankCnfValidator> beanValidatorsList = metaDataService.loadPropertyMap().get("customer");
        for (BankCnfValidator bankCnfValidator : beanValidatorsList) {
            Object value = PropertyUtils.getProperty(customer, bankCnfValidator.getFieldName());

            if (StringUtils.equalsIgnoreCase(bankCnfValidator.getValidationType(), "MANDATORY")) {
                if (mandatoryValidator(value)) {
                    errors.add(bankCnfValidator.getValidationMessage());
                }
            }
        }
        List<String> errors1 = validateCustomerAccount(customer.getCustomerAccount());
        if (!CollectionUtils.isEmpty(errors1)) {
            errors.addAll(errors1);
        }
        Customer customerMobile = customerJpaRepository.findByMobileNumber(customer.getMobileNumber());
        if(customerMobile!=null)
        {
            errors.add("Customer Already registered with  Mobile  Number:"+customerMobile.getMobileNumber() + ",for the Rel Number:"+customerMobile.getRelationshipNumber() +", Account Number:"+customerMobile.getCustomerAccount().getAccountNumber());
        }

        Customer  customerPan =customerJpaRepository.findByPanNumber(customer.getPanNumber());

        if(customerPan!=null)
        {
            errors.add("Customer Already registered with  Pan Number:"+customerPan.getPanNumber() + ",for the Rel Number:"+customerPan.getRelationshipNumber() +", Account Number:"+customerPan.getCustomerAccount().getAccountNumber());
        }

        return errors;
    }

    private List<String> validateCustomerAccount(CustomerAccount customerAccount) throws Exception {
        List<String> errors = new ArrayList<>();

        List<BankCnfValidator> beanValidatorsList = metaDataService.loadPropertyMap().get("customerAccount");
        for (BankCnfValidator bankCnfValidator : beanValidatorsList) {
            Object value = PropertyUtils.getProperty(customerAccount, bankCnfValidator.getFieldName());

            if (StringUtils.equalsIgnoreCase(bankCnfValidator.getValidationType(), "MANDATORY")) {
                if (mandatoryValidator(value)) {
                    errors.add(bankCnfValidator.getValidationMessage());
                }
            }
        }
        return errors;
    }


    private static boolean mandatoryValidator(Object value) {
        boolean result = false;
        if (value == null) {
            result = true;
        }
        if (TypeUtils.isInstance(value, String.class) && StringUtils.isBlank(StringUtils.defaultString((String) value))) {
            result = true;
        }
        return result;
    }


}


