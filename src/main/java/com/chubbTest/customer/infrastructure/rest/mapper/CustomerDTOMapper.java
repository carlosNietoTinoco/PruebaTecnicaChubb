package com.chubbTest.customer.infrastructure.rest.mapper;

import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.infrastructure.rest.dto.CreateCustomerRequestDTO;
import com.chubbTest.customer.infrastructure.rest.dto.CustomerResponseDTO;
import com.chubbTest.customer.infrastructure.rest.dto.UpdateCustomerRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerDTOMapper {

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "activateDate", ignore = true)
    @Mapping(target = "inactivateDate", ignore = true)
    Customer toDomain(CreateCustomerRequestDTO dto);

    Customer toDomain(UpdateCustomerRequestDTO dto);

    CustomerResponseDTO toResponseDTO(Customer customer);
}