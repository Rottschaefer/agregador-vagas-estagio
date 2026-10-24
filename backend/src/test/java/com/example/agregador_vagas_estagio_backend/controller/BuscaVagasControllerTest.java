package com.example.agregador_vagas_estagio_backend.controller;

import com.example.agregador_vagas_estagio_backend.exception.GlobalExceptionHandler;
import com.example.agregador_vagas_estagio_backend.service.AgregadorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuscaVagas.class)
@Import(GlobalExceptionHandler.class)
class BuscaVagasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgregadorService agregadorService;

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando IllegalArgumentException for lançada")
    void deveRetornar400QuandoFonteInvalida() throws Exception {
        when(agregadorService.buscaPorFonte(eq("invalida"), any(), any(), anyInt()))
                .thenThrow(new IllegalArgumentException("Fonte de vagas inválida: invalida"));

        mockMvc.perform(get("/buscar-vagas")
                        .param("fonte", "invalida")
                        .param("termo", "java"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("Fonte de vagas inválida: invalida"));
    }
}
