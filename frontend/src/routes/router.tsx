import { createBrowserRouter } from "react-router-dom";
import BuscaVagas from "../pages/HomePage";
import Layout from "./Layout";
import ErrorPage from "../pages/ErrorPage";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            { index: true, element: <BuscaVagas /> },
        ]
    }
]);
export default router;
