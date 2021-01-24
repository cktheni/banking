package com.training.banking.service;

import com.training.banking.entity.BankCnfValidator;
import com.training.banking.entity.Customer;
import com.training.banking.entity.CustomerAccount;
import com.training.banking.entity.TransactionDetails;
import com.training.banking.entity.id.CustomerAccountId;
import com.training.banking.repository.CustomerAccountJpaRepository;
import com.training.banking.repository.CustomerJpaRepository;
import com.training.banking.repository.TransactionDetailsJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
@Service
public class IntraBankTransfersService {

    @Autowired
    TransactionDetailsJpaRepository transactionDetailsJpaRepository;
    @Autowired
    CustomerAccountJpaRepository customerAccountJpaRepository;

    @Autowired
    CustomerJpaRepository customerJpaRepository;

    @Autowired
    private MetaDataService metaDataService;


    @Transactional
    public TransactionDetails postTransaction(TransactionDetails  transactionDetails) throws Exception {
        try {
            boolean isValidTransaction = false;
            log.info("Calling PostTransaction");
            List<String> errors = new ArrayList<>();

            Customer customer = customerJpaRepository.findByMobileNumber(transactionDetails.getMobileNumber());
            if(customer!=null) {
                transactionDetails.setCountryCode(customer.getCountryCode());
                transactionDetails.setRelationshipNo(customer.getRelationshipNumber());
                isValidTransaction=true;
            }
            else
            {
                errors.add("Given Mobile Number Not Registered for Fund Transfer");

            }
            TransactionDetails creditTransactionDetails = transactionDetails;
            if(isValidTransaction)
            {
                errors = validateTransactionDetails(transactionDetails);
                if (CollectionUtils.isEmpty(errors)) {
                    Optional<CustomerAccount> fromCustomerAccount =  customerAccountJpaRepository.findById(new CustomerAccountId(transactionDetails.getCountryCode(),transactionDetails.getRelationshipNo(),transactionDetails.getFromAccountNumber(),transactionDetails.getFromAccountCurrency()));

                    if (fromCustomerAccount.isPresent()) {

                        CustomerAccount fromCustAccount = fromCustomerAccount.get();

                        if(fromCustAccount.getAvailableBalance() >= transactionDetails.getTransactionAmount())
                        {
                            CustomerAccount toCustomerAccount = customerAccountJpaRepository.findByAccountNumberAndCurrencyCode(transactionDetails.getToAccountNumber(),transactionDetails.getToAccountCurrency());
                            if(toCustomerAccount !=null)
                            {
                                fromCustAccount.setAvailableBalance(fromCustAccount.getAvailableBalance() - transactionDetails.getTransactionAmount());
                                fromCustAccount = customerAccountJpaRepository.save(fromCustAccount);

                                transactionDetails.setTransactionType("D");
                                transactionDetails.setTransactionId(UUID.randomUUID().toString());
                                transactionDetails.setReferenceNumber(StringUtils.join(transactionDetails.getRelationshipNo() ,"-", transactionDetails.getTransactionId()));
                                transactionDetails.setTransactionType("D");
                                log.info(fromCustAccount.getAccountNumber() +" Debited with :"+transactionDetails.getTransactionAmount() +", Available Balance :"+fromCustAccount.getAvailableBalance());
                                log.info("Debit transactionDetails:"+transactionDetails);

                                transactionDetails = transactionDetailsJpaRepository.save(transactionDetails);
                                log.info("Debit Transaction details Saved Successfully");

                                toCustomerAccount.setAvailableBalance(toCustomerAccount.getAvailableBalance()+transactionDetails.getTransactionAmount());
                                toCustomerAccount = customerAccountJpaRepository.save(toCustomerAccount);

                                log.info(toCustomerAccount.getAccountNumber() +" Credited with :"+transactionDetails.getTransactionAmount() +", Available Balance :"+toCustomerAccount.getAvailableBalance());

                                creditTransactionDetails.setTransactionType("C");
                                creditTransactionDetails.setTransactionId(UUID.randomUUID().toString());
                                creditTransactionDetails.setReferenceNumber(transactionDetails.getReferenceNumber());
                                creditTransactionDetails.setRelationshipNo(toCustomerAccount.getRelationshipNumber());
                                creditTransactionDetails.setMobileNumber(toCustomerAccount.getMobileNumber());
                                creditTransactionDetails.setFromAccountNumber(toCustomerAccount.getAccountNumber());
                                creditTransactionDetails.setFromAccountCurrency(toCustomerAccount.getCurrencyCode());
                                creditTransactionDetails.setToAccountNumber(fromCustAccount.getAccountNumber());
                                creditTransactionDetails.setToAccountCurrency(fromCustAccount.getCurrencyCode());
                                creditTransactionDetails.setTransactionDate(new Date());
                                creditTransactionDetails.setCreatedDate(new Date());

                                log.info("Credit transactionDetails:"+creditTransactionDetails);

                                transactionDetails = transactionDetailsJpaRepository.save(creditTransactionDetails);
                                log.info("Credit Transaction details Saved Sucessfully");
                                transactionDetails.setResponse("Transaction Success");
                            }
                            else
                            {
                                log.info("To Account Details not correct");
                                errors.add("Given To Account details :"+StringUtils.join(transactionDetails.getToAccountNumber(),"-",transactionDetails.getToAccountCurrency() + " Not Found"));
                            }
                        }
                        else
                        {
                            log.info("Insufficient Balance");
                            errors.add("Insufficient Balance, Availablable Balance:"+fromCustomerAccount.get().getAvailableBalance() + ", Transaction Amount:"+transactionDetails.getTransactionAmount());
                        }
                    }
                    else
                    {
                        log.info("From Account details not correct");
                        errors.add("Given Country code , Relationship number, From Account details :"+StringUtils.join(transactionDetails.getCountryCode(),"-",transactionDetails.getRelationshipNo(),"-"+transactionDetails.getFromAccountNumber())+" not found");
                    }
                }
            }
            if (!CollectionUtils.isEmpty(errors)) {
                transactionDetails.setErrors(new ArrayList());
                transactionDetails.getErrors().addAll(errors);
            }

            if (!CollectionUtils.isEmpty(transactionDetails.getErrors())) {
                transactionDetails.setResponse("Transaction Fail!!");
            }
              return transactionDetails;

        } catch (Exception e) {
            log.error("Exception in Post Transaction {}",e);
            throw new Exception("PostTransaction Exception:" + e.getMessage(), e);
        }

    }

    public List<TransactionDetails> getTransactionDetails(String accountNumber, Integer month, Integer year) throws Exception
    {
        try {
            String countryCode="";
            List<TransactionDetails> transactionDetailsLst =null;
            CustomerAccount customerAccount  = customerAccountJpaRepository.findByAccountNumber(accountNumber);
            if(customerAccount!=null)
            {
                countryCode = customerAccount.getCountryCode();
                transactionDetailsLst= transactionDetailsJpaRepository.getTransactionDetails(countryCode,accountNumber,month,year);
            }
            else
            {
                throw new Exception("Given Account Number Not Found");
            }
            return transactionDetailsLst;
        }
        catch (Exception e) {
            log.error("Exception in Post getTransactionDetails {}",e);
            throw new Exception("getTransactionDetails Exception:" + e.getMessage(), e);
        }
    }

    private List<String>  validateTransactionDetails(TransactionDetails transactionDetails) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        List<String> errors = new ArrayList<>();
        if(StringUtils.equalsIgnoreCase(transactionDetails.getFromAccountNumber(), transactionDetails.getToAccountNumber()))
        {
            errors.add("From Account and To Account should not be same");
        }
        else
        {

            if(!StringUtils.equalsIgnoreCase(transactionDetails.getFromAccountCurrency(),transactionDetails.getToAccountCurrency()))
            {
                errors.add("From Account and To Account Currency should  be same");
            }
            else
            {
                List<BankCnfValidator> beanValidatorsList = metaDataService.loadPropertyMap().get("transactionDetails");
                for (BankCnfValidator bankCnfValidator : beanValidatorsList) {
                    Object value = PropertyUtils.getProperty(transactionDetails, bankCnfValidator.getFieldName());

                    if (StringUtils.equalsIgnoreCase(bankCnfValidator.getValidationType(), "MANDATORY")) {
                        if (mandatoryValidator(value)) {
                            errors.add(bankCnfValidator.getValidationMessage());
                        }
                    }
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
