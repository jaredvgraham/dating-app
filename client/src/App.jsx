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
import CreateAccount from "./pages/CreateAccount/CreateAccount";
import { ProfileCreation } from "./pages/ProfileCreation/ProfileCreation";
import { IsUser } from "./pages/IsUser";
import { Profile } from "./pages/Profile/Profile";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          {/* Layout as the parent route */}
          <Route element={<RequireAuth />}>
            <Route index element={<Dashboard />} />{" "}
            {/* RequireAuth wraps around protected routes */}{" "}
            {/* Use index for the default child route */}
            <Route path="messaging" element={<LeftBar />} />
            <Route path="discover" element={<Discover />} />
            <Route path="messages" element={<RightBar />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="isUser" element={<IsUser />} />
            <Route path="/create-account" element={<CreateAccount />} />
            <Route path="/profile-creation" element={<ProfileCreation />} />
          </Route>
        </Route>

        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;
