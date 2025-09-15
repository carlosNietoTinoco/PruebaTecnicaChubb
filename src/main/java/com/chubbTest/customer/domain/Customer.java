package com.chubbTest.customer.domain;

import com.chubbTest.customer.domain.enums.Country;
import com.chubbTest.customer.domain.enums.Gender;
import com.chubbTest.customer.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private UUID customerId;

    private String name;

    private LocalDate birthDate;

    private Gender gender;

    private String numCTA;

    private Status status;

    private Country country;

    private boolean isActive;

    private LocalDateTime activateDate;

    private LocalDateTime inactivateDate;
}