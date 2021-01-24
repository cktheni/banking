package com.training.banking.entity;

import com.training.banking.entity.id.BankCnfValidatorId;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(BankCnfValidatorId.class)
@Table(name = "BANK_CNF_VALIDATOR")
public class BankCnfValidator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ENTITY_NAME", nullable = false, length = 50, unique = true)
    private String entityName;

    @Id
    @Column(name = "FIELD_NAME", nullable = false, length = 50, unique = true)
    private String fieldName;

    @Column(name = "VALIDATION_TYPE", length = 50)
    private String validationType;

    @Column(name = "FIELD_TYPE", length = 50)
    private String fieldType;

    @Column(name = "VALIDATION_MESSAGE", length = 250)
    private String validationMessage;

    public String getKey() {
        return entityName;
    }

}
