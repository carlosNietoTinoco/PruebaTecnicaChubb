package com.chubbTest.customer.infrastructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;
import com.chubbTest.customer.infrastructure.database.model.CountryEntity;
import com.chubbTest.customer.infrastructure.database.model.CustomerEntity;
import com.chubbTest.customer.infrastructure.database.model.GenderEntity;
import com.chubbTest.customer.infrastructure.database.model.StatusEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerEntityMapper {

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "numCTA", source = "numCTA")
    @Mapping(target = "activateDate", source = "activateDate")
    @Mapping(target = "inactivateDate", source = "inactivateDate")
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "country", ignore = true)
    CustomerEntity toEntity(Customer customer);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "numCTA", source = "numCTA")
    @Mapping(target = "activateDate", source = "activateDate")
    @Mapping(target = "inactivateDate", source = "inactivateDate")
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "country", ignore = true)
    Customer toDomain(CustomerEntity entity);

    default GenderEntity map(Gender gender) {
        if (gender == null) return null;
        GenderEntity entity = new GenderEntity();
        entity.setName(gender.name());
        return entity;
    }

    default Gender map(GenderEntity entity) {
        if (entity == null) return null;
        return Gender.valueOf(entity.getName());
    }

    default StatusEntity map(Status status) {
        if (status == null) return null;
        StatusEntity entity = new StatusEntity();
        entity.setName(status.name());
        return entity;
    }

    default Status map(StatusEntity entity) {
        if (entity == null) return null;
        return Status.valueOf(entity.getName());
    }

    default CountryEntity map(Country country) {
        if (country == null) return null;
        CountryEntity entity = new CountryEntity();
        entity.setName(country.name());
        return entity;
    }

    default Country map(CountryEntity entity) {
        if (entity == null) return null;
        return Country.valueOf(entity.getName());
    }
}