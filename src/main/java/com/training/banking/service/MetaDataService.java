package com.training.banking.service;


import com.training.banking.entity.BankCnfValidator;
import com.training.banking.repository.BankCnfValidatorJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MetaDataService {
    @Autowired
    private BankCnfValidatorJpaRepository validatorRepository;

    public  Map<String, List<BankCnfValidator>> loadPropertyMap() {
        Map<String, List<BankCnfValidator>> validatorMap = Optional.ofNullable(validatorRepository.findAll()).orElseGet(Collections::emptyList).stream().collect(Collectors.groupingBy(BankCnfValidator::getKey));
        log.info("Validator Map is loaded !!!.Size : {}", validatorMap.size());
        return validatorMap;
    }
}
