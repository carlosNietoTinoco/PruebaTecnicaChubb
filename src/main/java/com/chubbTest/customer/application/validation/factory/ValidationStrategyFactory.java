package com.chubbTest.customer.application.validation.factory;

import java.util.Optional;

import com.chubbTest.customer.application.validation.strategy.CountryValidationStrategy;
import com.chubbTest.customer.domain.enums.Country;

public interface ValidationStrategyFactory {

    Optional<CountryValidationStrategy> getStrategy(Country country);

}