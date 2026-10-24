package com.example.agregador_vagas_estagio_backend.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;

import static org.assertj.core.api.Assertions.assertThat;

class EstagiarBrServiceTest {

    private EstagiarBrService service;
    private Document doc;

    @BeforeEach
    void setUp() throws Exception {
        service = new EstagiarBrService();
        try (InputStream is = getClass().getResourceAsStream("/fixtures/estagiarbr.html")) {
            assertThat(is).isNotNull();
            doc = Jsoup.parse(is, StandardCharsets.UTF_8.name(), "https://www.estagiar-br.com.br");
        }
    }

    @Test
    @DisplayName("Deve extrair todas as vagas da fixture offline sem filtros")
    void deveExtrairVagasSemFiltros() {
        List<VagaDTO> vagas = service.parseDocument(doc, "", "");

        assertThat(vagas).hasSize(2);
        VagaDTO vaga1 = vagas.get(0);
        assertThat(vaga1.titulo()).isEqualTo("Estágio em Desenvolvimento Java");
        assertThat(vaga1.empresa()).isEqualTo("Tech Inovações");
        assertThat(vaga1.localizacao()).isEqualTo("São Paulo, SP");
        assertThat(vaga1.link()).isEqualTo("https://www.estagiar-br.com.br/oportunidades/estagio/101");
        assertThat(vaga1.fonte()).isEqualTo("EstagiarBR");
    }

    @Test
    @DisplayName("Deve filtrar vagas por termo de busca")
    void deveFiltrarPorTermo() {
        List<VagaDTO> vagas = service.parseDocument(doc, "Java", "");

        assertThat(vagas).hasSize(1);
        assertThat(vagas.get(0).titulo()).containsIgnoringCase("Java");
    }

    @Test
    @DisplayName("Deve filtrar vagas por localidade")
    void deveFiltrarPorLocalidade() {
        List<VagaDTO> vagas = service.parseDocument(doc, "", "Rio de Janeiro");

        assertThat(vagas).hasSize(1);
        assertThat(vagas.get(0).localizacao()).containsIgnoringCase("Rio de Janeiro");
    }
}
