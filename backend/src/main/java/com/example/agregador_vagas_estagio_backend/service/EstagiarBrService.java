package com.example.agregador_vagas_estagio_backend.service;

import java.io.IOException;
import java.util.ArrayList; // Import necessário para a lista
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service; // Não esqueça do @Service aqui também!

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;

@Service("estagiarbrService") //As fontes passam todas pra minúsculo, aí sem essa linha daria erro
public class EstagiarBrService implements VagaScraper{

    @Override
    public List<VagaDTO> retornaVagas(String termo, String local, int pagina) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();

        try {  
            String urlBase = "https://www.estagiar-br.com.br/oportunidades/estagio";
            String url = String.format(urlBase, termo);
            
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .get();

            return parseDocument(doc, termo, local);

        } catch (IOException e) {
            return listaDeVagas; 
        }
    }

    public List<VagaDTO> parseDocument(Document doc, String termo, String local) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        Element jobsListUl = doc.selectFirst("body");

        if (jobsListUl != null) {
            Elements jobs = jobsListUl.select("a.vacancy-link");
            
            for (Element job : jobs) {
                Element titleElement = job.selectFirst("h1");
                String titulo = (titleElement != null) ? titleElement.text() : "Não encontrado";
                String linkVaga = "https://www.estagiar-br.com.br" + job.attr("href");
                
                Element employerElement = job.selectFirst("img.vacancy-logo + span");
                String empresa = (employerElement != null) ? employerElement.text() : "Não encontrado";
                
                Element locationElement = job.selectFirst("h3");
                String localizacao = (locationElement != null) ? locationElement.text() : "Não encontrado";
                
                if (local != null && !local.isBlank()) {
                    if (!localizacao.toLowerCase().contains(local.toLowerCase())) {
                        continue;
                    }
                }

                if (termo != null && !termo.isBlank()) {
                    if (!titulo.toLowerCase().contains(termo.toLowerCase())) {
                        continue;
                    }
                }

                VagaDTO vaga = new VagaDTO(titulo, empresa, localizacao, linkVaga, "EstagiarBR");
                listaDeVagas.add(vaga);
            }
        }

        return listaDeVagas;
    }
}