import { Outlet } from "react-router-dom"
import Header from "../components/Header"

const Layout = () => {
  return (
    <div className="bg-gray-50">
      <Header />
      <div>
        <Outlet />
      </div>
    </div>
  )
}
export default Layout