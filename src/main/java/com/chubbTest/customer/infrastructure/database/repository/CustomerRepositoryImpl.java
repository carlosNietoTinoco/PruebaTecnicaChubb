package com.chubbTest.customer.infrastructure.database.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.chubbTest.customer.application.repository.CustomerRepository;
import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.infrastructure.database.mapper.CustomerEntityMapper;
import com.chubbTest.customer.infrastructure.database.model.CustomerEntity;
import com.chubbTest.customer.infrastructure.database.repository.jpa.CustomerJpaRepository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerEntityMapper mapper;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository, CustomerEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity existingEntity = jpaRepository.findById(customer.getCustomerId()).orElse(null);
        if (existingEntity == null) {
            CustomerEntity newEntity = mapper.toEntity(customer);
            CustomerEntity savedEntity = jpaRepository.save(newEntity);
            return mapper.toDomain(savedEntity);
        } else {
            existingEntity = mapper.updateEntityFromDomain(customer, existingEntity);
            CustomerEntity savedEntity = jpaRepository.save(existingEntity);
            return mapper.toDomain(savedEntity);
        }
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        Optional<CustomerEntity> entityOptional = jpaRepository.findById(customerId);
        return entityOptional.map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID customerId) {
        return jpaRepository.existsById(customerId);
    }
}