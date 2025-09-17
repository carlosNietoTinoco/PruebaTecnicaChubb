package com.chubbTest.customer.application.validation.strategy.impl;

import org.springframework.stereotype.Component;

import com.chubbTest.customer.application.validation.strategy.CountryValidationStrategy;
import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.exception.InvalidCustomerDataException;

@Component
public class ChileValidationStrategy implements CountryValidationStrategy {

    @Override
    public void validate(Customer customer) {
        if (customer.getNumCTA() != null && !customer.getNumCTA().startsWith("003")) {
            throw new InvalidCustomerDataException("Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'.");
        }
    }

    @Override
    public Country getCountry() {
        return Country.CHILE;
    }

}
