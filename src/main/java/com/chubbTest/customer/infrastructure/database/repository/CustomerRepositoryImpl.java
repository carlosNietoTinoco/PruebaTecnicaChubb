package com.chubbTest.customer.infrastructure.database.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.chubbTest.customer.application.repository.CustomerRepository;
import com.chubbTest.customer.domain.Customer;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<UUID, Customer> customerStore = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        customerStore.put(customer.getCustomerId(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.ofNullable(customerStore.get(customerId));
    }

    @Override
    public boolean existsById(UUID customerId) {
        return customerStore.containsKey(customerId);
    }
}