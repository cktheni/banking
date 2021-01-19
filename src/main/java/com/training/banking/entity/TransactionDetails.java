package com.training.banking.entity;

import com.training.banking.constant.AppConstants;
import com.training.banking.entity.id.TransactionDetailsId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "TRANSFERS_TRANSACTION_DETAILS")
@IdClass(TransactionDetailsId.class)
public class TransactionDetails  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COUNTRY_CODE", nullable = false, length = 3)
    private String countryCode;

    @Id
    @Column(name = "TRANSACTION_ID", nullable = false, length = 300)
    private String transactionId;

    @Column(name = "RELATIONSHIP_NO", nullable = false, length = 15)
    private String relationshipNo;

    @Column(name = "MOBILE_NO", nullable = false, length = 15)
    private String mobileNumber;

    @Column(name = "FROM_ACCOUNT_NUMBER", length = 15)
    private String fromAccountNumber;

    @Column(name = "FROM_ACCOUNT_CURRENCY", length = 15)
    private String fromAccountCurrency;

    @Column(name = "TO_ACCOUNT_NUMBER", length = 100)
    private String toAccountNumber;

    @Column(name = "TO_ACCOUNT_CURRENCY", length = 15)
    private String toAccountCurrency;

    @Column(name = "TO_ACCOUNT_NAME", length = 100)
    private String toCustomerName;

    @Column(name = "TRANSACTION_AMOUNT", length = 50)
    private Double transactionAmount;

    @Column(name = "TRANSACTION_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT " + AppConstants.DEFAULT_CREATED_DATE)
    private Date transactionDate;

    @Column(name = "REFERENCE_NO", nullable = false, length = 500)
    private String referenceNumber;


    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "CREATED_BY", nullable = false, length = 100, columnDefinition = "VARCHAR2(100 CHAR) DEFAULT '" + AppConstants.DEFAULT_CREATED_BY + "'")
    private String createdBy;

    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT " + AppConstants.DEFAULT_CREATED_DATE)
    private Date createdDate;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @PrePersist
    void preInsert() {
        if (this.createdBy == null) {
            setCreatedBy(AppConstants.DEFAULT_CREATED_BY);
        }
        if(this.transactionDate==null)
        {
            setTransactionDate(new Date());
        }
        if (this.createdDate == null) {
            setCreatedDate(new Date());
        }

    }

    @PreUpdate
    void preUpdate() {
        if (this.updatedBy == null) {
            setUpdatedBy(AppConstants.DEFAULT_CREATED_BY);
        }
        setUpdatedDate(new Date());
    }
}
