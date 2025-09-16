package com.chubbTest.customer.infrastructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "numCTA", source = "numCTA")
    @Mapping(target = "activateDate", source = "activateDate")
    @Mapping(target = "inactivateDate", source = "inactivateDate")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "country", source = "country")
    CustomerEntity toEntity(Customer customer);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "numCTA", source = "numCTA")
    @Mapping(target = "activateDate", source = "activateDate")
    @Mapping(target = "inactivateDate", source = "inactivateDate")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "country", source = "country")
    Customer toDomain(CustomerEntity entity);

    @Mapping(target = "customerId", ignore = true)
    CustomerEntity updateEntityFromDomain(Customer customer, @MappingTarget CustomerEntity entity);

    default GenderEntity map(Gender gender) {
        if (gender == null) return null;
        GenderEntity entity = new GenderEntity();
        entity.setId(gender == Gender.MALE ? 1 : 2);
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
        entity.setId(status == Status.ACTIVE ? 1 : 2);
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
        int id = switch (country) {
            case COLOMBIA -> 1;
            case CHILE -> 2;
            case ARGENTINA -> 3;
            case BRAZIL -> 4;
            case ECUADOR -> 5;
            case MEXICO -> 6;
            case PANAMA -> 7;
        };
        entity.setId(id);
        entity.setName(country.name());
        return entity;
    }

    default Country map(CountryEntity entity) {
        if (entity == null) return null;
        return Country.valueOf(entity.getName());
    }
}