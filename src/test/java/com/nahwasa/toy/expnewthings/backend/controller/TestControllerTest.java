package com.nahwasa.toy.expnewthings.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("test TestController")
    @Test
    public void shouldTestReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Success")));
    }

    @DisplayName("testWithPath Test")
    @Test
    public void shouldTestWithPathReturnMessageIncludeingPathVariable() throws Exception {
        String testVar = "testVar!!";
        mockMvc.perform(get("/test/path/{var}/end", testVar))
                .andDo(print())
                .andExpect(content().string(containsString("Test Path : " + testVar + " Success")));
    }

}