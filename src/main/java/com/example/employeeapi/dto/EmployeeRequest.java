package com.example.employeeapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Incoming payload. A record is an immutable data carrier (Java 16+),
 * the closest thing to a Nest DTO class with class-validator decorators.
 */
public record EmployeeRequest(
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "email is required") @Email(message = "email must be valid") String email,
        String department,
        @Positive(message = "salary must be positive") Double salary
) {
}
