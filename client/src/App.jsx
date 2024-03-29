import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Layout from "./Layout";
import RequireAuth from "./components/RequireAuth";
import Dashboard from "./pages/Dashboard/Dashboard";
import { LeftBar } from "./pages/LeftBar/LeftBar";

import { RightBar } from "./pages/RightBar/RightBar";
import { Register } from "./pages/Register/Register";
import { Login } from "./pages/login/Login";
import { Discover } from "./pages/Discover/Discover";
import "./index.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          {" "}
          {/* Layout as the parent route */}
          <Route element={<RequireAuth />}>
            {" "}
            {/* RequireAuth wraps around protected routes */}
            <Route index element={<Dashboard />} />{" "}
            {/* Use index for the default child route */}
            <Route path="messaging" element={<LeftBar />} />
            <Route path="discover" element={<Discover />} />
            <Route path="messages" element={<RightBar />} />
          </Route>
        </Route>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;
