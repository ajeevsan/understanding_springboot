package com.example.employeeapi.dto;

import com.example.employeeapi.entity.Employee;

/** Outgoing payload. Controls exactly which fields leave the API. */
public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String department,
        Double salary
) {
    public static EmployeeResponse from(Employee e) {
        return new EmployeeResponse(e.getId(), e.getName(), e.getEmail(), e.getDepartment(), e.getSalary());
    }
}
