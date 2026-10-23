import { useQuery } from "@tanstack/react-query";
import { URL_BASE } from "../utils/constantes";

const fetchVagas = async (
  nome: string,
  local: string,
  fonte: string,
  pagina: number,
) => {
  const fonteParam = fonte === "Todas" ? "" : fonte.toLowerCase();

  const url = `${URL_BASE}/buscar-vagas?fonte=${fonteParam}&termo=${nome}&local=${local}&pagina=${pagina}`;

  const response = await fetch(url);

  if (!response.ok) {
    throw new Error("Erro ao buscar as vagas");
  }

  return response.json();
};

export function useBuscaVagas(
  nome: string,
  local: string,
  fonte: string,
  pagina: number,
) {
  return useQuery({
    queryKey: ["vagas", nome, local, fonte, pagina],
    queryFn: () => fetchVagas(nome, local, fonte, pagina),
  });
}
