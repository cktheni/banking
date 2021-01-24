package com.training.banking.endpoint;

import com.training.banking.entity.TransactionDetails;
import com.training.banking.service.IntraBankTransfersService;
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
@RequestMapping("/fundTransfer")
public class IntraBankFundTransfersEndpoint {

@Autowired
IntraBankTransfersService intraBankTransfersService;

    @RequestMapping(value = "/intraBank", method = RequestMethod.POST)
    public ResponseEntity postTransaction(@RequestBody TransactionDetails transactionDetails) {
        try {
             transactionDetails = intraBankTransfersService.postTransaction(transactionDetails);
            if (transactionDetails != null && CollectionUtils.isEmpty(transactionDetails.getErrors())){
                return ResponseEntity.ok(transactionDetails);
            } else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(transactionDetails);
            }
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body( e.getMessage());
        }
    }
}
