import { useRef, useState, useEffect } from "react";
import useAuth from "../../hooks/useAuth";
import { Link, useNavigate, useLocation } from "react-router-dom";
import ".././Register/Register.css"; // Reuse the Register component's styles
import axios, { axiosPrivate } from "../../api/axios";
import { useGeoLocation } from "../../hooks/useGeoLocation";

// Placeholder URL for the login endpoint
const LOGIN_URL = "/login";

export const Login = () => {
  const { locationError, locationInfo } = useGeoLocation();
  console.log(locationError, locationInfo);
  const { setAuth } = useAuth();
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();
  const location = useLocation();

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
        JSON.stringify({ username, password: pwd, location: locationInfo }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      console.log(JSON.stringify(response?.data));
      const accessToken = response?.data?.accessToken;
      const refreshToken = response?.data?.refreshToken;
      console.log(`this is ${refreshToken}`);
      console.log(`my ${accessToken}`);
      console.log(
        `this is the location: ${JSON.stringify(locationInfo, null, 2)}`
      );

      /* const roles = response?.data?.roles */ //this might be a thing or not <========
      setAuth({ username, pwd, accessToken, isAuthenticated: true });
      setUsername("");
      setPwd("");

      /* navigate("/create-account"); */
      // Example: console.log(response);
      // Save the token, navigate to the dashboard or do something after successful login
      navigate(`/isUser`); // Adjust the navigation URL as needed
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
      errRef?.current?.focus();
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
            <label htmlFor="username" className="reg-label"></label>
            <input
              type="text"
              id="username"
              placeholder="Username"
              ref={userRef}
              autoComplete="off"
              className="reg-input"
              onChange={(e) => setUsername(e.target.value)}
              value={username}
              required
            />

            <label htmlFor="password" className="reg-label"></label>
            <input
              type="password"
              id="password"
              placeholder="Password"
              className="reg-input"
              onChange={(e) => setPwd(e.target.value)}
              value={pwd}
              required
            />

            <button disabled={!username || !pwd} className="reg-submit">
              Sign In
            </button>
          </form>
          <p className="already-reg">
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
