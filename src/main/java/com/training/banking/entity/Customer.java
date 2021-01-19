package com.training.banking.entity;

import com.training.banking.constant.AppConstants;
import com.training.banking.entity.id.CustomerId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@IdClass(CustomerId.class)
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COUNTRY_CODE", nullable = false, length = 3, unique = true)
    private String countryCode;

    @Id
    @Column(name = "RELATIONSHIP_NO", nullable = false, length = 20, unique = true)
    private String relationshipNumber;

    @Column(name = "FIRST_NAME", length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50)
    private String lastName;

    @Column(name = "MOBILE_NO", nullable = false, length = 20)
    private String mobileNumber;

    @Column(name = "EMAIL_ID", length = 150)
    private String emailId;

    @Column(name = "DATE_OF_BIRTH", length = 150)
    private Date dateOfBirth;

    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name = "CUSTOMER_ADDRESS", length = 200)
    private String customerAddress;

    @Column(name = "CREATED_BY", nullable = false, length = 50, columnDefinition = "VARCHAR2(100 CHAR) DEFAULT '" + AppConstants.DEFAULT_CREATED_BY + "'")
    private String createdBy;

    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT " + AppConstants.DEFAULT_CREATED_DATE)
    private Date createdDate;

    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CustomerAccount customerAccount;

    @PrePersist
    void preInsert() {
        if (this.createdBy == null) {
            setCreatedBy(AppConstants.DEFAULT_CREATED_BY);
        }
        if (this.createdDate == null) {
            setCreatedDate(new Date());
        }
        if (this.registrationDate == null) {
            setRegistrationDate(new Date());
        }
    }
    @PreUpdate
    void preUpdate() {
        if (this.updatedBy == null) {
            setUpdatedBy(AppConstants.DEFAULT_CREATED_BY);
        }
    }
}
