import React from "react";
import { useCookies } from "react-cookie"; // Corrected import
import "./Cookies.css";
const Cookies = () => {
  const [cookies, setCookie] = useCookies(["cookieConsent"]);
  const giveCookieConsent = () => {
    setCookie("cookieConsent", true, { secure: true, sameSite: "none" }); // Set path if necessary
  };

  return (
    <div className="cookie-ctn">
      <p>
        We use cookies to enhance your user experience. By clicking allow, you
        consent to our use of cookies for this purpose. Thank you!
      </p>
      <button onClick={giveCookieConsent}>Allow</button>
    </div>
  );
};

export default Cookies;
