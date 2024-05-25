import React, { useEffect } from "react";
import "./rightBar.css";

import { useLocation, useNavigate } from "react-router-dom";
import assets from "../../assets/assets";
import { useState } from "react";
import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";

export const RightBar = ({ onSelectMessage, setDefaultMatch }) => {
  const { auth } = useAuth();
  const userId = auth.userId;
  console.log(`got the userId here ${userId}`);
  const accessToken = auth.accessToken;
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();
  const isDashboard = location.pathname === "/";
  const rightBarClasses = isDashboard
    ? "m-right-bar"
    : "m-right-bar-non-dashboard";

  const [matches, setMatches] = useState([]);
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    const getMatches = async () => {
      try {
        const response = await axiosPrivate.get("/matches");
        console.log(response.data);
        const allMatches = response.data;
        const matchesWithTime = response.data.filter(
          (match) => match.message && match.message.timestamp
        );
        const reversedMatches = allMatches.reverse();
        const sortedMatchesByTimestamp = matchesWithTime.sort(
          (a, b) =>
            new Date(b.message.timestamp) - new Date(a.message.timestamp)
        );

        const matchesWithMessages = response.data.filter(
          (match) => match.message !== null
        );
        setMatches(reversedMatches);
        setMessages(sortedMatchesByTimestamp);
        if (sortedMatchesByTimestamp.length > 0) {
          setDefaultMatch(sortedMatchesByTimestamp[0]);
        } else {
          // Handle case where no matches have messages yet
          console.log("No matches with messages found.");
        } // Setting the default match for the dashboard
      } catch (error) {
        console.log("error getting matches", error);
      }
    };
    getMatches();
  }, []);

  const handleMsgClick = (msg) => {
    if (window.innerWidth < 1300) {
      navigate("/messaging", { state: { msg } });
    } else {
      setDefaultMatch(msg);
    }
  };

  const startConversation = (match) => {
    // Now using matchKey to start a conversation
    const matchKey = match.matchKey;
    console.log(`first name ${match.firstname}`);
    console.log(`this is the ${matchKey}`);

    navigate("/messaging", { state: { match } });
  };
  return (
    <div className={rightBarClasses}>
      {/* Matches section */}

      <div className="matches">
        <h2>Matches</h2>
        <div className="matches-grid">
          {matches.map((match) => (
            <div key={match.matchKey} className="matches-box">
              <img
                onClick={() => startConversation(match)}
                className="match-pro-pic"
                src={match.image}
                alt={match.firstname}
              />
              {/* You can include more info about the match here if needed */}
            </div>
          ))}
        </div>
        {matches.length === 0 && (
          <div className="matches-grid">
            <p className="no-matches">No matches yet</p>
            {/* You can include more info about the match here if needed */}
          </div>
        )}
      </div>

      {/* Messages section */}
      <div className="msgs">
        <h2>Messages</h2>
        <div className="msgs-grid">
          {messages.map((msg) => (
            <div
              key={msg.matchKey}
              className={`message-box ${
                msg.message.method === "received" ? "message-R" : ""
              }`}
              onClick={() => handleMsgClick(msg)}
            >
              {/* Unread indicator */}
              {msg.message.method === "received" &&
                msg.message.read === false && (
                  <div className="unread-indicator"></div>
                )}
              <img
                className="msg-pro-pic"
                src={msg.image}
                alt={msg.firstname}
              />
              <div className="msg-info">
                <h4>{msg.firstname}</h4>
                <p>{msg.message.content}</p>
              </div>
            </div>
          ))}
          {messages.length === 0 && <p>No messages yet</p>}
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
