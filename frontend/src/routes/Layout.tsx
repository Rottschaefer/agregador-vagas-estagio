import { Outlet } from "react-router-dom";
import Header from "../components/Header";

const Layout = () => {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-800 flex flex-col font-sans selection:bg-indigo-500 selection:text-white">
      <Header />
      <main className="flex-1">
        <Outlet />
      </main>
      <footer className="mt-16 border-t border-slate-200/80 bg-white py-8 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="font-medium text-slate-600">
            © {new Date().getFullYear()} <span className="text-indigo-600 font-semibold">Agrega Estágio</span> — Centralizando oportunidades para estudantes universitários.
          </p>
          <div className="flex items-center gap-4 text-slate-400">
            <span className="inline-flex items-center gap-1.5">
              <i className="bi bi-shield-check text-emerald-500"></i>
              Open Source & Gratuito
            </span>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
