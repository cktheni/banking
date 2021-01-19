package com.training.banking.repository;

import com.training.banking.entity.TransactionDetails;
import com.training.banking.entity.id.TransactionDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionDetailsJpaRepository extends JpaRepository<TransactionDetails, TransactionDetailsId> {

    @Query("from TransactionDetails where countryCode=:countryCode and fromAccountNumber=:accountNumber and month(transactionDate) = :month and year(transactionDate) = :year")
    List<TransactionDetails> getTransactionDetails(@Param("countryCode") String countryCode, @Param("accountNumber") String accountNumber,@Param("month") Integer month, @Param("year") Integer year);
}
