package com.chubbTest.customer.application.repository;

import java.util.Optional;
import java.util.UUID;

import com.chubbTest.customer.domain.Customer;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID customerId);

    boolean existsById(UUID customerId);
}