import { NavLink } from "react-router-dom"
import "bootstrap-icons/font/bootstrap-icons.min.css"

const Header = () => {
  return (
    <header className="bg-purple-600 w-full py-3 text-center shadow-sm mb-8 flex items-center">
      <NavLink to="/">
        <img src="../public/logo_no_bg.png" width="150px" height="150px" alt="Agrega Estágio"></img>
      </NavLink>

      {/* <NavLink to="/" className="text-white text-base font-medium">
        <i className="bi bi-search me-2" />
        Busca
      </NavLink> */}
    </header>
  )
}
export default Header