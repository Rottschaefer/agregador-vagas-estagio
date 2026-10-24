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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final ExecutorService executor;

    public AgregadorService() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public AgregadorService(Map<String, VagaScraper> scrapers) {
        this(scrapers, Executors.newVirtualThreadPerTaskExecutor());
    }

    public AgregadorService(Map<String, VagaScraper> scrapers, ExecutorService executor) {
        this.scrapers = scrapers;
        this.executor = executor != null ? executor : Executors.newVirtualThreadPerTaskExecutor();
    }

    //Busca apenas na fonte especificada na URL
    public List<VagaDTO> buscaPorFonte(String fonte, String termo, String local, int pagina) {
        String termoNormalizado = termo != null ? termo.trim() : "";
        String localNormalizado = local != null ? local.trim() : "";

        if (fonte == null || fonte.isBlank() || fonte.equalsIgnoreCase("todas")) {
            return buscaVagas(termoNormalizado, localNormalizado, pagina);
        }
        
        String beanName = fonte.toLowerCase() + "Service";
        
        VagaScraper scraper = scrapers != null ? scrapers.get(beanName) : null;
        
        if (scraper == null) {
            throw new IllegalArgumentException("Fonte de vagas inválida: " + fonte);
        }
        
        return scraper.retornaVagas(termoNormalizado, localNormalizado, pagina);
    }

    public List<VagaDTO> buscaVagas(String termo, String local, int pagina) {
        String termoNormalizado = termo != null ? termo.trim() : "";
        String localNormalizado = local != null ? local.trim() : "";

        List<CompletableFuture<List<VagaDTO>>> futures = new ArrayList<>();

        for (String fonte : fontes) {
            String beanName = fonte.toLowerCase() + "Service";
            VagaScraper scraper = scrapers != null ? scrapers.get(beanName) : null;

            if (scraper == null) {
                continue;
            }

            CompletableFuture<List<VagaDTO>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return scraper.retornaVagas(termoNormalizado, localNormalizado, pagina);
                } catch (Exception e) {
                    log.error("Erro ao buscar vagas na fonte {}", fonte, e);
                    return Collections.<VagaDTO>emptyList();
                }
            }, executor)
            .orTimeout(12, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.warn("Timeout ou falha na fonte {}: {}", fonte, ex.getMessage());
                return Collections.emptyList();
            });

            futures.add(future);
        }

        List<VagaDTO> vagas = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(vagas);
        return vagas;
    }
        
}
