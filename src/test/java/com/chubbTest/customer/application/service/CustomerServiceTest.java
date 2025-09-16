package com.chubbTest.customer.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.chubbTest.customer.application.repository.CustomerRepository;
import com.chubbTest.customer.domain.Customer;
import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;
import com.chubbTest.customer.domain.exception.CustomerNotFoundException;
import com.chubbTest.customer.domain.exception.InvalidCustomerDataException;

class CustomerServiceTest {

    // Success cases

    @Test
    void createCustomer_shouldSetStatusActiveAndActivateDate() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("123456789012");
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        when(repositoryMock.save(customer)).thenReturn(customer);

        // Act
        Customer result = service.createCustomer(customer);

        // Assert
        assertThat(result.getCustomerId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(result.getActivateDate()).isNotNull();
        assertThat(result.getName()).isEqualTo("Carlos");

        verify(repositoryMock).save(customer);
    }

    @Test
    void updateCustomer_shouldUpdateFieldsSuccessfully() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setName("New Name");
        partialCustomer.setBirthDate(LocalDate.of(1995, 5, 15));

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(repositoryMock.save(existingCustomer)).thenReturn(existingCustomer);

        // Act
        Customer result = service.updateCustomer(customerId, partialCustomer);

        // Assert
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 15));
        assertThat(result.getStatus()).isEqualTo(Status.ACTIVE);

        verify(repositoryMock).findById(customerId);
        verify(repositoryMock).save(existingCustomer);
    }

    @Test
    void deactivateCustomer_shouldDeactivateActiveCustomer() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Test", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(repositoryMock.save(existingCustomer)).thenReturn(existingCustomer);

        // Act
        Customer result = service.deactivateCustomer(customerId);

        // Assert
        assertThat(result.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(result.getInactivateDate()).isNotNull();

        verify(repositoryMock).findById(customerId);
        verify(repositoryMock).save(existingCustomer);
    }

    @Test
    void activateCustomer_shouldActivateInactiveCustomer() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Test", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.INACTIVE, Country.COLOMBIA, null, LocalDateTime.now());

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(repositoryMock.save(existingCustomer)).thenReturn(existingCustomer);

        // Act
        Customer result = service.activateCustomer(customerId);

        // Assert
        assertThat(result.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(result.getActivateDate()).isNotNull();

        verify(repositoryMock).findById(customerId);
        verify(repositoryMock).save(existingCustomer);
    }

    // Error cases - Name validations

    @Test
    void createCustomer_withNameLongerThan255Characters_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("A".repeat(256)); // 256 characters
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("123456789012");
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El nombre no puede exceder 255 caracteres.");
    }

    @Test
    void updateCustomer_withNameLongerThan255Characters_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setName("A".repeat(256)); // 256 characters

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El nombre no puede exceder 255 caracteres.");
    }

    // Error cases - Birthday validations

    @Test
    void createCustomer_withBirthdayBefore1900_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1889, 12, 31)); // Before 1900
        customer.setGender(Gender.MALE);
        customer.setNumCTA("123456789012");
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: La fecha de nacimiento 'birthDate' debe ser igual o posterior a 1990-01-01.");
    }

    @Test
    void updateCustomer_withBirthdayBefore1900_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setBirthDate(LocalDate.of(1889, 12, 31)); // Before 1900

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: La fecha de nacimiento 'birthDate' debe ser igual o posterior a 1990-01-01.");
    }

    // Error cases - NumCTA validations

    @Test
    void createCustomer_withNumCTALessThan12Digits_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("12345678901"); // 11 digits
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe tener entre 12 y 15 caracteres.");
    }

    @Test
    void createCustomer_withNumCTAMoreThan15Digits_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("1234567890123456"); // 16 digits
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe tener entre 12 y 15 caracteres.");
    }

    @Test
    void createCustomer_withNumCTAContainingLetters_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("12345678901A"); // Contains letter
        customer.setCountry(Country.COLOMBIA);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe contener solo números.");
    }

    @Test
    void updateCustomer_withNumCTALessThan12Digits_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setNumCTA("12345678901"); // 11 digits

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe tener entre 12 y 15 caracteres.");
    }

    @Test
    void updateCustomer_withNumCTAMoreThan15Digits_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setNumCTA("1234567890123456"); // 16 digits

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe tener entre 12 y 15 caracteres.");
    }

    @Test
    void updateCustomer_withNumCTAContainingLetters_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setNumCTA("12345678901A"); // Contains letter

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: El 'numCTA' debe contener solo números.");
    }

    // Error cases - Country-specific validations

    @Test
    void createCustomer_withChileCountryAndNumCTANotStartingWith003_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        Customer customer = new Customer();
        customer.setName("Carlos");
        customer.setBirthDate(LocalDate.of(1990, 5, 15));
        customer.setGender(Gender.MALE);
        customer.setNumCTA("123456789012"); // Valid length but doesn't start with 003
        customer.setCountry(Country.CHILE);
        customer.setStatus(Status.ACTIVE);

        // Act & Assert
        assertThatThrownBy(() -> service.createCustomer(customer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'.");
    }

    @Test
    void updateCustomer_changingToChileCountryWithNumCTANotStartingWith003_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "123456789012", Status.ACTIVE, Country.COLOMBIA, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setCountry(Country.CHILE); // Changing to Chile
        partialCustomer.setNumCTA("123456789012"); // Doesn't start with 003

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'.");
    }

    @Test
    void updateCustomer_alreadyChileCountryUpdatingNumCTANotStartingWith003_shouldThrowInvalidDataException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "Old Name", LocalDate.of(1990, 1, 1),
                Gender.MALE, "003456789012", Status.ACTIVE, Country.CHILE, LocalDateTime.now(), null);

        Customer partialCustomer = new Customer();
        partialCustomer.setNumCTA("123456789012"); // Doesn't start with 003

        when(repositoryMock.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(customerId, partialCustomer))
                .isInstanceOf(InvalidCustomerDataException.class)
                .hasMessage("Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'.");
    }

    // Error cases - Non-existent customer IDs

    @Test
    void updateCustomer_withNonExistentId_shouldThrowCustomerNotFoundException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID nonExistentId = UUID.randomUUID();
        Customer partialCustomer = new Customer();
        partialCustomer.setName("New Name");

        when(repositoryMock.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.updateCustomer(nonExistentId, partialCustomer))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No se encontró el cliente con ID: " + nonExistentId);
    }

    @Test
    void activateCustomer_withNonExistentId_shouldThrowCustomerNotFoundException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID nonExistentId = UUID.randomUUID();

        when(repositoryMock.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.activateCustomer(nonExistentId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No se encontró el cliente con ID: " + nonExistentId);
    }

    @Test
    void deactivateCustomer_withNonExistentId_shouldThrowCustomerNotFoundException() {
        // Arrange
        CustomerRepository repositoryMock = mock(CustomerRepository.class);
        CustomerService service = new CustomerService(repositoryMock);

        UUID nonExistentId = UUID.randomUUID();

        when(repositoryMock.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.deactivateCustomer(nonExistentId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("No se encontró el cliente con ID: " + nonExistentId);
    }
}