package com.chubbTest.customer.application.validation.factory.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.chubbTest.customer.application.validation.factory.ValidationStrategyFactory;
import com.chubbTest.customer.application.validation.strategy.CountryValidationStrategy;
import com.chubbTest.customer.domain.enums.Country;

@Component
public class ValidationStrategyFactoryImpl implements ValidationStrategyFactory {

    private final Map<Country, CountryValidationStrategy> strategies;

    public ValidationStrategyFactoryImpl(List<CountryValidationStrategy> strategyList) {
        strategies = new EnumMap<>(Country.class);
        strategyList.forEach(strategy -> strategies.put(strategy.getCountry(), strategy));
    }

    @Override
    public Optional<CountryValidationStrategy> getStrategy(Country country) {
        return Optional.ofNullable(strategies.get(country));
    }

}
