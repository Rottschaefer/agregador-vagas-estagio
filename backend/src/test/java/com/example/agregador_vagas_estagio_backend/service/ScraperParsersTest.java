package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScraperParsersTest {

    private Document carregarFixture(String nomeArquivo) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/" + nomeArquivo)) {
            if (is == null) {
                throw new IllegalArgumentException("Fixture não encontrada: " + nomeArquivo);
            }
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return Jsoup.parse(html);
        }
    }

    @Test
    @DisplayName("EstagiarBr: deve extrair vagas corretamente da fixture HTML")
    void deveExtrairVagasEstagiarBr() throws IOException {
        Document doc = carregarFixture("estagiarbr.html");
        EstagiarBrService service = new EstagiarBrService();

        List<VagaDTO> vagas = service.parseDocument(doc, "java", "");

        assertThat(vagas).hasSize(1);
        VagaDTO vaga = vagas.get(0);
        assertThat(vaga.titulo()).isEqualTo("Estágio em Desenvolvimento Java");
        assertThat(vaga.empresa()).isEqualTo("Tech Corp");
        assertThat(vaga.localizacao()).isEqualTo("São Paulo, SP");
        assertThat(vaga.link()).isEqualTo("https://www.estagiar-br.com.br/vaga/12345");
        assertThat(vaga.fonte()).isEqualTo("EstagiarBR");
    }

    @Test
    @DisplayName("Infojobs: deve extrair vagas e respeitar filtro de local")
    void deveExtrairVagasInfojobs() throws IOException {
        Document doc = carregarFixture("infojobs.html");
        InfojobsService service = new InfojobsService();

        List<VagaDTO> vagasSP = service.parseDocument(doc, "São Paulo");
        assertThat(vagasSP).hasSize(1);
        assertThat(vagasSP.get(0).titulo()).isEqualTo("Estágio em TI");
        assertThat(vagasSP.get(0).localizacao()).contains("São Paulo");

        List<VagaDTO> todas = service.parseDocument(doc, "");
        assertThat(todas).hasSize(2);
    }

    @Test
    @DisplayName("Glassdoor: deve extrair vagas da fixture HTML")
    void deveExtrairVagasGlassdoor() throws IOException {
        Document doc = carregarFixture("glassdoor.html");
        GlassdoorService service = new GlassdoorService();

        List<VagaDTO> vagas = service.parseDocument(doc, "");

        assertThat(vagas).hasSize(2);
        assertThat(vagas.get(0).titulo()).isEqualTo("Estágio em Engenharia de Software");
        assertThat(vagas.get(0).empresa()).isEqualTo("Google");
        assertThat(vagas.get(0).localizacao()).isEqualTo("Belo Horizonte, MG");
        assertThat(vagas.get(0).fonte()).isEqualTo("Glassdoor");
    }

    @Test
    @DisplayName("Gupy: deve extrair vagas da fixture HTML")
    void deveExtrairVagasGupy() throws IOException {
        Document doc = carregarFixture("gupy.html");
        GupyService service = new GupyService();

        List<VagaDTO> vagas = service.parseDocument(doc, "");

        assertThat(vagas).hasSize(2);
        assertThat(vagas.get(0).titulo()).isEqualTo("Estágio Backend Java");
        assertThat(vagas.get(0).empresa()).isEqualTo("Mercado Livre");
        assertThat(vagas.get(0).localizacao()).isEqualTo("São Paulo, SP");
        assertThat(vagas.get(0).fonte()).isEqualTo("Gupy");
    }

    @Test
    @DisplayName("RecrutaEasy: deve extrair vagas da fixture HTML")
    void deveExtrairVagasRecrutaEasy() throws IOException {
        Document doc = carregarFixture("recrutaeasy.html");
        RecrutaEasy service = new RecrutaEasy();

        List<VagaDTO> vagas = service.parseDocument(doc, "");

        assertThat(vagas).hasSize(2);
        assertThat(vagas.get(0).titulo()).isEqualTo("Estágio em QA e Testes");
        assertThat(vagas.get(0).empresa()).isEqualTo("Software House");
        assertThat(vagas.get(0).localizacao()).isEqualTo("Rio de Janeiro, RJ");
        assertThat(vagas.get(0).link()).isEqualTo("https://recrutaeasy.com.br/vagas/detalhe/50");
        assertThat(vagas.get(0).fonte()).isEqualTo("RecrutaEasy");
    }

    @Test
    @DisplayName("AcademiaUniversitario: deve extrair vagas da fixture HTML")
    void deveExtrairVagasAcademiaUniversitario() throws IOException {
        Document doc = carregarFixture("academiauniversitario.html");
        AcademiaUniversitarioService service = new AcademiaUniversitarioService();

        List<VagaDTO> vagas = service.parseDocument(doc, "");

        assertThat(vagas).hasSize(2);
        assertThat(vagas.get(0).empresa()).isEqualTo("Empresa Exemplo SA");
        assertThat(vagas.get(0).titulo()).isEqualTo("Estágio em Administração e Finanças");
        assertThat(vagas.get(0).localizacao()).isEqualTo("SP");
        assertThat(vagas.get(0).link()).isEqualTo("https://app.academiadouniversitario.com.br/vaga/7788");
        assertThat(vagas.get(0).fonte()).isEqualTo("Academia do Universitário");
    }
}
