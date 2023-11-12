package com.ruanbekker.cargarage;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("IT")

public class MyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VactorSearchManager vactorSearchManager;


    @Test
    void shouldStoreItem() throws Exception {
        vactorSearchManager.store("shop1", UUID.randomUUID().toString(),"my text");
        vactorSearchManager.cosineSearch("shop1","my text");
    }
}
