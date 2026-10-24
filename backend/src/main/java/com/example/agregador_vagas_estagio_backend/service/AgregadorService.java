package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AgregadorService {

    String[] fontes = {
        "Gupy",
        "RecrutaEasy",
        "Glassdoor",
        "InfoJobs",
        "EstagiarBR",
        "AcademiaUniversitario"
    };

    // Injeta todos os services que implementam VagaScraper aqui automaticamente
    @Autowired
    private Map<String, VagaScraper> scrapers;

    //Busca apenas na fonte especificada na URL
    public List<VagaDTO> buscaPorFonte(String fonte, String termo, String local, int pagina) {
        String termoNormalizado = termo != null ? termo.trim() : "";
        String localNormalizado = local != null ? local.trim() : "";

        if (fonte == null || fonte.isBlank() || fonte.equalsIgnoreCase("todas")) {
            return buscaVagas(termoNormalizado, localNormalizado, pagina);
        }
        
        String beanName = fonte.toLowerCase() + "Service";
        
        VagaScraper scraper = scrapers.get(beanName);
        
        if (scraper == null) {
            throw new IllegalArgumentException("Fonte de vagas inválida: " + fonte);
        }
        
        return scraper.retornaVagas(termoNormalizado, localNormalizado, pagina);
    }

    public List<VagaDTO> buscaVagas(String termo, String local, int pagina) {
        String termoNormalizado = termo != null ? termo.trim() : "";
        String localNormalizado = local != null ? local.trim() : "";

        List<VagaDTO> vagas = new ArrayList<>();
        
        for (String fonte : fontes) {
            String beanName = fonte.toLowerCase() + "Service";

            VagaScraper scraper = scrapers.get(beanName);

            if (scraper == null) {
                throw new IllegalArgumentException("Fonte de vagas inválida: " + fonte);
            }

            try {
                vagas.addAll(scraper.retornaVagas(termoNormalizado, localNormalizado, pagina));
            } catch (Exception e) {
                log.error("Erro ao buscar vagas na fonte {}", fonte, e);
            }

        }
        
        Collections.shuffle(vagas);
        
        return vagas;
    }
        
}
