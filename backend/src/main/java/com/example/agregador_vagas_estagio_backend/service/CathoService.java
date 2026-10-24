package com.example.agregador_vagas_estagio_backend.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.example.agregador_vagas_estagio_backend.dto.VagaDTO;
import com.example.agregador_vagas_estagio_backend.interfaces.VagaScraper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("cathoService")
public class CathoService implements VagaScraper {

    @Override
    public List<VagaDTO> retornaVagas(String termo, String local, int pagina) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        try {
            String busca = (termo != null && !termo.isBlank()) ? termo.trim() : "";
            String queryParam = busca.isEmpty() ? "" : URLEncoder.encode(busca, StandardCharsets.UTF_8);
            String url = String.format("https://www.catho.com.br/vagas/estagio/?q=%s&page=%d", queryParam, Math.max(1, pagina));

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            return parseDocument(doc, local);
        } catch (Exception e) {
            log.error("Erro ao realizar scraping na Catho", e);
            return listaDeVagas;
        }
    }

    public List<VagaDTO> parseDocument(Document doc, String local) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        if (doc == null) {
            return listaDeVagas;
        }

        Elements jobElements = doc.select("article.offer");
        for (Element job : jobElements) {
            try {
                Element titleLink = job.selectFirst("h2.title_offer a, h2 a");
                if (titleLink == null) {
                    continue;
                }

                String titulo = titleLink.text().trim();
                String href = titleLink.attr("href").trim();
                String link = href.startsWith("/") ? "https://www.catho.com.br" + href : href;

                Element companyElement = job.selectFirst("p span.text-12, p.mb-2 span");
                String empresa = companyElement != null && !companyElement.text().isBlank()
                        ? companyElement.text().trim()
                        : "Confidencial / Não informada";

                Element locationP = job.selectFirst("p:has(span.i_job_location)");
                String localizacao = "Não informada";
                if (locationP != null) {
                    String raw = locationP.text();
                    int dashIdx = raw.lastIndexOf('-');
                    if (dashIdx != -1 && dashIdx + 1 < raw.length()) {
                        localizacao = raw.substring(dashIdx + 1).trim();
                    } else {
                        localizacao = raw.replace("vaga", "").replace("vagas", "").replace("1", "").trim();
                    }
                }

                if (local != null && !local.isBlank()) {
                    if (!localizacao.toLowerCase().contains(local.toLowerCase())) {
                        continue;
                    }
                }

                listaDeVagas.add(new VagaDTO(titulo, empresa, localizacao, link, "Catho"));
            } catch (Exception e) {
                log.warn("Erro ao processar card de vaga na Catho: {}", e.getMessage());
            }
        }

        return listaDeVagas;
    }
}
