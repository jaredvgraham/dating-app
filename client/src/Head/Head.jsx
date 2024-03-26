import React from "react";
import assets from "../assets/assets.js";
import "./Head.css";
export const Head = () => {
  return (
    <div className="logo-container">
      <img src={assets.rLogo} alt="" />
    </div>
  );
};
