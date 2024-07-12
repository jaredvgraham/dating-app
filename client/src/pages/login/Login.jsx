import { useRef, useState, useEffect, useContext } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import ".././Register/Register.css"; // Reuse the Register component's styles
import axios from "../../api/axios";
import { useGeoLocation } from "../../hooks/useGeoLocation";
import { AuthContext } from "../../AuthProvider";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../../firebase"; // Adjust the import based on your file structure

// Placeholder URL for the login endpoint
const LOGIN_URL = "/login";

export const Login = () => {
  const { locationError, locationInfo } = useGeoLocation();
  console.log(locationError, locationInfo);
  const { setAuth } = useContext(AuthContext);
  const userRef = useRef();
  const errRef = useRef();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState("");
  const [pwd, setPwd] = useState("");
  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    userRef.current.focus();
  }, []);

  useEffect(() => {
    setErrMsg("");
  }, [email, pwd]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, pwd);
      const user = userCredential.user;
      const accessToken = await user.getIdToken();

      const response = await axios.post(
        LOGIN_URL,
        JSON.stringify({ email, location: locationInfo }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );

      const { userId, username } = response.data;

      setAuth({
        username: username,
        email: email,
        accessToken,
        userId: userId,
        isAuthenticated: true,
      });

      setEmail("");
      setPwd("");

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
            <label htmlFor="email" className="reg-label"></label>
            <input
              type="email"
              id="email"
              placeholder="Email"
              ref={userRef}
              autoComplete="off"
              className="reg-input"
              onChange={(e) => setEmail(e.target.value)}
              value={email}
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

            <button disabled={!email || !pwd} className="reg-submit">
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
