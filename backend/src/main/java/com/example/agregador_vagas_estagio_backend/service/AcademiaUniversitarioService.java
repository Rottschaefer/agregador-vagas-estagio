package com.example.agregador_vagas_estagio_backend.service;

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
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

@Slf4j
@Service("academiauniversitarioService")
public class AcademiaUniversitarioService implements VagaScraper{

    @Override
    public List<VagaDTO> retornaVagas(String termo, String local, int pagina) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            page.navigate("https://app.academiadouniversitario.com.br/");

    
            String termoBusca = (termo != null && !termo.isBlank()) ? termo : "";
            if (local != null && !local.isBlank()) {
                termoBusca = termoBusca.isBlank() ? local : termoBusca + " " + local;
            }

            Locator inputBusca = page.locator("input[name='job']");

            inputBusca.fill(termoBusca);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Buscar minha vaga")).first().click();
            
            page.waitForSelector("div[data-testid='jobs-list-grid']", new Page.WaitForSelectorOptions().setTimeout(7000));

            //Espera as requisições da página acabarem antes de procurar as tags
            page.waitForLoadState(LoadState.NETWORKIDLE);

            String htmlRenderizado = page.content();
            Document doc = Jsoup.parse(htmlRenderizado);
            browser.close();

            return parseDocument(doc, local);
        } catch (Exception e) {
            log.error("Erro ao realizar scraping na Academia do Universitário", e);
        }

        return listaDeVagas;
    }

    public List<VagaDTO> parseDocument(Document doc, String local) {
        List<VagaDTO> listaDeVagas = new ArrayList<>();
        Element jobsListDiv = doc.selectFirst("div[data-testid=jobs-list-grid]");

        if (jobsListDiv != null) {
            Elements jobs = jobsListDiv.select("article");
            
            for (Element job : jobs) {
                try {
                    Elements h3 = job.select("h3");
                    
                    String empresa = !h3.isEmpty() ? h3.get(0).text() : "Não informada";
                    String titulo = h3.size() >= 2 ? h3.get(1).text() : (!h3.isEmpty() ? h3.get(0).text() : "Título não encontrado");

                    Element localizacaoElement = null;
                    Elements spans = job.select("span");
                    for (Element span : spans) {
                        String txt = span.text().trim();
                        if (!txt.isEmpty() && (txt.matches("^[A-Z]{2}$") || txt.toLowerCase().contains("remoto") || txt.toLowerCase().contains("híbrido") || txt.contains("-") || txt.contains(","))) {
                            localizacaoElement = span;
                            break;
                        }
                    }
                    if (localizacaoElement == null && !spans.isEmpty()) {
                        localizacaoElement = spans.first();
                    }
                    String localizacao = (localizacaoElement != null) ? localizacaoElement.text() : "Remoto/Não informado";
                    if (local != null && !local.isBlank()) {
                        if (!localizacao.toLowerCase().contains(local.toLowerCase())) {
                            continue;
                        }
                    }

                    Element linkElement = job.selectFirst("a[href^=/vaga/]"); 
                    if (linkElement == null) {
                        linkElement = job.selectFirst("a"); 
                    }
                    String linkRelativo = (linkElement != null) ? linkElement.attr("href") : "";
                    String link = !linkRelativo.isEmpty() ? "https://app.academiadouniversitario.com.br" + linkRelativo : "";

                    VagaDTO vaga = new VagaDTO(titulo, empresa, localizacao, link, "Academia do Universitário");
                    listaDeVagas.add(vaga);
                } catch (Exception cardError) {
                    log.warn("Erro ao processar card da Academia do Universitário: {}", cardError.getMessage());
                }
            }
        } else {
            log.warn("Não foi possível encontrar o container 'jobs-list-grid'.");
        }

        return listaDeVagas;
    }
}