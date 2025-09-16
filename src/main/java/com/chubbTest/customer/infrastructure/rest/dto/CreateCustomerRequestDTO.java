package com.chubbTest.customer.infrastructure.rest.dto;

import java.time.LocalDate;

import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;

public class CreateCustomerRequestDTO {

    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String numCTA;
    private Country country;
    private Status status;

    public CreateCustomerRequestDTO() {
    }

    public CreateCustomerRequestDTO(String name, LocalDate birthDate, Gender gender, String numCTA, Country country, Status status) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.numCTA = numCTA;
        this.country = country;
        this.status = status;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}