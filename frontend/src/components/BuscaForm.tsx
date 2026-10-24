import useBuscaStore from "../store/BuscaStore";
import { useFontes } from "../hooks/useFontes";

const FONTES_PADRAO = [
  "Gupy",
  "Catho",
  "VagasCom",
  "InfoJobs",
  "Glassdoor",
  "EstagiarBR",
  "RecrutaEasy",
  "AcademiaUniversitario",
];

const QUICK_TAGS = [
  { label: "🌐 Remoto", termo: "", local: "Remoto" },
  { label: "💻 Tecnologia", termo: "TI", local: "" },
  { label: "🚀 Frontend", termo: "Frontend", local: "" },
  { label: "☕ Backend", termo: "Backend", local: "" },
  { label: "📊 Dados & IA", termo: "Dados", local: "" },
  { label: "🎨 UI/UX Design", termo: "Design", local: "" },
  { label: "📈 Marketing", termo: "Marketing", local: "" },
];

const BuscaForm = () => {
  const nomeVaga = useBuscaStore((s) => s.nomeInput);
  const setNomeVaga = useBuscaStore((s) => s.setNomeInput);
  const localidade = useBuscaStore((s) => s.localInput);
  const setLocalidade = useBuscaStore((s) => s.setLocalInput);
  const fonteSelecionada = useBuscaStore((s) => s.fonteInput);
  const setFonteSelecionada = useBuscaStore((s) => s.setFonteInput);

  const handleBuscar = useBuscaStore((s) => s.buscar);
  const { data: fontesApi } = useFontes();
  const fontes = fontesApi && fontesApi.length > 0 ? fontesApi : FONTES_PADRAO;

  const handleSelecionarFonte = (fonte: string) => {
    if (fonteSelecionada === fonte) {
      setFonteSelecionada("");
    } else {
      setFonteSelecionada(fonte);
    }
  };

  const handleQuickTag = (termo: string, local: string) => {
    if (termo) setNomeVaga(termo);
    if (local) setLocalidade(local);
    setTimeout(() => {
      handleBuscar();
    }, 50);
  };

  const handleSubmit = (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    handleBuscar();
  };

  return (
    <div className="w-full flex flex-col items-center text-center">
      {/* Hero Headline */}
      <div className="max-w-3xl mb-8">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-50 border border-indigo-100/80 text-indigo-700 text-xs font-semibold mb-4 shadow-2xs">
          <i className="bi bi-stars text-indigo-500"></i>
          <span>Agregador Inteligente de Vagas</span>
        </div>
        <h1 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold text-slate-900 tracking-tight leading-tight">
          Encontre seu estágio ideal <br className="hidden sm:inline" />
          <span className="bg-gradient-to-r from-indigo-600 via-violet-600 to-purple-600 bg-clip-text text-transparent">
            em um só lugar
          </span>
        </h1>
        <p className="mt-3.5 text-base sm:text-lg text-slate-600 max-w-2xl mx-auto">
          Pesquise oportunidades simultaneamente na Gupy, Catho, Vagas.com, InfoJobs e mais. Rápido, direto ao ponto e sem cadastros.
        </p>
      </div>

      {/* Floating Search Bar */}
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-4xl bg-white p-2.5 sm:p-3 rounded-2xl shadow-xl shadow-slate-200/60 border border-slate-200/90 flex flex-col md:flex-row items-stretch gap-2.5 transition-all focus-within:ring-2 focus-within:ring-indigo-500/20 focus-within:border-indigo-500"
      >
        {/* Input: Termo/Cargo */}
        <div className="flex-1 flex items-center px-3.5 py-2.5 bg-slate-50/70 rounded-xl hover:bg-slate-50 transition-colors focus-within:bg-white focus-within:ring-1 focus-within:ring-indigo-400">
          <i className="bi bi-briefcase text-slate-400 text-lg me-3"></i>
          <input
            type="text"
            placeholder="Cargo, tecnologia ou área (ex: Frontend, TI, Design)"
            value={nomeVaga}
            onChange={(e) => setNomeVaga(e.target.value)}
            className="w-full bg-transparent text-slate-800 placeholder:text-slate-400 text-sm font-medium focus:outline-hidden"
          />
        </div>

        {/* Divider vertical no desktop */}
        <div className="hidden md:block w-px bg-slate-200 my-1 self-stretch"></div>

        {/* Input: Localidade */}
        <div className="flex-1 flex items-center px-3.5 py-2.5 bg-slate-50/70 rounded-xl hover:bg-slate-50 transition-colors focus-within:bg-white focus-within:ring-1 focus-within:ring-indigo-400">
          <i className="bi bi-geo-alt text-slate-400 text-lg me-3"></i>
          <input
            type="text"
            placeholder="Cidade, estado ou 'Remoto'"
            value={localidade}
            onChange={(e) => setLocalidade(e.target.value)}
            className="w-full bg-transparent text-slate-800 placeholder:text-slate-400 text-sm font-medium focus:outline-hidden"
          />
        </div>

        {/* Botão de Busca */}
        <button
          type="submit"
          className="bg-gradient-to-r from-indigo-600 via-indigo-600 to-violet-600 hover:from-indigo-700 hover:to-violet-700 text-white font-bold px-7 py-3 rounded-xl shadow-md shadow-indigo-500/20 hover:shadow-indigo-500/35 transition-all duration-200 flex items-center justify-center gap-2 cursor-pointer active:scale-98"
        >
          <i className="bi bi-search text-sm"></i>
          <span>Buscar Vagas</span>
        </button>
      </form>

      {/* Quick Filter Tags */}
      <div className="w-full max-w-4xl mt-4 flex flex-wrap items-center justify-center gap-1.5 sm:gap-2">
        <span className="text-xs font-semibold text-slate-600 me-1">Sugestões rápidas:</span>
        {QUICK_TAGS.map((tag) => (
          <button
            key={tag.label}
            type="button"
            onClick={() => handleQuickTag(tag.termo, tag.local)}
            className="text-xs font-medium bg-white hover:bg-indigo-50 text-slate-600 hover:text-indigo-600 px-3 py-1.5 rounded-full border border-slate-200 hover:border-indigo-200 shadow-2xs transition-all cursor-pointer"
          >
            {tag.label}
          </button>
        ))}
      </div>

      {/* Source Filters Bar */}
      <div className="w-full max-w-4xl mt-6 pt-5 border-t border-slate-200/70 flex flex-col items-center">
        <div className="flex items-center gap-2 mb-3">
          <i className="bi bi-funnel text-xs text-slate-400"></i>
          <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
            Filtrar por portal de estágio
          </span>
        </div>

        <div className="flex flex-wrap justify-center gap-2">
          <button
            type="button"
            onClick={() => setFonteSelecionada("")}
            className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
              fonteSelecionada === "" || fonteSelecionada === "Todas"
                ? "bg-indigo-600 text-white shadow-xs shadow-indigo-500/30 scale-102"
                : "bg-white text-slate-600 border border-slate-200 hover:bg-slate-50 hover:border-slate-300"
            }`}
          >
            Todas as Fontes
          </button>

          {fontes.map((fonte) => {
            const isSelected = fonteSelecionada === fonte;
            return (
              <button
                key={fonte}
                type="button"
                onClick={() => handleSelecionarFonte(fonte)}
                className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold transition-all cursor-pointer ${
                  isSelected
                    ? "bg-indigo-600 text-white shadow-xs shadow-indigo-500/30 scale-102"
                    : "bg-white text-slate-600 border border-slate-200 hover:bg-slate-50 hover:border-slate-300"
                }`}
              >
                {fonte}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default BuscaForm;
