import BuscaForm from "../components/BuscaForm";
import ListaVagas from "../components/ListaVagas";

const HomePage = () => {
  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12 flex flex-col gap-10">
      <BuscaForm />
      <ListaVagas />
    </div>
  );
};

export default HomePage;
