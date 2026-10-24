package com.example.agregador_vagas_estagio_backend.service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;

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

@Service
public class AgregadorService {

    String[] fontes = {
        "Gupy",
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
        if (fonte == "") return buscaVagas(termo, local, pagina);
        
        String beanName = fonte.toLowerCase() + "Service";
        
        VagaScraper scraper = scrapers != null ? scrapers.get(beanName) : null;
        
        if (scraper == null) {
            throw new IllegalArgumentException("Fonte de vagas inválida: " + fonte);
        }
        
        return scraper.retornaVagas(termo, local, pagina);
    }

    public List<VagaDTO> buscaVagas(String termo, String local, int pagina) {
        List<CompletableFuture<List<VagaDTO>>> futures = new ArrayList<>();

        for (String fonte : fontes) {
            String beanName = fonte.toLowerCase() + "Service";
            VagaScraper scraper = scrapers != null ? scrapers.get(beanName) : null;

            if (scraper == null) {
                continue;
            }

            CompletableFuture<List<VagaDTO>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return scraper.retornaVagas(termo, local, pagina);
                } catch (Exception e) {
                    System.err.println("Erro ao buscar vagas na fonte " + fonte + ": " + e.getMessage());
                    return Collections.<VagaDTO>emptyList();
                }
            }, executor)
            .orTimeout(12, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                System.err.println("Timeout ou falha na fonte " + fonte + ": " + ex.getMessage());
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
