package com.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientServerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_server_call() throws Exception {
        mockMvc.perform(get("/server-call")
                        .secure(true))
                .andExpect(status().isOk());
    }

    /**
     * @throws Exception
     * @implNote work required
     */
    @Test
    @Disabled
    void test_client_call() throws Exception {
        mockMvc.perform(get("/client-call")
                        .secure(true))
                .andExpect(status().isOk());
    }
}