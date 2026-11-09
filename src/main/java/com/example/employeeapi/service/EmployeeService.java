package com.example.employeeapi.service;

import com.example.employeeapi.dto.EmployeeRequest;
import com.example.employeeapi.dto.EmployeeResponse;
import com.example.employeeapi.entity.Employee;
import com.example.employeeapi.exception.ResourceNotFoundException;
import com.example.employeeapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic lives here. @Service registers it as a Spring bean
 * (Nest equivalent: @Injectable provider).
 */
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    // Constructor injection: preferred over @Autowired on fields (easier to test, fields can be final).
    // With a single constructor, @Autowired is not needed.
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll(String department) {
        List<Employee> employees = (department == null || department.isBlank())
                ? repository.findAll()
                : repository.findByDepartmentIgnoreCase(department);
        return employees.stream().map(EmployeeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return EmployeeResponse.from(getOrThrow(id));
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Employee with email " + request.email() + " already exists");
        }
        Employee saved = repository.save(
                new Employee(request.name(), request.email(), request.department(), request.salary()));
        return EmployeeResponse.from(saved);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getOrThrow(id);
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());
        // No explicit save() needed: inside a transaction, the managed entity is
        // flushed automatically on commit (dirty checking).
        return EmployeeResponse.from(employee);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Employee getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }
}
