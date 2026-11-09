package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createThenFetchEmployee() throws Exception {
        String body = """
                {"name":"Asha","email":"asha@example.com","department":"Engineering","salary":90000}
                """;

        mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Asha"));

        mockMvc.perform(get("/api/employees").param("department", "engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("asha@example.com"));
    }

    @Test
    void invalidPayloadReturns400() throws Exception {
        String body = """
                {"name":"","email":"not-an-email"}
                """;

        mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mockMvc.perform(get("/api/employees/9999"))
                .andExpect(status().isNotFound());
    }
}
