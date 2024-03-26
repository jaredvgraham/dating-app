import { Dashboard } from "./Dashboard/Dashboard";
import { Discover } from "./Discover/Discover";
import { Head } from "./Head/Head";
import { LeftBar } from "./LeftBar/LeftBar";
import { Navbar } from "./Navbar/Navbar";
import { RightBar } from "./RightBar/RightBar";
import { Register } from "./Register/Register";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./index.css";

function App() {
  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/messaging" element={<LeftBar />} />
          <Route path="/discover" element={<Discover />} />
          <Route path="/messages" element={<RightBar />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </Router>

      {/*  <Dashboard />
      <Navbar /> */}
    </>
  );
}

export default App;
