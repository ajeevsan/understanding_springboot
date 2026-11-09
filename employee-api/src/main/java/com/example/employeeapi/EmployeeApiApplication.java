package com.example.employeeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 * Component scan starts from THIS package, so all other packages must live under
 * com.example.employeeapi or Spring won't find your beans.
 */
@SpringBootApplication
public class EmployeeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApiApplication.class, args);
    }
}
