import "./Navbar.css";
import { Link } from "react-router-dom";
export const Navbar = () => {
  return (
    <div className="navbar-container">
      <nav>
        <ul>
          <li>
            <Link to="/">Dashboard</Link>
          </li>
          <li>
            <Link to="/discover">Discover</Link>
          </li>
          <li>
            <Link to="/messages">Messages</Link>
          </li>
          <li>
            <Link to="/messaging">Messaging</Link>
          </li>
        </ul>
      </nav>
    </div>
  );
};
