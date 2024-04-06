// Layout.js
import { Outlet } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar";
import "./index.css";
import "./pages/RightBar/rightBar.css";
import "./pages/LeftBar/LeftBar.css";
import "./pages/Discover/Discover.css";
import useAuth from "./hooks/useAuth";

const Layout = () => {
  const { auth } = useAuth();
  return (
    <>
      {auth.profileExists && <Navbar />}
      <Outlet /> {/* Child routes will be rendered here */}
    </>
  );
};

export default Layout;
