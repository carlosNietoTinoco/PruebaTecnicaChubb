package com.chubbTest.customer.infrastructure.rest.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;

public class CustomerResponseDTO {

    private UUID customerId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String numCTA;
    private Status status;
    private Country country;

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(UUID customerId, String name, LocalDate birthDate, Gender gender, String numCTA, Status status, Country country) {
        this.customerId = customerId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.numCTA = numCTA;
        this.status = status;
        this.country = country;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNumCTA() {
        return numCTA;
    }

    public void setNumCTA(String numCTA) {
        this.numCTA = numCTA;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}