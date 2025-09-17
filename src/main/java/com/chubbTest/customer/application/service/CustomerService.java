package com.chubbTest.customer.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chubbTest.customer.application.repository.CustomerRepository;
import com.chubbTest.customer.application.validation.factory.ValidationStrategyFactory;
import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Status;
import com.chubbTest.customer.domain.exception.CustomerNotFoundException;
import com.chubbTest.customer.domain.exception.CustomerStatusConflictException;
import com.chubbTest.customer.domain.exception.InvalidCustomerDataException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ValidationStrategyFactory validationStrategyFactory;

    public CustomerService(CustomerRepository customerRepository, 
                           ValidationStrategyFactory validationStrategyFactory) {
        this.customerRepository = customerRepository;
        this.validationStrategyFactory = validationStrategyFactory;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Iniciando proceso de creación de cliente.");
        validateCustomerData(customer);

        customer.setCustomerId(UUID.randomUUID());

        setCustomerStatusAndDates(customer, customer.getStatus(), LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Cliente creado exitosamente con ID: {}", savedCustomer.getCustomerId());
        return savedCustomer;
    }

    @Transactional
    public Customer updateCustomer(UUID customerId, Customer partialCustomer) {
        log.info("Iniciando proceso de actualización para el cliente con ID: {}", customerId);

        Customer customer = findCustomerById(customerId);
        if (customer.getStatus() != Status.ACTIVE) {
            log.error("Intento de modificación de cliente inactivo con ID: {}. Estado actual: {}", customerId, customer.getStatus());
            throw new CustomerStatusConflictException("La operación falló: El cliente con ID " + customerId + " no está activo y no puede ser modificado.");
        }

        validateCustomerDataForUpdate(customer, partialCustomer);

        if (partialCustomer.getName() != null) {
            customer.setName(partialCustomer.getName());
        }
        if (partialCustomer.getBirthDate() != null) {
            customer.setBirthDate(partialCustomer.getBirthDate());
        }
        if (partialCustomer.getGender() != null) {
            customer.setGender(partialCustomer.getGender());
        }
        if (partialCustomer.getNumCTA() != null) {
            customer.setNumCTA(partialCustomer.getNumCTA());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Cliente con ID: {} actualizado exitosamente.", updatedCustomer.getCustomerId());
        return updatedCustomer;
    }

    @Transactional
    public Customer deactivateCustomer(UUID customerId) {
        log.info("Iniciando desactivación del cliente con ID: {}", customerId);

        Customer customer = findCustomerById(customerId);

        if (customer.getStatus() != Status.ACTIVE) {
            log.warn("Intento de desactivar un cliente que no está activo. ID: {}, Estado: {}", customerId, customer.getStatus());
            throw new CustomerStatusConflictException("El cliente con ID " + customerId + " ya se encuentra inactivo.");
        }

        setCustomerStatusAndDates(customer, Status.INACTIVE, LocalDateTime.now());

        Customer deactivatedCustomer = customerRepository.save(customer);
        log.info("Cliente con ID: {} desactivado exitosamente.", deactivatedCustomer.getCustomerId());
        return deactivatedCustomer;
    }

    @Transactional
    public Customer activateCustomer(UUID customerId) {
        log.info("Iniciando activación del cliente con ID: {}", customerId);
        Customer customer = findCustomerById(customerId);
        if (customer.getStatus() != Status.INACTIVE) {
            log.warn("Intento de activar un cliente que no está inactivo. ID: {}, Estado: {}", customerId, customer.getStatus());
            throw new CustomerStatusConflictException("El cliente con ID " + customerId + " ya se encuentra activo.");
        }

        setCustomerStatusAndDates(customer, Status.ACTIVE, LocalDateTime.now());

        Customer activatedCustomer = customerRepository.save(customer);
        log.info("Cliente con ID: {} activado exitosamente.", activatedCustomer.getCustomerId());
        return activatedCustomer;
    }

    private void validateCustomerData(Customer customer) {
        validateName(customer);
        validateBirthDate(customer);
        validateNumCTA(customer);
        validateCountrySpecificRules(customer);
    }

    private void validateCustomerDataForUpdate(Customer existingCustomer, Customer partialCustomer) {
        validateName(partialCustomer);
        validateBirthDate(partialCustomer);
        validateNumCTA(partialCustomer);

        Country countryToValidate = partialCustomer.getCountry() != null ? partialCustomer.getCountry() : existingCustomer.getCountry();
        String numCTAToValidate = partialCustomer.getNumCTA() != null ? partialCustomer.getNumCTA() : existingCustomer.getNumCTA();

        if (countryToValidate == Country.CHILE && numCTAToValidate != null && !numCTAToValidate.startsWith("003")) {
            throw new InvalidCustomerDataException("Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'.");
        }
    }

    private void validateName(Customer customer) {
        if (customer.getName() != null && customer.getName().length() > 255) {
            throw new InvalidCustomerDataException("Error de validación: El nombre no puede exceder 255 caracteres.");
        }
    }

    private void validateBirthDate(Customer customer) {
        if (customer.getBirthDate() != null && customer.getBirthDate().isBefore(LocalDate.of(1990, 1, 1))) {
            throw new InvalidCustomerDataException("Error de validación: La fecha de nacimiento 'birthDate' debe ser igual o posterior a 1990-01-01.");
        }
    }

    private void validateNumCTA(Customer customer) {
        if (customer.getNumCTA() != null) {
            if (customer.getNumCTA().length() < 12 || customer.getNumCTA().length() > 15) {
                throw new InvalidCustomerDataException("Error de validación: El 'numCTA' debe tener entre 12 y 15 caracteres.");
            }
            if (!customer.getNumCTA().matches("\\d+")) {
                throw new InvalidCustomerDataException("Error de validación: El 'numCTA' debe contener solo números.");
            }
        }
    }

    private void validateCountrySpecificRules(Customer customer) {
        validationStrategyFactory.getStrategy(customer.getCountry())
                .ifPresent(strategy -> strategy.validate(customer));
    }

    private void setCustomerStatusAndDates(Customer customer, Status status, LocalDateTime timestamp) {
        customer.setStatus(status);

        if (status == Status.ACTIVE) {
            customer.setActivateDate(timestamp);
            customer.setInactivateDate(null);
        } else {
            customer.setInactivateDate(timestamp);
            customer.setActivateDate(null);
        }
    }

    private Customer findCustomerById(UUID customerId) {
        log.debug("Buscando cliente con ID: {}", customerId);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            log.error("No se encontró el cliente con ID: {}", customerId);
            throw new CustomerNotFoundException("No se encontró el cliente con ID: " + customerId);
        }
        log.debug("Cliente con ID: {} encontrado.", customerId);
        return optionalCustomer.get();
    }
}