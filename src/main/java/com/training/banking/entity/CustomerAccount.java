package com.training.banking.entity;

import com.training.banking.constant.AppConstants;
import com.training.banking.entity.id.CustomerAccountId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@IdClass(CustomerAccountId.class)
@Table(name = "CUSTOMER_ACCOUNT")
public class CustomerAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COUNTRY_CODE", nullable = false, length = 3)
    private String countryCode;

    @Id
    @Column(name = "RELATIONSHIP_NO", nullable = false, length = 20)
    private String relationshipNumber;

    @Id
    @Column(name = "ACCOUNT_NO", nullable = false, length = 20)
    private String accountNumber;

    @Id
    @Column(name = "ACCOUNT_CURRENCY", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "MOBILE_NO", nullable = false, length = 20)
    private String mobileNumber;

    @OneToOne
    @JoinColumns(value = {@JoinColumn(name = "COUNTRY_CODE", nullable = false, insertable = false, updatable = false), @JoinColumn(name = "RELATIONSHIP_NO", nullable = false, insertable = false, updatable = false)})
    private Customer customer;

    @Column(name = "BRANCH", length = 100)
    private String branch;

    @Column(name = "AVAILABLE_BALANCE")
    private Double availableBalance;

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
        if (this.createdDate == null) {
            setCreatedDate(new Date());
        }
    }
    @PreUpdate
    void preUpdate() {
        if (this.updatedBy == null) {
            setUpdatedBy(AppConstants.DEFAULT_CREATED_BY);
        }
    }

}
