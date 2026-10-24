package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregadorServiceTest {

    @InjectMocks
    private AgregadorService agregadorService;

    @Mock
    private VagaScraper gupyScraper;

    @Mock
    private VagaScraper glassdoorScraper;

    @Mock
    private VagaScraper infojobsScraper;

    @Mock
    private VagaScraper estagiarbrScraper;

    @Mock
    private VagaScraper academiauniversitarioScraper;

    @Mock
    private VagaScraper recrutaeasyScraper;

    private Map<String, VagaScraper> scrapersMap;

    @BeforeEach
    void setUp() {
        scrapersMap = new HashMap<>();
        scrapersMap.put("gupyService", gupyScraper);
        scrapersMap.put("glassdoorService", glassdoorScraper);
        scrapersMap.put("infojobsService", infojobsScraper);
        scrapersMap.put("estagiarbrService", estagiarbrScraper);
        scrapersMap.put("academiauniversitarioService", academiauniversitarioScraper);
        scrapersMap.put("recrutaeasyService", recrutaeasyScraper);

        ReflectionTestUtils.setField(agregadorService, "scrapers", scrapersMap);
    }

    @Test
    @DisplayName("Deve buscar vagas com sucesso para uma fonte específica")
    void deveBuscarPorFonteEspecificaComSucesso() {
        List<VagaDTO> vagasEsperadas = List.of(
                new VagaDTO("Estágio Java", "Tech SA", "Remoto", "https://gupy.io/1", "Gupy")
        );
        when(gupyScraper.retornaVagas("java", "remoto", 1)).thenReturn(vagasEsperadas);

        List<VagaDTO> resultado = agregadorService.buscaPorFonte("Gupy", "java", "remoto", 1);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).titulo()).isEqualTo("Estágio Java");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando a fonte for desconhecida")
    void deveLancarExcecaoParaFonteInvalida() {
        assertThatThrownBy(() -> agregadorService.buscaPorFonte("FonteInexistente", "java", "", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fonte de vagas inválida: FonteInexistente");
    }

    @Test
    @DisplayName("Deve agregar vagas de múltiplas fontes com sucesso")
    void deveAgregarVagasDeMultiplasFontes() {
        when(gupyScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of(new VagaDTO("Vaga 1", "Empresa A", "SP", "link1", "Gupy")));
        when(glassdoorScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of(new VagaDTO("Vaga 2", "Empresa B", "RJ", "link2", "Glassdoor")));
        when(infojobsScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(estagiarbrScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(academiauniversitarioScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(recrutaeasyScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());

        List<VagaDTO> resultado = agregadorService.buscaVagas("java", "SP", 1);

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(VagaDTO::titulo).containsExactlyInAnyOrder("Vaga 1", "Vaga 2");
    }

    @Test
    @DisplayName("Deve ser resiliente quando uma fonte falhar com exceção")
    void deveContinuarQuandoUmaFonteFalhar() {
        when(gupyScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Timeout de scraping na Gupy"));
        when(glassdoorScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of(new VagaDTO("Vaga Glassdoor", "Empresa B", "RJ", "link2", "Glassdoor")));
        when(infojobsScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(estagiarbrScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(academiauniversitarioScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());
        when(recrutaeasyScraper.retornaVagas(anyString(), anyString(), anyInt()))
                .thenReturn(List.of());

        List<VagaDTO> resultado = agregadorService.buscaVagas("java", "RJ", 1);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).titulo()).isEqualTo("Vaga Glassdoor");
    }
}
