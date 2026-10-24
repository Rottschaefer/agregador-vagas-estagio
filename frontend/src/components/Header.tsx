import { NavLink } from "react-router-dom";
import "bootstrap-icons/font/bootstrap-icons.min.css";

const Header = () => {
  return (
    <header className="sticky top-0 z-50 bg-white/90 backdrop-blur-md border-b border-slate-200/80 shadow-xs transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <NavLink to="/" className="flex items-center gap-3 group">
          <div className="relative flex items-center justify-center p-1.5 rounded-xl bg-gradient-to-tr from-indigo-600 to-violet-500 shadow-sm group-hover:scale-105 transition-transform">
            <i className="bi bi-mortarboard-fill text-white text-xl px-1"></i>
          </div>
          <div className="flex flex-col">
            <span className="font-extrabold text-lg sm:text-xl tracking-tight text-slate-900 group-hover:text-indigo-600 transition-colors">
              Agrega<span className="text-indigo-600">Estágio</span>
            </span>
            <span className="text-[10px] font-medium text-slate-400 uppercase tracking-wider -mt-1 hidden sm:inline">
              Hub de Vagas Universitárias
            </span>
          </div>
        </NavLink>

        <div className="flex items-center gap-3 sm:gap-4">
          <div className="hidden sm:flex items-center gap-2 bg-emerald-50 border border-emerald-200 text-emerald-700 text-xs font-semibold px-3 py-1.5 rounded-full shadow-2xs">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
            </span>
            <span>8 Fontes Integradas</span>
          </div>

          <a
            href="https://github.com/Rottschaefer/agregador-vagas-estagio"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center gap-2 text-slate-600 hover:text-indigo-600 bg-slate-100 hover:bg-indigo-50 px-3.5 py-1.5 rounded-full text-xs font-semibold border border-slate-200 hover:border-indigo-200 transition-all"
            title="Ver no GitHub"
          >
            <i className="bi bi-github text-sm"></i>
            <span className="hidden md:inline">GitHub</span>
          </a>
        </div>
      </div>
    </header>
  );
};

export default Header;
