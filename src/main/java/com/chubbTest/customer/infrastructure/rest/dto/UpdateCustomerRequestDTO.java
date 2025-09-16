package com.chubbTest.customer.infrastructure.rest.dto;

import java.time.LocalDate;

import com.chubbTest.customer.domain.enums.Gender;

public class UpdateCustomerRequestDTO {

    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String numCTA;

    public UpdateCustomerRequestDTO() {
    }

    public UpdateCustomerRequestDTO(String name, LocalDate birthDate, Gender gender, String numCTA) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.numCTA = numCTA;
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
}