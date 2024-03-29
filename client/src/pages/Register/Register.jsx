import { useRef, useState, useEffect } from "react";
import "./Register.css"; // Updated CSS file with unique class names
import axios from "../../api/axios";

import {
  faCheck,
  faTimes,
  faInfoCircle,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";

const USER_REGEX = /^[a-zA-Z][a-zA-Z0-9-_.]{3,23}$/;
const PWD_REGEX = /^(?=.*[0-9]).{8,24}$/;
const REGISTER_URL = "/register";

export const Register = () => {
  const userRef = useRef();
  const errRef = useRef();

  const [username, setUsername] = useState("");
  const [validUsername, setValidUsername] = useState(false);
  const [usernameFocus, setUsernameFocus] = useState(false);

  const [pwd, setPwd] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [matchPwd, setMatchPwd] = useState("");
  const [validMatch, setValidMatch] = useState(false);
  const [matchFocus, setMatchFocus] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    userRef.current.focus();
  }, []);

  useEffect(() => {
    setValidUsername(USER_REGEX.test(username));
  }, [username]);

  useEffect(() => {
    setValidPwd(PWD_REGEX.test(pwd));
    setValidMatch(pwd === matchPwd);
  }, [pwd, matchPwd]);

  useEffect(() => {
    setErrMsg("");
  }, [username, pwd, matchPwd]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const v1 = USER_REGEX.test(username);
    const v2 = PWD_REGEX.test(pwd);
    if (!v1 || !v2) {
      setErrMsg("Invalid Entry");
      return;
    }
    try {
      const response = await axios.post(
        REGISTER_URL,
        JSON.stringify({ username, pwd }),
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
      console.log(response.data);
      console.log(response.accsessToken);
      console.log(JSON.stringify(response));
      setSuccess(true);
    } catch (err) {
      if (!err?.response) {
        setErrMsg("No server Response");
      } else if (err.response?.status === 409) {
        setErrMsg("Username Taken");
      } else {
        setErrMsg("Registration Failed");
      }
      errRef.current.focus();
    }
  };

  return (
    <>
      {success ? (
        <div className="reg-container">
          <section className="reg-section">
            <h1>Success!</h1>
            <p>
              <Link className="signIn-link" to="/login">
                Sign In
              </Link>
            </p>
          </section>
        </div>
      ) : (
        <div className="reg-container">
          <section className="reg-section">
            <p
              ref={errRef}
              className={`reg-errmsg ${errMsg ? "" : "reg-offscreen"}`}
              aria-live="assertive"
            >
              {errMsg}
            </p>
            <h1 className="reg-title">Register</h1>
            <form onSubmit={handleSubmit} className="reg-form">
              {/* Username Input */}
              <label htmlFor="username" className="reg-label">
                Username:
                <span className={validUsername ? "reg-valid" : "reg-hide"}>
                  <FontAwesomeIcon icon={faCheck} />
                </span>
                <span
                  className={
                    validUsername || !username ? "reg-hide" : "reg-invalid"
                  }
                >
                  <FontAwesomeIcon icon={faTimes} />
                </span>
              </label>
              <input
                type="text"
                id="username"
                ref={userRef}
                autoComplete="off"
                className="reg-input"
                onChange={(e) => setUsername(e.target.value)}
                required
                aria-invalid={validUsername ? "false" : "true"}
                aria-describedby="uidnote"
                onFocus={() => setUsernameFocus(true)}
                onBlur={() => setUsernameFocus(false)}
              />
              <p
                id="uidnote"
                className={`reg-instructions ${
                  usernameFocus && username && !validUsername
                    ? ""
                    : "reg-offscreen"
                }`}
              >
                <FontAwesomeIcon icon={faInfoCircle} />4 to 24 characters. Must
                begin with a letter. Letters, numbers, underscores, hyphens
                allowed.
              </p>

              {/* Password Input */}
              <label htmlFor="password" className="reg-label">
                Password:
                <span className={validPwd ? "reg-valid" : "reg-hide"}>
                  <FontAwesomeIcon icon={faCheck} />
                </span>
                <span className={validPwd || !pwd ? "reg-hide" : "reg-invalid"}>
                  <FontAwesomeIcon icon={faTimes} />
                </span>
              </label>
              <input
                type="password"
                id="password"
                className="reg-input"
                onChange={(e) => setPwd(e.target.value)}
                required
                aria-invalid={validPwd ? "false" : "true"}
                aria-describedby="pwdnote"
                onFocus={() => setPwdFocus(true)}
                onBlur={() => setPwdFocus(false)}
              />
              <p
                id="pwdnote"
                className={`reg-instructions ${
                  pwdFocus && !validPwd ? "" : "reg-offscreen"
                }`}
              >
                <FontAwesomeIcon icon={faInfoCircle} />8 to 24 characters. Must
                include a number. Letters, numbers, underscores, hyphens
                allowed.
              </p>

              {/* Confirm Password Input */}
              <label htmlFor="confirm_pwd" className="reg-label">
                Confirm Password:
                <span
                  className={validMatch && matchPwd ? "reg-valid" : "reg-hide"}
                >
                  <FontAwesomeIcon icon={faCheck} />
                </span>
                <span
                  className={
                    validMatch || !matchPwd ? "reg-hide" : "reg-invalid"
                  }
                >
                  <FontAwesomeIcon icon={faTimes} />
                </span>
              </label>
              <input
                type="password"
                id="confirm_pwd"
                className="reg-input"
                onChange={(e) => setMatchPwd(e.target.value)}
                required
                aria-invalid={validMatch ? "false" : "true"}
                aria-describedby="confirmnote"
                onFocus={() => setMatchFocus(true)}
                onBlur={() => setMatchFocus(false)}
              />
              <p
                id="confirmnote"
                className={`reg-instructions ${
                  matchFocus && !validMatch ? "" : "reg-offscreen"
                }`}
              >
                <FontAwesomeIcon icon={faInfoCircle} />
                Must match the first password input field.
              </p>

              {/* Submit Button */}
              <button
                className="reg-submit"
                disabled={!validUsername || !validPwd || !validMatch}
              >
                Sign Up
              </button>
            </form>
            <p>
              Already registerd?
              <br />
              <span className="line">
                <Link className="signIn-link" to="/login">
                  Sign In
                </Link>
              </span>
            </p>
          </section>
        </div>
      )}
    </>
  );
};
