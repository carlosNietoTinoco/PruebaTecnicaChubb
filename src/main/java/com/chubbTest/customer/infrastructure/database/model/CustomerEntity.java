package com.chubbTest.customer.infrastructure.database.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer", indexes = {
    @Index(name = "idx_customer_status", columnList = "status_id"),
    @Index(name = "idx_customer_country", columnList = "country_id")
})
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id", columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gender_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_gender"))
    private GenderEntity gender;

    @Column(name = "num_cta", nullable = false, length = 15, unique = true)
    private String numCTA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_status"))
    private StatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_country"))
    private CountryEntity country;

    @Column(name = "activate_date")
    private LocalDateTime activateDate;

    @Column(name = "inactivate_date")
    private LocalDateTime inactivateDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public CustomerEntity() {
    }

    public CustomerEntity(UUID customerId, String name, LocalDate birthDate, GenderEntity gender,
                         String numCTA, StatusEntity status, CountryEntity country,
                         LocalDateTime activateDate, LocalDateTime inactivateDate) {
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

    public GenderEntity getGender() {
        return gender;
    }

    public void setGender(GenderEntity gender) {
        this.gender = gender;
    }

    public String getNumCTA() {
        return numCTA;
    }

    public void setNumCTA(String numCTA) {
        this.numCTA = numCTA;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return customerId != null && customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + (gender != null ? gender.getName() : null) +
                ", numCTA='" + numCTA + '\'' +
                ", status=" + (status != null ? status.getName() : null) +
                ", country=" + (country != null ? country.getName() : null) +
                ", activateDate=" + activateDate +
                ", inactivateDate=" + inactivateDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}