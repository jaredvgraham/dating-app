import React, { useEffect } from "react";
import "./rightBar.css";

import { useLocation, useNavigate } from "react-router-dom";
import assets from "../../assets/assets";
import { useState } from "react";
import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";

import { matchWithessages } from "../../matchWithessages/matchWithessages";

export const RightBar = ({ onSelectMessage }) => {
  const { auth } = useAuth();
  const accessToken = auth.accessToken;
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();
  const isDashboard = location.pathname === "/";
  const rightBarClasses = isDashboard
    ? "m-right-bar"
    : "m-right-bar-non-dashboard";

  const matchesArray = [
    { id: 1, imageUrl: assets.wom6alt, name: "Jane" },
    { id: 2, imageUrl: assets.wom6alt, name: "Jane" },
    { id: 3, imageUrl: assets.wom6alt, name: "Jane" },
    { id: 4, imageUrl: assets.wom6alt, name: "Jane" },
    { id: 5, image: assets.wom6alt, name: "Jane" },
    { id: 6, image: assets.wom6alt, name: "Jane" },
    { id: 7, image: assets.wom6alt, name: "Jane" },
  ];
  const [matches, setMatches] = useState([]);

  useEffect(() => {
    const getMatches = async () => {
      try {
        const response = await axiosPrivate.get("/matches");

        const allMatches = response.data;
        console.log(response.data);
        setMatches(allMatches);
      } catch (error) {
        console.log("error getting matches", error);
      }
    };
    getMatches();
  }, []);

  const handleMsgClick = (msg) => {
    onSelectMessage(msg);
    navigate("/messaging", { state: { selectedMessage: msg } });
  };
  return (
    <div className={rightBarClasses}>
      {/* Matches section */}

      <div className="matches">
        <h2>Matches</h2>
        <div className="matches-grid">
          {matches.map((match) => (
            <div key={match.id} className="matches-box">
              <img
                className="match-pro-pic"
                src={match.image}
                alt={match.Firstame}
              />
              {/* You can include more info about the match here if needed */}
            </div>
          ))}
        </div>
      </div>

      {/* Messages section */}
      <div className="msgs">
        <h2>Messages</h2>
        <div className="msgs-grid">
          {matchWithessages.map((msg) => (
            <div
              key={msg.id}
              className="message-box"
              onClick={() => handleMsgClick(msg)}
            >
              <img className="msg-pro-pic" src={msg.image} alt={msg.name} />
              <div className="msg-info">
                <h4>{msg.name}</h4>
                <p>{msg.message}</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Non-dashboard view */}
      {!isDashboard && (
        <div className="non-dashboard-element">
          <img src={assets.rLogo} alt="" className="logo-non-dash" />
        </div>
      )}
    </div>
  );
};
