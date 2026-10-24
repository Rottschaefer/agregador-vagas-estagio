import { useQuery } from "@tanstack/react-query";
import { URL_BASE } from "../utils/constantes";

const fetchFontes = async (): Promise<string[]> => {
  const response = await fetch(`${URL_BASE}/buscar-vagas/fontes`);

  if (!response.ok) {
    throw new Error("Erro ao buscar as fontes de vagas");
  }

  return response.json();
};

export function useFontes() {
  return useQuery({
    queryKey: ["fontes"],
    queryFn: fetchFontes,
    staleTime: 1000 * 60 * 60, // 1 hora de cache
  });
}
