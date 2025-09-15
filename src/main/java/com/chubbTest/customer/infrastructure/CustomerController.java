package com.chubbTest.customer.infrastructure;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @PostMapping
    public void createCustomer(@RequestBody Object request) {
        throw new RuntimeException("Not implemented yet");
    }

    @PatchMapping("/{customerId}")
    public void updateCustomer(@PathVariable String customerId, @RequestBody Object request) {
        throw new RuntimeException("Not implemented yet");
    }

    @PatchMapping("/{customerId}/deactivate")
    public void deactivateCustomer(@PathVariable String customerId) {
        throw new RuntimeException("Not implemented yet");
    }

    @PatchMapping("/{customerId}/activate")
    public void activateCustomer(@PathVariable String customerId) {
        throw new RuntimeException("Not implemented yet");
    }
}