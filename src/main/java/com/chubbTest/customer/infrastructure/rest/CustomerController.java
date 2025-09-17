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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDTOMapper mapper;

    public CustomerController(CustomerService customerService, CustomerDTOMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Crea un nuevo cliente en el sistema. Valida los datos de entrada y aplica reglas de negocio específicas.", notes = "Requiere un JWT válido con los permisos o roles adecuados (`SCOPE_customers:write`).")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Cliente creado con éxito. Devuelve el DTO del cliente recién creado, incluyendo el ID generado."),
        @ApiResponse(code = 400, message = "Los datos proporcionados en el cuerpo de la solicitud son inválidos."),
        @ApiResponse(code = 401, message = "Token JWT inválido."),
        @ApiResponse(code = 403, message = "Permisos insuficientes."),
        @ApiResponse(code = 500, message = "Error interno del servidor.")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CreateCustomerRequestDTO request) {
        Customer customer = mapper.toDomain(request);
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerResponseDTO response = mapper.toResponseDTO(createdCustomer);
        return ResponseEntity.status(201).body(response);
    }

    @ApiOperation(value = "Actualiza los datos de un cliente existente (`name`, `birthDate`, `gender`, `numCTA`). Una precondición clave es que el cliente debe tener un estado `ACTIVE` para poder ser actualizado.", notes = "Requiere un JWT válido con permisos de actualización (ej. `SCOPE_customers:update`).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cliente actualizado con éxito. Devuelve el DTO del cliente con los datos actualizados."),
        @ApiResponse(code = 400, message = "Los datos en el cuerpo de la solicitud son inválidos (mismas validaciones que en la creación)."),
        @ApiResponse(code = 401, message = "Token JWT inválido."),
        @ApiResponse(code = 403, message = "Permisos insuficientes."),
        @ApiResponse(code = 404, message = "No se encontró ningún cliente con el `customerId` proporcionado."),
        @ApiResponse(code = 409, message = "La operación no puede ser completada porque el cliente no está en estado `ACTIVE`."),
        @ApiResponse(code = 500, message = "Error interno del servidor.")
    })
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody UpdateCustomerRequestDTO request) {
        Customer partialCustomer = mapper.toDomain(request);
        Customer updatedCustomer = customerService.updateCustomer(customerId, partialCustomer);
        CustomerResponseDTO response = mapper.toResponseDTO(updatedCustomer);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Cambia el estado de un cliente a `INACTIVE` y registra la fecha de desactivación (`inactivateDate`). Solo se puede desactivar un cliente que se encuentre actualmente `ACTIVE`.", notes = "Requiere JWT con permisos específicos (ej. `SCOPE_customers:deactivate`).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cliente desactivado con éxito. Devuelve el DTO del cliente actualizado."),
        @ApiResponse(code = 401, message = "Token JWT inválido."),
        @ApiResponse(code = 403, message = "Permisos insuficientes."),
        @ApiResponse(code = 404, message = "El cliente con el `customerId` no existe."),
        @ApiResponse(code = 409, message = "El cliente ya se encuentra en estado `INACTIVE`."),
        @ApiResponse(code = 500, message = "Error interno del servidor.")
    })
    @PatchMapping("/{customerId}/deactivate")
    public ResponseEntity<CustomerResponseDTO> deactivateCustomer(@PathVariable UUID customerId) {
        Customer deactivatedCustomer = customerService.deactivateCustomer(customerId);
        CustomerResponseDTO response = mapper.toResponseDTO(deactivatedCustomer);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Cambia el estado de un cliente a `ACTIVE` y registra la fecha de activación (`activateDate`). Solo se puede activar un cliente que se encuentre actualmente `INACTIVE`.", notes = "Requiere JWT con permisos específicos (ej. `SCOPE_customers:activate`).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cliente activado con éxito. Devuelve el DTO del cliente actualizado."),
        @ApiResponse(code = 401, message = "Token JWT inválido."),
        @ApiResponse(code = 403, message = "Permisos insuficientes."),
        @ApiResponse(code = 404, message = "El cliente con el `customerId` no existe."),
        @ApiResponse(code = 409, message = "El cliente ya se encuentra en estado `ACTIVE`."),
        @ApiResponse(code = 500, message = "Error interno del servidor.")
    })
    @PatchMapping("/{customerId}/activate")
    public ResponseEntity<CustomerResponseDTO> activateCustomer(@PathVariable UUID customerId) {
        Customer activatedCustomer = customerService.activateCustomer(customerId);
        CustomerResponseDTO response = mapper.toResponseDTO(activatedCustomer);
        return ResponseEntity.ok(response);
    }
}