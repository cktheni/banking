package com.training.banking.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankCnfValidatorId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String entityName;
    private String fieldName;


}
