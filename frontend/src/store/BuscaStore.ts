import { create } from "zustand";

interface BuscaStore {
  nomeInput: string;
  nomePesquisa: string;
  localInput: string;
  localPesquisa: string;
  fonteInput: string;
  fontePesquisa: string;
  paginaAtual: number;

  setNomeInput: (novoNomeInput: string) => void;
  setNomePesquisa: (novoNomePesquisa: string) => void;
  setLocalInput: (novoLocalInput: string) => void;
  setLocalPesquisa: (novoLocalPesquisa: string) => void;
  setFonteInput: (novoFonteInput: string) => void;
  setFontePesquisa: (novoFontePesquisa: string) => void;
  setPaginaAtual: (novaPaginaAtual: number) => void;

  buscar: () => void;
}

const useBuscaStore = create<BuscaStore>((set, get) => ({
  nomeInput: "",
  nomePesquisa: "",
  localInput: "",
  localPesquisa: "",
  fonteInput: "Gupy",
  fontePesquisa: "Gupy",
  paginaAtual: 1,

  setNomeInput: (novoNomeInput: string) =>
    set(() => ({ nomeInput: novoNomeInput })),
  setNomePesquisa: (novoNomePesquisa: string) =>
    set(() => ({ nomePesquisa: novoNomePesquisa })),
  setLocalInput: (novoLocalInput: string) =>
    set(() => ({ localInput: novoLocalInput })),
  setLocalPesquisa: (novoLocalPesquisa: string) =>
    set(() => ({ localPesquisa: novoLocalPesquisa })),
  setFonteInput: (novoFonteInput: string) =>
    set(() => ({ fonteInput: novoFonteInput })),
  setFontePesquisa: (novoFontePesquisa: string) =>
    set(() => ({ fontePesquisa: novoFontePesquisa })),
  setPaginaAtual: (novaPaginaAtual: number) =>
    set(() => ({ paginaAtual: novaPaginaAtual })),
  buscar: () =>
    set({
      nomePesquisa: get().nomeInput,
      localPesquisa: get().localInput,
      fontePesquisa: get().fonteInput,
      paginaAtual: 1,
    }),
}));

export default useBuscaStore;
