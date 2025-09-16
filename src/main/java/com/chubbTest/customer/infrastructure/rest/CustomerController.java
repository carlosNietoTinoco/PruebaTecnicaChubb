package com.chubbTest.customer.infrastructure.rest;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubbTest.customer.application.service.CustomerService;
import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.infrastructure.rest.dto.CreateCustomerRequestDTO;
import com.chubbTest.customer.infrastructure.rest.dto.CustomerResponseDTO;
import com.chubbTest.customer.infrastructure.rest.dto.UpdateCustomerRequestDTO;
import com.chubbTest.customer.infrastructure.rest.mapper.CustomerDTOMapper;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDTOMapper mapper;

    public CustomerController(CustomerService customerService, CustomerDTOMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CreateCustomerRequestDTO request) {
        Customer customer = mapper.toDomain(request);
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerResponseDTO response = mapper.toResponseDTO(createdCustomer);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody UpdateCustomerRequestDTO request) {
        Customer partialCustomer = mapper.toDomain(request);
        Customer updatedCustomer = customerService.updateCustomer(customerId, partialCustomer);
        CustomerResponseDTO response = mapper.toResponseDTO(updatedCustomer);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{customerId}/deactivate")
    public ResponseEntity<CustomerResponseDTO> deactivateCustomer(@PathVariable UUID customerId) {
        Customer deactivatedCustomer = customerService.deactivateCustomer(customerId);
        CustomerResponseDTO response = mapper.toResponseDTO(deactivatedCustomer);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{customerId}/activate")
    public ResponseEntity<CustomerResponseDTO> activateCustomer(@PathVariable UUID customerId) {
        Customer activatedCustomer = customerService.activateCustomer(customerId);
        CustomerResponseDTO response = mapper.toResponseDTO(activatedCustomer);
        return ResponseEntity.ok(response);
    }
}