package com.example.finmanagerbackend.limit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;


@SpringBootTest
@AutoConfigureMockMvc
public class LimitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addNewLimitTest() throws Exception {
        // some data for test
        LimitDTO limitDTO = new LimitDTO(
                LimitType.DAY,
                new BigDecimal( 100 ),
                null,
                LocalDate.now()
        );

        // casting to json style
        String limitJSON = objectMapper.writeValueAsString( limitDTO );

        // testing is correct data was sent to method and controller got it good
        mockMvc.perform( MockMvcRequestBuilders.post( "/api/v1/limits/" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( limitJSON ) )
                .andExpect( MockMvcResultMatchers.status().isOk() );
    }

    // todo: i potem zmienić na oczekiwanie ...perform...andExpect(notFound)
    @Test
    public void deleteLimitTest_ExceptionIdIsNotExists() throws Exception {
        Assertions.assertThrows( ServletException.class, () -> {
            mockMvc.perform( MockMvcRequestBuilders.delete( "/api/v1/limits/2" ) )
                    .andReturn();
        } );
    }
}
