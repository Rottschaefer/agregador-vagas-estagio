import BuscaForm from "../components/BuscaForm";
import ListaVagas from "../components/ListaVagas";

const HomePage = () => {
  return (
    <div className="min-h-screen bg-gray-50 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col gap-3">
      <BuscaForm />
      <ListaVagas />
    </div>
  );
};

export default HomePage;
