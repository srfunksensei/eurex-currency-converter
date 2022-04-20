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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Milan Brankovic
 */
@SpringBootTest
public class SwaggerTest {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void swaggerUiPresent() throws Exception {
        mvc.perform(get("/swagger-ui/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/swagger-ui/index.html"));

        mvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/swagger-ui/springfox.css"))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/swagger-ui/springfox.js"))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/swagger-resources/configuration/ui"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void swaggerApiDocsPresentPublic() throws Exception {
        mvc.perform(get("/v2/api-docs")
                        .param("group", "public"))
                .andExpect(status().is2xxSuccessful());
    }
}
