package com.example.employeeapi.controller;

import com.example.employeeapi.dto.EmployeeRequest;
import com.example.employeeapi.dto.EmployeeResponse;
import com.example.employeeapi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * HTTP layer only: parse request, call service, shape response.
 * @RestController = @Controller + @ResponseBody (return values are serialized to JSON).
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // GET /api/employees or /api/employees?department=Engineering
    @GetMapping
    public List<EmployeeResponse> getAll(@RequestParam(required = false) String department) {
        return service.findAll(department);
    }

    // GET /api/employees/1
    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // POST /api/employees  -> 201 Created + Location header
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/employees/" + created.id()))
                .body(created);
    }

    // PUT /api/employees/1
    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return service.update(id, request);
    }

    // DELETE /api/employees/1  -> 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
