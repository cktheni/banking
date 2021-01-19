package com.training.banking.entity;

import com.training.banking.entity.id.CustomerAccountId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CUSTOMER_ACCOUNT_CNF_SETUP")
public class CustomerAccountCnfSetup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="CUST_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COUNTRY_CODE", nullable = false, length = 3, unique = true)
    private String countryCode;

    @Column(name = "RELATIONSHIP_NO", nullable = false, length = 20)
    private String relationshipNumber;

    @Column(name = "ACCOUNT_NO", nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "ACCOUNT_CURRENCY", nullable = false, length = 3)
    private String currencyCode;


}
