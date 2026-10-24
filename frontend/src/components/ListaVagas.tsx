import { useBuscaVagas } from "../hooks/useBuscaVagas";
import useBuscaStore from "../store/BuscaStore";
import type { Vaga } from "../types/vaga";

const getFonteBadgeColor = (fonte: string) => {
  const f = fonte.toLowerCase();
  if (f.includes("gupy")) return "bg-emerald-50 text-emerald-700 border-emerald-200";
  if (f.includes("catho")) return "bg-blue-50 text-blue-700 border-blue-200";
  if (f.includes("vagas")) return "bg-rose-50 text-rose-700 border-rose-200";
  if (f.includes("infojobs")) return "bg-amber-50 text-amber-700 border-amber-200";
  if (f.includes("glassdoor")) return "bg-teal-50 text-teal-700 border-teal-200";
  if (f.includes("estagiar")) return "bg-purple-50 text-purple-700 border-purple-200";
  return "bg-indigo-50 text-indigo-700 border-indigo-200";
};

const SkeletonCard = () => (
  <div className="bg-white rounded-2xl p-6 border border-slate-200/80 shadow-xs animate-pulse flex flex-col justify-between h-56">
    <div>
      <div className="flex justify-between items-start gap-3">
        <div className="h-5 bg-slate-200 rounded-md w-3/4 mb-3"></div>
        <div className="h-5 bg-slate-100 rounded-full w-20"></div>
      </div>
      <div className="h-4 bg-slate-200 rounded-md w-1/2 mb-2"></div>
      <div className="h-4 bg-slate-100 rounded-md w-1/3"></div>
    </div>
    <div className="pt-4 border-t border-slate-100 flex justify-between items-center">
      <div className="h-4 bg-slate-100 rounded-md w-24"></div>
      <div className="h-4 bg-slate-200 rounded-md w-16"></div>
    </div>
  </div>
);

const ListaVagas = () => {
  const nomeVaga = useBuscaStore((s) => s.nomePesquisa);
  const localidade = useBuscaStore((s) => s.localPesquisa);
  const fonteSelecionada = useBuscaStore((s) => s.fontePesquisa);
  const paginaAtual = useBuscaStore((s) => s.paginaAtual);
  const setPaginaAtual = useBuscaStore((s) => s.setPaginaAtual);
  const pesquisaIniciada = useBuscaStore((s) => s.pesquisaIniciada);

  const {
    data: vagas,
    isFetching,
    error,
  } = useBuscaVagas(nomeVaga, localidade, fonteSelecionada, paginaAtual, pesquisaIniciada);

  if (!pesquisaIniciada) {
    return (
      <div className="w-full mt-4 flex flex-col items-center">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-5xl">
          <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-xs flex flex-col items-center text-center">
            <div className="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-2xl mb-4">
              <i className="bi bi-collection-fill"></i>
            </div>
            <h3 className="font-bold text-slate-800 text-base mb-1.5">8 Portais em 1 Só Clique</h3>
            <p className="text-slate-500 text-xs leading-relaxed">
              Consolidamos Gupy, Catho, Vagas.com, InfoJobs e outros para que você não precise abrir dezenas de abas.
            </p>
          </div>

          <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-xs flex flex-col items-center text-center">
            <div className="w-12 h-12 rounded-xl bg-violet-50 text-violet-600 flex items-center justify-center text-2xl mb-4">
              <i className="bi bi-lightning-charge-fill"></i>
            </div>
            <h3 className="font-bold text-slate-800 text-base mb-1.5">Busca Concorrente</h3>
            <p className="text-slate-500 text-xs leading-relaxed">
              Scraping com Virtual Threads que dispara varreduras simultâneas, entregando resultados frescos em tempo real.
            </p>
          </div>

          <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-xs flex flex-col items-center text-center">
            <div className="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-2xl mb-4">
              <i className="bi bi-box-arrow-up-right"></i>
            </div>
            <h3 className="font-bold text-slate-800 text-base mb-1.5">Link Direto à Vaga</h3>
            <p className="text-slate-500 text-xs leading-relaxed">
              Sem intermediários ou formulários duplicados. Clique na vaga e vá direto para a página de inscrição oficial.
            </p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full flex flex-col items-center">
      {/* Loading State com Skeletons */}
      {isFetching && (
        <div className="w-full">
          <div className="flex items-center justify-center gap-3 mb-6 text-slate-600 text-sm font-medium">
            <div className="animate-spin rounded-full border-2 border-indigo-600 border-t-transparent w-4 h-4"></div>
            <span>Buscando oportunidades nas plataformas...</span>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 w-full">
            {Array.from({ length: 6 }).map((_, i) => (
              <SkeletonCard key={i} />
            ))}
          </div>
        </div>
      )}

      {/* Erro de busca */}
      {!isFetching && error && (
        <div className="w-full max-w-xl bg-rose-50 border border-rose-200 rounded-2xl p-6 text-center my-8">
          <div className="w-12 h-12 rounded-full bg-rose-100 text-rose-600 mx-auto flex items-center justify-center text-2xl mb-3">
            <i className="bi bi-exclamation-triangle"></i>
          </div>
          <h3 className="text-rose-900 font-bold text-lg mb-1">Não foi possível carregar as vagas</h3>
          <p className="text-rose-700 text-sm mb-4">
            {error.message || "Ocorreu um erro ao conectar com o serviço de agregação."}
          </p>
          <button
            onClick={() => window.location.reload()}
            className="px-4 py-2 bg-rose-600 text-white rounded-xl text-xs font-semibold hover:bg-rose-700 transition-colors cursor-pointer"
          >
            Tentar Novamente
          </button>
        </div>
      )}

      {/* Lista de Resultados */}
      {!isFetching && !error && (
        <div className="w-full">
          {/* Header de Resultados */}
          <div className="flex flex-col sm:flex-row items-center justify-between gap-3 mb-6 pb-4 border-b border-slate-200">
            <div className="flex items-center gap-2">
              <span className="text-lg font-bold text-slate-900">
                Oportunidades Encontradas
              </span>
              <span className="bg-indigo-50 text-indigo-700 text-xs font-bold px-2.5 py-1 rounded-full border border-indigo-100">
                {vagas?.length ?? 0} vagas
              </span>
            </div>

            {fonteSelecionada && (
              <span className="text-xs text-slate-500">
                Filtrado por: <strong className="text-slate-800">{fonteSelecionada}</strong>
              </span>
            )}
          </div>

          {/* Empty State */}
          {vagas?.length === 0 && (
            <div className="w-full py-16 bg-white rounded-2xl border border-slate-200 text-center flex flex-col items-center">
              <div className="w-14 h-14 rounded-full bg-slate-100 text-slate-400 flex items-center justify-center text-2xl mb-3">
                <i className="bi bi-search"></i>
              </div>
              <h4 className="text-slate-800 font-bold text-base mb-1">Nenhuma vaga encontrada</h4>
              <p className="text-slate-500 text-xs max-w-md">
                Tente buscar por termos mais amplos (como "Estágio", "TI" ou "Remoto") ou selecione outra fonte no filtro acima.
              </p>
            </div>
          )}

          {/* Cards Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
            {vagas?.map((vaga: Vaga, index: number) => {
              const badgeClass = getFonteBadgeColor(vaga.fonte);
              const isRemoto =
                vaga.localizacao?.toLowerCase().includes("remoto") ||
                vaga.titulo?.toLowerCase().includes("remoto");

              return (
                <a
                  key={vaga.id ?? `${vaga.fonte}-${vaga.link}-${index}`}
                  href={vaga.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="group block bg-white rounded-2xl p-6 border border-slate-200/90 shadow-2xs hover:shadow-xl hover:shadow-indigo-100/50 hover:border-indigo-300 hover:-translate-y-1 transition-all duration-300 flex flex-col justify-between h-full"
                >
                  <div>
                    {/* Top row: Badge da fonte + tag Remoto */}
                    <div className="flex items-center justify-between gap-2 mb-3">
                      <span className={`text-[11px] font-bold uppercase tracking-wider px-2.5 py-1 rounded-lg border ${badgeClass}`}>
                        {vaga.fonte}
                      </span>

                      {isRemoto && (
                        <span className="inline-flex items-center gap-1 text-[11px] font-semibold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-md border border-emerald-200/80">
                          <i className="bi bi-wifi text-[10px]"></i>
                          Remoto
                        </span>
                      )}
                    </div>

                    {/* Título da Vaga */}
                    <h2 className="text-base sm:text-lg font-bold text-slate-900 group-hover:text-indigo-600 transition-colors line-clamp-2 leading-snug mb-2">
                      {vaga.titulo}
                    </h2>

                    {/* Empresa */}
                    <div className="flex items-center gap-2 text-slate-600 text-xs font-medium mb-1.5">
                      <i className="bi bi-building text-slate-400"></i>
                      <span className="truncate">{vaga.empresa || "Empresa Confidencial"}</span>
                    </div>

                    {/* Localidade */}
                    <div className="flex items-center gap-2 text-slate-500 text-xs">
                      <i className="bi bi-geo-alt text-slate-400"></i>
                      <span className="truncate">{vaga.localizacao || "Brasil"}</span>
                    </div>
                  </div>

                  {/* Card Footer: Botão de ação */}
                  <div className="mt-5 pt-4 border-t border-slate-100 flex items-center justify-between text-xs font-semibold text-indigo-600 group-hover:text-indigo-700">
                    <span>Ver detalhes</span>
                    <span className="inline-flex items-center gap-1 group-hover:translate-x-0.5 transition-transform">
                      Candidatar-se <i className="bi bi-arrow-up-right text-xs"></i>
                    </span>
                  </div>
                </a>
              );
            })}
          </div>

          {/* Paginação */}
          {vagas &&
            vagas.length > 0 &&
            (fonteSelecionada === "Gupy" || fonteSelecionada === "RecrutaEasy") && (
              <div className="flex justify-center items-center gap-4 mt-12 mb-6">
                <button
                  onClick={() => setPaginaAtual(paginaAtual - 1)}
                  disabled={paginaAtual === 1}
                  className="px-5 py-2.5 rounded-xl font-semibold text-xs cursor-pointer bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:border-slate-300 transition-all shadow-2xs disabled:opacity-40 disabled:cursor-not-allowed flex items-center gap-2"
                >
                  <i className="bi bi-chevron-left text-xs"></i>
                  Anterior
                </button>

                <span className="text-slate-600 font-semibold text-xs bg-slate-100 px-3 py-1.5 rounded-lg">
                  Página {paginaAtual}
                </span>

                <button
                  onClick={() => setPaginaAtual(paginaAtual + 1)}
                  disabled={isFetching || vagas.length === 0}
                  className="px-5 py-2.5 rounded-xl font-semibold text-xs cursor-pointer bg-indigo-600 text-white hover:bg-indigo-700 transition-all shadow-xs shadow-indigo-500/25 disabled:opacity-40 disabled:cursor-not-allowed flex items-center gap-2"
                >
                  Próxima
                  <i className="bi bi-chevron-right text-xs"></i>
                </button>
              </div>
            )}
        </div>
      )}
    </div>
  );
};

export default ListaVagas;
