package com.example.agregador_vagas_estagio_backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgregadorServiceFontesTest {

    @Test
    @DisplayName("Deve listar todas as fontes registradas")
    void deveListarTodasAsFontes() {
        AgregadorService service = new AgregadorService();
        List<String> fontes = service.listarFontes();

        assertNotNull(fontes);
        assertEquals(6, fontes.size());
        assertTrue(fontes.contains("Gupy"));
        assertTrue(fontes.contains("RecrutaEasy"));
        assertTrue(fontes.contains("Glassdoor"));
        assertTrue(fontes.contains("InfoJobs"));
        assertTrue(fontes.contains("EstagiarBR"));
        assertTrue(fontes.contains("AcademiaUniversitario"));
    }
}
