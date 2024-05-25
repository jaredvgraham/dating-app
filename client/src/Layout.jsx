// Layout.js
import { Outlet } from "react-router-dom";
import Navbar from "./components/Navbar/Navbar";
import "./index.css";
import "./pages/RightBar/rightBar.css";
import "./pages/LeftBar/LeftBar.css";
import "./pages/Discover/Discover.css";
import useAuth from "./hooks/useAuth";
import WebSocketProvider from "./context/WebSocketContext";
import { useCookies } from "react-cookie";
import Cookies from "./components/cookies/Cookies";
import { NotificationProvider } from "./context/NotificationContext";
import NotificationBar from "./components/NotificationAlert/NotificationBar";

const Layout = () => {
  const { auth } = useAuth();
  const [cookies] = useCookies(["cookieConsent"]);
  return (
    <>
      {!cookies.cookieConsent && <Cookies />}
      {auth.profileExists && <Navbar />}
      <NotificationProvider>
        <WebSocketProvider>
          <NotificationBar />
          <Outlet /> {/* Child routes will be rendered here */}
        </WebSocketProvider>
      </NotificationProvider>
    </>
  );
};

export default Layout;
