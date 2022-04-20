package com.mb.ecb.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Milan Brankovic
 */
@SpringBootTest
public class HealthEndpointTest {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void getHealth() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
