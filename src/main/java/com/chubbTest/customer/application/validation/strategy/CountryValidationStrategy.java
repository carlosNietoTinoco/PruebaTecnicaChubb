package com.chubbTest.customer.application.validation.strategy;

import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.domain.enums.Country;

public interface CountryValidationStrategy {

    void validate(Customer customer);
    Country getCountry();

}