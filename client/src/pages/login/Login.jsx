import { useRef, useState, useEffect } from "react";
import useAuth from "../../hooks/useAuth";
import { Link, useNavigate, useLocation } from "react-router-dom";
import ".././Register/Register.css"; // Reuse the Register component's styles
import axios from "../../api/axios";

// Placeholder URL for the login endpoint
const LOGIN_URL = "/auth";

export const Login = () => {
  const { setAuth } = useAuth();
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  const [username, setUsername] = useState("");
  const [pwd, setPwd] = useState("");
  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    userRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg("");
  }, [username, pwd]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        LOGIN_URL,
        JSON.stringify({ username, pwd }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      console.log(JSON.stringify(response?.data));
      const accsessToken = response?.data?.accsessToken;
      /* const roles = response?.data?.roles */ //this might be a thing or not <========
      setAuth({ username, pwd, accsessToken });
      setUsername("");
      setPwd("");

      // Example: console.log(response);
      // Save the token, navigate to the dashboard or do something after successful login
      navigate(from, { replace: true }); // Adjust the navigation URL as needed
    } catch (err) {
      if (!err?.response) {
        setErrMsg("No Server Response");
      } else if (err.response?.status === 400) {
        setErrMsg("Missing Username or Password");
      } else if (err.response?.status === 401) {
        setErrMsg("Unauthorized");
      } else {
        setErrMsg("Login Failed");
      }
      errRef.current.focus();
    }
  };

  return (
    <>
      {errMsg && (
        <p ref={errRef} className="reg-errmsg" aria-live="assertive">
          {errMsg}
        </p>
      )}
      <div className="reg-container">
        <section className="reg-section">
          <h1 className="reg-title">Sign In</h1>
          <form onSubmit={handleSubmit} className="reg-form">
            <label htmlFor="username" className="reg-label">
              Username:
            </label>
            <input
              type="text"
              id="username"
              ref={userRef}
              autoComplete="off"
              className="reg-input"
              onChange={(e) => setUsername(e.target.value)}
              value={username}
              required
            />

            <label htmlFor="password" className="reg-label">
              Password:
            </label>
            <input
              type="password"
              id="password"
              className="reg-input"
              onChange={(e) => setPwd(e.target.value)}
              value={pwd}
              required
            />

            <button disabled={!username || !pwd} className="reg-submit">
              Sign In
            </button>
          </form>
          <p>
            Need an account?
            <br />
            <span className="line">
              <Link className="signIn-link" to="/register">
                Sign Up
              </Link>
            </span>
          </p>
        </section>
      </div>
    </>
  );
};
