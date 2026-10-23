import { useBuscaVagas } from "../hooks/useBuscaVagas";
import useBuscaStore from "../store/BuscaStore";

const ListaVagas = () => {
  const nomeVaga = useBuscaStore((s) => s.nomePesquisa);
  const localidade = useBuscaStore((s) => s.localPesquisa);
  const fonteSelecionada = useBuscaStore((s) => s.fontePesquisa);
  const paginaAtual = useBuscaStore((s) => s.paginaAtual);
  const setPaginaAtual = useBuscaStore((s) => s.setPaginaAtual);

  const {
    data: vagas,
    isFetching: isFetching,
    error: error,
  } = useBuscaVagas(nomeVaga, localidade, fonteSelecionada, paginaAtual);

  if (isFetching)
    return (
      <div className="flex items-center">
        <div
          className="animate-spin rounded-full border-2 border-purple-600 border-t-transparent"
          style={{
            width: 15,
            height: 15,
          }}
        />
        <p className="text-center text-base font-thin md:text-start md:ms-2">
          Buscando...
        </p>
      </div>
    );

  {
    /* Exibição dos Resultados da API */
  }
  return (
    <div className="w-full px-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3  gap-6 mt-8 pb-8">
      {error && (
        <>
          <div className="">
            <p className="text-2xl font-semibold text-purple-500">Oops...</p>
            <p className="text-xl mt-5">
              Algo deu <span className="text-teal-600">errado!</span> 🫤
            </p>
            <p className="mt-3">Erro: {error.message}</p>
          </div>
        </>
      )}

      {!error &&
        vagas?.map((vaga: any) => (
          <a href={vaga.link} target="_blank" className="block hover:scale-102">
            <div
              key={vaga.id}
              className="bg-gray-100 rounded-3xl p-6 text-purple-600 shadow-md h-full"
            >
              <div className="flex justify-between items-start">
                <h2 className="text-xl font-bold">{vaga.titulo}</h2>

                <p className="text-gray-100 text-xs font-bold uppercase tracking-widest bg-teal-400/55 px-1.5 rounded-2xl ms-3 me-1 mt-2 text-center">
                  {vaga.fonte}
                </p>
              </div>

              <p className="text-gray-600 font-medium mt-1">{vaga.empresa}</p>
              <p className="text-gray-600">{vaga.localizacao}</p>
            </div>
          </a>
        ))}

      {vagas?.length === 0 && (
        <p className="text-gray-500 text-center">
          Nenhuma vaga encontrada para essa busca.
        </p>
      )}
      {/* Container de Paginação */}
      {vagas &&
        vagas.length > 0 &&
        (fonteSelecionada == "Gupy" || fonteSelecionada == "RecrutaEasy") && (
          <div className="col-span-full flex justify-center items-center gap-4 mt-10">
            <button
              onClick={() => setPaginaAtual(paginaAtual - 1)}
              disabled={paginaAtual === 1}
              className="px-6 py-2 rounded-full font-semibold cursor-pointer bg-gray-200 text-purple-600 hover:bg-purple-600 hover:text-white transition-all duration-300 disabled:opacity-50 disabled:hover:bg-gray-200 disabled:hover:text-purple-600 disabled:cursor-not-allowed shadow-sm flex items-center gap-2"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2.5}
                stroke="currentColor"
                className="w-4 h-4"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M15.75 19.5 8.25 12l7.5-7.5"
                />
              </svg>
              Anterior
            </button>

            <span className="text-gray-500 font-medium text-sm">
              Página {paginaAtual}
            </span>

            <button
              onClick={() => setPaginaAtual(paginaAtual + 1)}
              disabled={isFetching || vagas.length === 0}
              className="px-6 py-2 rounded-full font-semibold cursor-pointer bg-purple-600 text-white hover:bg-purple-700 transition-all duration-300 shadow-sm disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
            >
              Próxima
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2.5}
                stroke="currentColor"
                className="w-4 h-4"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M8.25 4.5l7.5 7.5-7.5 7.5"
                />
              </svg>
            </button>
          </div>
        )}
    </div>
  );
};
export default ListaVagas;
