package com.training.banking.endpoint;

import com.training.banking.constant.AppConstants;
import com.training.banking.entity.TransactionDetails;
import com.training.banking.service.IntraBankTransfersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class StatementRequestEndPoint {

    @Autowired
    IntraBankTransfersService intraBankTransfersService;

    @RequestMapping(value = "/statements", method = RequestMethod.GET)
    public ResponseEntity getTransaction(@RequestParam String countryCode, @RequestParam String accountNumber,@RequestParam Integer month, @RequestParam Integer year) {
        try {
            List<TransactionDetails> transactionDetailsList = intraBankTransfersService.getTransactionDetails(countryCode,accountNumber,month,year);
            if (!CollectionUtils.isEmpty(transactionDetailsList)) {
                return ResponseEntity.ok(transactionDetailsList);
            } else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(AppConstants.FAILURE_RESPONSE);
            }
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body( e.getMessage());
        }
    }
}
