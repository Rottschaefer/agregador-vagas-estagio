# AGENTS.md

Internship job aggregator. Monorepo with two independent apps (no shared code, no root task runner):
- `frontend/` — React 19 + Vite + TS, Tailwind v4 (via `@tailwindcss/vite`), Zustand, TanStack Query, React Router.
- `backend/` — Spring Boot 4.1 (webmvc), Java 25, Maven Wrapper, Lombok. Scrapes job sites with Jsoup and Playwright (Java).

Code, identifiers and comments are in Portuguese (`vaga`, `fonte`, `termo`, `pagina`); follow that.

## Commands

Frontend (from `frontend/`):
- `npm install`, `npm run dev` (port 5173), `npm run build` (`tsc -b && vite build`), `npm run lint`
- Lint does not typecheck; run `npm run build` to catch TS errors. Lint must pass before commit.
- No frontend test setup exists.

Backend (from `backend/`):
- `./mvnw spring-boot:run` (port 8080), `./mvnw test`, `./mvnw package`
- Use the wrapper; no global Maven assumed. Requires JDK 25.
- Only test is `contextLoads` (`@SpringBootTest`). Single test: `./mvnw test -Dtest=ClassName#method`.

## How it's wired

- Single endpoint: `GET /buscar-vagas?fonte=&termo=&local=&pagina=` in `controller/BuscaVagas.java` → `AgregadorService`.
- Frontend calls it from `src/hooks/useBuscaVagas.tsx`; base URL is hardcoded in `src/utils/constantes.ts` (`http://localhost:8080`). Backend CORS is hardcoded to `http://localhost:5173` via `@CrossOrigin`. Change both if ports change.
- Search state lives in `src/store/BuscaStore.ts` (`*Input` = form fields, `*Pesquisa` = committed query used by the hook).
- Empty `fonte` means "all sources": `AgregadorService.buscaVagas` loops its hardcoded `fontes` array, swallows per-source errors, and shuffles results.

## Adding / changing a scraper (source)

Each source is a `@Service` implementing `interfaces/VagaScraper` returning `List<VagaDTO>`. Resolution is by bean name: `fonte.toLowerCase() + "Service"`. So:
1. Multi-word class names need an explicit bean name, e.g. `@Service("estagiarbrService")`, `@Service("recrutaeasyService")`.
2. Add the name to the `fontes` array in `AgregadorService` (otherwise excluded from the all-sources search).
3. Add the display name to the `fontes` list in `frontend/src/components/BuscaForm.tsx`.

Scrapers operam em sites de terceiros. Para testes automatizados, **sempre utilize fixtures HTML salvas offline** em vez de chamadas de rede ao vivo (Playwright ou Jsoup), garantindo testes rápidos, determinísticos e sem dependência de internet.

## Terminal & Anti-Hang Guidelines

1. **Evite background tasks**: Para comandos de `git`, build (`npm run build`, `./mvnw test`), scripts ou chamadas de API (`curl`, `gh`), configure sempre `WaitMsBeforeAsync: 10000` (tempo síncrono máximo).
2. **Nunca encadeie comandos longos com `&&`**: Não execute `git add ... && git commit ... && git push ...` em um único comando. Isso faz a execução exceder o timeout síncrono e virar uma background task fantasma. Execute comandos atômicos separados: primeiro `git add`, depois `git commit`, depois `git push`.
3. **Nunca encerre o turno aguardando uma background task**: Se qualquer comando for convertido em background task (`task-xxx`), NUNCA finalize seu turno dizendo que vai aguardar o término. O terminal da IDE possui uma falha em que a notificação de conclusão nem sempre é emitida, deixando o agente congelado indefinidamente. Inspecione o status com `manage_task Action="status"`; se persistir em `RUNNING` por mais de 10 segundos, cancele a tarefa com `manage_task Action="kill"`, verifique o resultado real no disco (`git status`, arquivos gerados, `gh pr list`) e continue a execução imediatamente.

## Fixtures de HTML & Testes Offline para Scrapers

1. **Fixtures locais**: Salve páginas reais dos portais alvos (ou trechos representativos minificados) em `backend/src/test/resources/fixtures/` (ex: `vagascom.html`, `catho.html`, `infojobs.html`).
2. **Testes offline**: Crie testes unitários isolados com JUnit 5 que carregam essas fixtures estáticas e validam cada seletor CSS/DOM e a extração correta de campos (`titulo`, `empresa`, `local`, `link`, etc.).
3. **Resiliência a mudanças de layout**: Os testes devem cobrir variações de estrutura HTML e ausência de campos opcionais para evitar `NullPointerException` ou `IndexOutOfBoundsException`.

## Qualidade de Código, Tipagem e Linting Estrito

1. **Frontend**:
   - Zero erros no ESLint: execute `npm run lint` e elimine qualquer uso de `any` explícito (`@typescript-eslint/no-explicit-any`).
   - Tipagem rigorosa: garanta que `npm run build` (`tsc -b && vite build`) passe sem nenhum erro de tipagem.
2. **Backend**:
   - Java 25 estrito: compilação sem warnings desnecessários, tipagem estrita nos DTOs e entidades.
   - Boas práticas de logging: use `@Slf4j` e métodos estruturados (`log.info`, `log.warn`, `log.error`), sem `System.out.println` ou `e.printStackTrace()`.

## Geração Abrangente de Testes & Cobertura de Edge Cases

1. **Cobertura de Edge Cases**: Cobrir casos extremos em todos os serviços (parâmetros nulos, strings vazias, paginação inválida, falhas parciais em fontes simultâneas).
2. **Resiliência e Mutação**: Testes unitários devem testar cenários negativos, validação de exceções (`assertThrows`) e estabilidade de concorrência com Virtual Threads.
3. **Aprovação 100%**: Nenhum commit ou PR deve ser aberto sem que `./mvnw test` e `npm run build` estejam passando com 100% de sucesso.
