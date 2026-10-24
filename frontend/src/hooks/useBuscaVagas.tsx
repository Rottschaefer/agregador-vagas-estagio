import { useQuery } from "@tanstack/react-query";
import type { Vaga } from "../types/vaga";
import { URL_BASE } from "../utils/constantes";

const fetchVagas = async (
  nome: string,
  local: string,
  fonte: string,
  pagina: number,
): Promise<Vaga[]> => {
  const fonteParam =
    !fonte || fonte.toLowerCase() === "todas" ? "" : fonte.toLowerCase();

  const params = new URLSearchParams({
    fonte: fonteParam,
    termo: nome || "",
    local: local || "",
    pagina: String(pagina || 1),
  });

  const url = `${URL_BASE}/buscar-vagas?${params.toString()}`;

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
  enabled: boolean = true,
) {
  return useQuery<Vaga[]>({
    queryKey: ["vagas", nome, local, fonte, pagina],
    queryFn: () => fetchVagas(nome, local, fonte, pagina),
    enabled,
  });
}
