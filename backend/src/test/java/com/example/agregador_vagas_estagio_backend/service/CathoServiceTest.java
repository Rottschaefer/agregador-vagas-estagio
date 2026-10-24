package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CathoServiceTest {

    private final CathoService service = new CathoService();

    @Test
    @DisplayName("Deve fazer parsing do HTML de vagas da Catho com sucesso")
    void deveFazerParsingDocumentoCatho() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/catho.html")) {
            assertNotNull(is, "Fixture /fixtures/catho.html não encontrada");
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html);

            List<VagaDTO> vagas = service.parseDocument(doc, "");

            assertNotNull(vagas);
            assertFalse(vagas.isEmpty(), "Deveria extrair pelo menos uma vaga da fixture");
            VagaDTO primeira = vagas.get(0);
            assertNotNull(primeira.titulo());
            assertNotNull(primeira.link());
            assertTrue(primeira.link().startsWith("http"));
            assertEquals("Catho", primeira.fonte());
        }
    }

    @Test
    @DisplayName("Deve filtrar vagas por localidade quando informado")
    void deveFiltrarVagasPorLocal() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/catho.html")) {
            assertNotNull(is);
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(html);

            List<VagaDTO> vagasFiltroInexistente = service.parseDocument(doc, "CidadeInexistenteXYZ");
            assertTrue(vagasFiltroInexistente.isEmpty());
        }
    }
}
