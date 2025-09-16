package com.chubbTest.customer.infrastructure.database.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubbTest.customer.infrastructure.database.model.CustomerEntity;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByNumCTA(String numCTA);

    boolean existsByNumCTA(String numCTA);
}