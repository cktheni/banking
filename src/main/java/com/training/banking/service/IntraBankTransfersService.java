package com.training.banking.service;

import com.training.banking.entity.CustomerAccount;
import com.training.banking.entity.TransactionDetails;
import com.training.banking.entity.id.CustomerAccountId;
import com.training.banking.repository.CustomerAccountJpaRepository;
import com.training.banking.repository.TransactionDetailsJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class IntraBankTransfersService {

    @Autowired
    TransactionDetailsJpaRepository transactionDetailsJpaRepository;
    @Autowired
    CustomerAccountJpaRepository customerAccountJpaRepository;

    @Transactional
    public TransactionDetails postTransaction(TransactionDetails  transactionDetails) throws Exception {
        try {
            boolean isValidTransaction = false;
            log.info("Calling PostTransaction");
            TransactionDetails creditTransactionDetails = transactionDetails;
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
                    }
                    else
                    {
                        log.info("To Account Details not correct");
                        throw new Exception("Given To Account details :"+StringUtils.join(transactionDetails.getToAccountNumber(),"-",transactionDetails.getToAccountCurrency() + " Not Found"));
                    }
                }
                else
                {
                    log.info("Insufficient Balance");
                    throw new Exception("Insufficient Balance, Availablable Balance:"+fromCustomerAccount.get().getAvailableBalance() + ", Transaction Amount:"+transactionDetails.getTransactionAmount());
                }
            }
            else
            {
                log.info("From Account details not correct");
                throw new Exception("Given Country code , Relationship number, From Account details :"+StringUtils.join(transactionDetails.getCountryCode(),"-",transactionDetails.getRelationshipNo(),"-"+transactionDetails.getFromAccountNumber())+" not found");
            }

            return transactionDetails;

        } catch (Exception e) {
            log.error("Exception in Post Transaction {}",e);
            throw new Exception("PostTransaction Exception:" + e.getMessage(), e);
        }

    }

    public List<TransactionDetails> getTransactionDetails(String countryCode, String accountNumber, Integer month, Integer year) throws Exception
    {
        try {
            List<TransactionDetails> transactionDetailsLst = transactionDetailsJpaRepository.getTransactionDetails(countryCode,accountNumber,month,year);
            return transactionDetailsLst;
        }
        catch (Exception e) {
            log.error("Exception in Post getTransactionDetails {}",e);
            throw new Exception("getTransactionDetails Exception:" + e.getMessage(), e);
        }
    }

}
