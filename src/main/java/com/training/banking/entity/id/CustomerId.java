package com.training.banking.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String countryCode;
    private String relationshipNumber;
}
