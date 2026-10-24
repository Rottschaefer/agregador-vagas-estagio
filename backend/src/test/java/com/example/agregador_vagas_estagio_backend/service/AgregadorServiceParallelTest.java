package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AgregadorServiceParallelTest {

    @Test
    @DisplayName("Deve buscar vagas em paralelo de múltiplas fontes e agregar resultados com resiliência")
    void deveBuscarVagasEmParalelo() {
        VagaScraper scraper1 = (termo, local, pagina) -> List.of(
                new VagaDTO("Dev Java", "Empresa A", "SP", "https://vaga-a", "Gupy")
        );
        VagaScraper scraper2 = (termo, local, pagina) -> List.of(
                new VagaDTO("Dev Python", "Empresa B", "RJ", "https://vaga-b", "Glassdoor")
        );
        VagaScraper scraperFalho = (termo, local, pagina) -> {
            throw new RuntimeException("Erro de conexão simulado");
        };

        Map<String, VagaScraper> map = new HashMap<>();
        map.put("gupyService", scraper1);
        map.put("glassdoorService", scraper2);
        map.put("infojobsService", scraperFalho);

        AgregadorService service = new AgregadorService(map);
        List<VagaDTO> resultado = service.buscaVagas("dev", "Brasil", 1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(v -> v.empresa().equals("Empresa A")));
        assertTrue(resultado.stream().anyMatch(v -> v.empresa().equals("Empresa B")));
    }
}
