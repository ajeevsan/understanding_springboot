package com.example.employeeapi.repository;

import com.example.employeeapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Just an interface. Spring Data generates the implementation at runtime.
 * JpaRepository<Entity, IdType> already gives save, findById, findAll, deleteById, etc.
 * Derived queries: method names are parsed into SQL.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employee> findByDepartmentIgnoreCase(String department);
}
