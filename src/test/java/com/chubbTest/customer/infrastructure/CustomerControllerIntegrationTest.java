package com.chubbTest.customer.infrastructure;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chubbTest.customer.TestSecurityConfig;
import com.chubbTest.customer.infrastructure.config.spring.CustomerApplication;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@Transactional
@Sql(scripts = "classpath:test-data-customer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CustomerControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;


    // 1.1 Create Customer Tests

    @Test
    @WithMockUser(authorities = "SCOPE_customers:write")
    void testCreateCustomer_Success_Colombia_Active() throws Exception {
        String requestJson = """
            {
              "name": "Juan Pérez",
              "birthDate": "1995-08-20",
              "gender": "MALE",
              "numCTA": "123456789012",
              "country": "COLOMBIA",
              "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:write")
    void testCreateCustomer_Success_Chile_Inactive() throws Exception {
        String requestJson = """
            {
              "name": "Ana Silva",
              "birthDate": "2001-02-10",
              "gender": "FEMALE",
              "numCTA": "003987654321",
              "country": "CHILE",
              "status": "INACTIVE"
            }
            """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:write")
    void testCreateCustomer_Fail_InvalidStatus() throws Exception {
        String requestJson = """
            {
              "name": "Usuario de Prueba",
              "birthDate": "1999-01-01",
              "gender": "MALE",
              "numCTA": "12345",
              "country": "COLOMBIA",
              "status": "PENDING"
            }
            """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("PENDING")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:write")
    void testCreateCustomer_Fail_BirthDateOutOfRange() throws Exception {
        String requestJson = """
            {
              "name": "Pedro",
              "birthDate": "1989-12-31",
              "gender": "MALE",
              "numCTA": "987654321",
              "country": "COLOMBIA",
              "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("1990-01-01")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:write")
    void testCreateCustomer_Fail_NumCTA_Chile_Invalid() throws Exception {
        String requestJson = """
            {
              "name": "Carlos Rojas",
              "birthDate": "1995-11-23",
              "gender": "MALE",
              "numCTA": "111987654321",
              "country": "CHILE",
              "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("003")));
    }

    // 1.2 Update Customer Tests

    @Test
    @WithMockUser(authorities = "SCOPE_customers:update")
    void testUpdateCustomer_Success() throws Exception {
        String requestJson = """
            {
              "name": "Juan Pérez Actualizado",
              "numCTA": "987654321098"
            }
            """;

        mockMvc.perform(patch("/api/v1/customers/d4a7f8b1-9c3e-4a5d-b1e2-f3c4d5a6b7c8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:update")
    void testUpdateCustomer_Fail_Inactive() throws Exception {
        String requestJson = """
            {
              "name": "Juan Pérez Intento de actualización"
            }
            """;

        String customerId = "f1a2b3c4-d5e6-f789-0123-456789abcdef";

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("no está activo")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:update")
    void testUpdateCustomer_Fail_NotFound() throws Exception {
        String requestJson = """
            {
              "name": "Test"
            }
            """;

        String nonExistentId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(patch("/api/v1/customers/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound());
    }

    // 1.3 Deactivate Customer Tests

    @Test
    @WithMockUser(authorities = "SCOPE_customers:deactivate")
    void testDeactivateCustomer_Success() throws Exception {
        mockMvc.perform(patch("/api/v1/customers/{id}/deactivate", "e1f2a3b4-c5d6-7890-abcd-ef1234567890"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:deactivate")
    void testDeactivateCustomer_Fail_AlreadyInactive() throws Exception {
        mockMvc.perform(patch("/api/v1/customers/{id}/deactivate", "b2c3d4e5-f678-9012-3456-7890abcdef12"))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("ya se encuentra inactivo")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:deactivate")
    void testDeactivateCustomer_Fail_NotFound() throws Exception {

        String nonExistentId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(patch("/api/v1/customers/{id}/deactivate", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No se encontró el cliente")));
    }

    // 1.4 Activate Customer Tests

    @Test
    @WithMockUser(authorities = "SCOPE_customers:activate")
    void testActivateCustomer_Success() throws Exception {
        mockMvc.perform(patch("/api/v1/customers/{id}/activate", "a1b2c3d4-e5f6-7890-1234-567890abcdef"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:activate")
    void testActivateCustomer_Fail_AlreadyActive() throws Exception {
        mockMvc.perform(patch("/api/v1/customers/{id}/activate", "c3d4e5f6-7890-1234-5678-90abcdef1234"))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("ya se encuentra activo")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_customers:activate")
    void testActivateCustomer_Fail_NotFound() throws Exception {

        String nonExistentId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(patch("/api/v1/customers/{id}/activate", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No se encontró el cliente")));
    }
}