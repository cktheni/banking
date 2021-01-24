package com.training.banking.repository;

import com.training.banking.entity.BankCnfValidator;
import com.training.banking.entity.id.BankCnfValidatorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCnfValidatorJpaRepository extends JpaRepository<BankCnfValidator, BankCnfValidatorId> {

}
