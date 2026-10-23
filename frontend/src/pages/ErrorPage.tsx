import Header from "../components/Header";

const ErrorPage = () => {
  return (
    <>
      <Header />
      <div className="ps-20">
        <h1 className="text-4xl font-semibold text-purple-500">
            Oops...
        </h1>
        <h2 className="text-2xl mt-5">
            Algo deu <span className="text-teal-600">errado!</span> 🫤 
        </h2>
      </div>
    </>
  );
};
export default ErrorPage;