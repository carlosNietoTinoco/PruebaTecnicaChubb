package com.chubbTest.customer.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import com.chubbTest.customer.TestSecurityConfig;
import com.chubbTest.customer.infrastructure.config.spring.CustomerApplication;

@SpringBootTest(classes = CustomerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // 1.1 Create Customer Tests

    @Test
    @WithMockUser
    public void testCreateCustomer_Success_Colombia_Active() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Assert body contains expected fields
    }

    @Test
    @WithMockUser
    public void testCreateCustomer_Success_Chile_Inactive() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @WithMockUser
    public void testCreateCustomer_Fail_InvalidStatus() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("PENDING");
    }

    @Test
    @WithMockUser
    public void testCreateCustomer_Fail_BirthDateOutOfRange() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("1990-01-01");
    }

    @Test
    @WithMockUser
    public void testCreateCustomer_Fail_NumCTA_Chile_Invalid() {
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers", HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("003");
    }

    // 1.2 Update Customer Tests

    @Test
    @WithMockUser
    public void testUpdateCustomer_Success() {
        String requestJson = """
            {
              "name": "Juan Pérez Actualizado",
              "numCTA": "987654321098"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/d4a7f8b1-9c3e-4a5d-b1e2-f3c4d5a6b7c8", HttpMethod.PATCH, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser
    public void testUpdateCustomer_Fail_Inactive() {
        String requestJson = """
            {
              "name": "Juan Pérez Intento de actualización"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/{id}", HttpMethod.PATCH, entity, String.class, "id_cliente_inactivo");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("no está activo");
    }

    @Test
    @WithMockUser
    public void testUpdateCustomer_Fail_NotFound() {
        String requestJson = """
            {
              "name": "Test"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/id-inexistente", HttpMethod.PATCH, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // 1.3 Deactivate Customer Tests

    @Test
    @WithMockUser
    public void testDeactivateCustomer_Success() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/{id}/deactivate", HttpMethod.PATCH, null, String.class, "id_cliente_activo");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser
    public void testDeactivateCustomer_Fail_AlreadyInactive() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/{id}/deactivate", HttpMethod.PATCH, null, String.class, "id_cliente_ya_inactivo");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("ya se encuentra inactivo");
    }

    @Test
    @WithMockUser
    public void testDeactivateCustomer_Fail_NotFound() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/id-inexistente/deactivate", HttpMethod.PATCH, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("No se encontró el cliente");
    }

    // 1.4 Activate Customer Tests

    @Test
    @WithMockUser
    public void testActivateCustomer_Success() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/{id}/activate", HttpMethod.PATCH, null, String.class, "id_cliente_inactivo");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockUser
    public void testActivateCustomer_Fail_AlreadyActive() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/{id}/activate", HttpMethod.PATCH, null, String.class, "id_cliente_ya_activo");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("ya se encuentra activo");
    }

    @Test
    @WithMockUser
    public void testActivateCustomer_Fail_NotFound() {
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/customers/id-inexistente/activate", HttpMethod.PATCH, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("No se encontró el cliente");
    }
}