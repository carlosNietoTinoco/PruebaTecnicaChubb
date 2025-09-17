package com.chubbTest.customer.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;

public class Customer {

    private UUID customerId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String numCTA;
    private Status status;
    private Country country;
    private LocalDateTime activateDate;
    private LocalDateTime inactivateDate;

    public Customer() {
    }

    public Customer(UUID customerId, String name, LocalDate birthDate, Gender gender, String numCTA, Status status, Country country, LocalDateTime activateDate, LocalDateTime inactivateDate) {
        this.customerId = customerId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.numCTA = numCTA;
        this.status = status;
        this.country = country;
        this.activateDate = activateDate;
        this.inactivateDate = inactivateDate;
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

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public LocalDateTime getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(LocalDateTime activateDate) {
        this.activateDate = activateDate;
    }

    public LocalDateTime getInactivateDate() {
        return inactivateDate;
    }

    public void setInactivateDate(LocalDateTime inactivateDate) {
        this.inactivateDate = inactivateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(birthDate, customer.birthDate) &&
                gender == customer.gender &&
                Objects.equals(numCTA, customer.numCTA) &&
                status == customer.status &&
                country == customer.country &&
                Objects.equals(activateDate, customer.activateDate) &&
                Objects.equals(inactivateDate, customer.inactivateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, birthDate, gender, numCTA, status, country, activateDate, inactivateDate);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='[MASKED]'" +
                ", birthDate='[MASKED]'" +
                ", gender=" + gender +
                ", numCTA='[MASKED]'" +
                ", status=" + status +
                ", country=" + country +
                ", isActive=" + isActive() +
                ", activateDate=" + activateDate +
                ", inactivateDate=" + inactivateDate +
                '}';
    }
}