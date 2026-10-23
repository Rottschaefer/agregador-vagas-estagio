import useBuscaStore from "../store/BuscaStore";

const BuscaForm = () => {
  const nomeVaga = useBuscaStore((s) => s.nomeInput);
  const setNomeVaga = useBuscaStore((s) => s.setNomeInput);
  const localidade = useBuscaStore((s) => s.localInput);
  const setLocalidade = useBuscaStore((s) => s.setLocalInput);
  const fonteSelecionada = useBuscaStore((s) => s.fonteInput);
  const setFonteSelecionada = useBuscaStore((s) => s.setFonteInput);

  const handleBuscar = useBuscaStore((s) => s.buscar);

  const handleSelecionarFonte = (fonte: string) => {
    if (fonteSelecionada === fonte) {
      setFonteSelecionada("");
    } else {
      setFonteSelecionada(fonte);
    }
  };

  const fontes = [
    "Gupy",
    "RecrutaEasy",
    "Glassdoor",
    "InfoJobs",
    "EstagiarBR",
    "AcademiaUniversitario",
  ];

  const handleSubmit = (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    handleBuscar();
  };

  return (
    <>
      <h1 className="text-2xl ms-2 font-semibold text-teal-600">Busca</h1>
      <form
        onSubmit={handleSubmit}
        className="w-full flex flex-col md:flex-row gap-6"
      >
        <div className="flex-1 flex flex-col gap-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="relative flex-1">
              <input
                type="text"
                placeholder=" "
                value={nomeVaga}
                onChange={(e) => setNomeVaga(e.target.value)}
                className="peer w-full placeholder:text-slate-400 text-black text-sm border-2 border-teal-400 rounded-full px-3 py-2 transition duration-300 ease focus:outline-none focus:border-teal-500 hover:border-teal-500 shadow-sm focus:shadow"
              />
              <label
                className="absolute cursor-text bg-gray-50 px-1 left-2.5 top-2.5 text-slate-400 text-sm transition-all transform origin-left 
                                peer-focus:-top-2 peer-focus:left-2.5 peer-focus:text-xs peer-focus:text-teal-700 peer-focus:scale-90
                                peer-not-placeholder-shown:-top-2 peer-not-placeholder-shown:left-2.5 peer-not-placeholder-shown:text-xs peer-not-placeholder-shown:text-teal-700 peer-not-placeholder-shown:scale-90"
              >
                Nome da vaga
              </label>
            </div>

            <div className="relative flex-1">
              <input
                type="text"
                placeholder=" "
                value={localidade}
                onChange={(e) => setLocalidade(e.target.value)}
                className="peer w-full placeholder:text-slate-400 text-black text-sm border-2 border-teal-400 rounded-full px-3 py-2 transition duration-300 ease focus:outline-none focus:border-teal-500 hover:border-teal-500 shadow-sm focus:shadow"
              />
              <label
                className="absolute cursor-text bg-gray-50 px-1 left-2.5 top-2.5 text-slate-400 text-sm transition-all transform origin-left 
                                peer-focus:-top-2 peer-focus:left-2.5 peer-focus:text-xs peer-focus:text-teal-700 peer-focus:scale-90
                                peer-not-placeholder-shown:-top-2 peer-not-placeholder-shown:left-2.5 peer-not-placeholder-shown:text-xs peer-not-placeholder-shown:text-teal-700 peer-not-placeholder-shown:scale-90"
              >
                Localidade
              </label>
            </div>
          </div>

          <div className="flex flex-wrap gap-2">
            {fontes.map((fonte) => (
              <button
                key={fonte}
                type="button"
                onClick={() => handleSelecionarFonte(fonte)}
                className={`px-4 py-2 rounded-full text-sm font-semibold cursor-pointer
                                    ${
                                      fonteSelecionada === fonte
                                        ? "bg-purple-600 text-white"
                                        : "bg-gray-200 text-gray-600"
                                    }`}
              >
                {fonte}
              </button>
            ))}
          </div>
        </div>

        <div>
          <button
            type="submit"
            className="w-full md:w-44 bg-purple-500 hover:bg-purple-600 text-white font-bold rounded-full py-2 shadow-lg transition-all cursor-pointer"
          >
            Buscar
            <i className="bi bi-search ms-2.5" />
          </button>
        </div>
      </form>
    </>
  );
};
export default BuscaForm;
