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
@Service("vagascomService")
public class VagasComService implements VagaScraper {

    @Override
    public List<VagaDTO> retornaVagas(String termo, String local, int pagina) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        try {
            String busca = (termo != null && !termo.isBlank()) ? termo.trim() : "";
            String queryParam = busca.isEmpty() ? "" : URLEncoder.encode(busca, StandardCharsets.UTF_8);
            String url = String.format("https://www.vagas.com.br/vagas-de-estagio?q=%s&pagina=%d", queryParam, Math.max(1, pagina));

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            return parseDocument(doc, local);
        } catch (Exception e) {
            log.error("Erro ao realizar scraping no Vagas.com", e);
            return listaDeVagas;
        }
    }

    public List<VagaDTO> parseDocument(Document doc, String local) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        if (doc == null) {
            return listaDeVagas;
        }

        Elements jobElements = doc.select("li.vaga");
        for (Element job : jobElements) {
            try {
                Element linkElement = job.selectFirst("a.link-detalhes-vaga");
                if (linkElement == null) {
                    continue;
                }

                String titulo = linkElement.hasAttr("title") && !linkElement.attr("title").isBlank()
                        ? linkElement.attr("title").trim()
                        : linkElement.text().trim();

                String href = linkElement.attr("href").trim();
                String link = href.startsWith("/") ? "https://www.vagas.com.br" + href : href;

                Element empresaElement = job.selectFirst("span.emprVaga");
                String empresa = empresaElement != null && !empresaElement.text().isBlank()
                        ? empresaElement.text().trim()
                        : "Não informada";

                Element localElement = job.selectFirst("div.vaga-local, span.vaga-local");
                String localizacao = localElement != null && !localElement.text().isBlank()
                        ? localElement.text().trim()
                        : "Não informada";

                if (local != null && !local.isBlank()) {
                    if (!localizacao.toLowerCase().contains(local.toLowerCase())) {
                        continue;
                    }
                }

                listaDeVagas.add(new VagaDTO(titulo, empresa, localizacao, link, "Vagas.com"));
            } catch (Exception e) {
                log.warn("Erro ao processar card de vaga no Vagas.com: {}", e.getMessage());
            }
        }

        return listaDeVagas;
    }
}
