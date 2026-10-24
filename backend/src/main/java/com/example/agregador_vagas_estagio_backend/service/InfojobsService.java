package com.example.agregador_vagas_estagio_backend.service;

import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service; 

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;

@Slf4j
@Service
public class InfojobsService implements VagaScraper{

    @Override
    public List<VagaDTO> retornaVagas(String termo, String local, int pagina) {
        try {  
            String query = (termo != null && !termo.isBlank()) ? termo : "";
            if (local != null && !local.isBlank()) {
                query = query.isBlank() ? local : query + " " + local;
            }
            String queryEncoded = java.net.URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8);

            String urlBase = "https://www.infojobs.com.br/empregos.aspx?palabra=%s";
            String url = String.format(urlBase, queryEncoded);

            Document doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .get();
            
            return parseDocument(doc, local);

        } catch (IOException e) {
            log.error("Erro ao realizar scraping no Infojobs", e); 
            return new ArrayList<>(); 
        }
    }

    public List<VagaDTO> parseDocument(Document doc, String local) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        Element jobsListUl = doc.selectFirst("div.js_vacanciesGridFragment.mb-16");

        if (jobsListUl != null) {
            Elements jobs = jobsListUl.select("div.card.js_rowCard");
            
            for (Element job : jobs) {
                Element titleElement = job.selectFirst("h2.js_vacancyTitle");
                String titulo = (titleElement != null) ? titleElement.text() : "Não encontrado";

                String linkVaga = "";
                if (titleElement != null && titleElement.parent() != null){
                    String href = titleElement.parent().attr("href");
                    linkVaga = "https://www.infojobs.com.br" + href;
                }
                else{
                    linkVaga = "Não encontrado";
                }
                
                Element employerElement = job.selectFirst("div.align-items-baseline a");
                String empresa = (employerElement != null) ? employerElement.text() : "Não encontrado";
                
                Element locationElement = job.selectFirst("div.mb-8");
                String localizacao = (locationElement != null) ? locationElement.ownText() : "Não encontrado";
                if (local != null && !local.isBlank()) {
                    if (!localizacao.toLowerCase().contains(local.toLowerCase())) {
                        continue;
                    }
                }

                VagaDTO vaga = new VagaDTO(titulo, empresa, localizacao, linkVaga, "Infojobs");
                listaDeVagas.add(vaga);
            }
        }

        return listaDeVagas;
    }
}