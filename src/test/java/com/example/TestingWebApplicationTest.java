package com.example;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Submit")));
    }

    @Test
    public void shouldReturnAccessGranted() throws Exception {
        this.mockMvc.perform(post("/").param("login", "ad").param("pass", "123")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, ad")));
        this.mockMvc.perform(post("/").param("login", "asd").param("pass", "234")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, asd")));
    }

    @Test
    public void shouldReturnAccessDenied() throws Exception {
        this.mockMvc.perform(post("/").param("login", "ad").param("pass", "234")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong pass, ad")));
        this.mockMvc.perform(post("/").param("login", "asd").param("pass", "123")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong pass, asd")));
    }

    @Test void connectionIsOk() throws Exception {
        this.mockMvc.perform(post("/").param("login", "asd").param("pass", "123")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("DB Error"))));
    }

    @Test void logOut() throws Exception {
        this.mockMvc.perform(get("/logout")).
                andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out")));
    }
}
