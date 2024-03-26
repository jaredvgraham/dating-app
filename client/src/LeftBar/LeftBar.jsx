import React, { useEffect, useState, useRef } from "react";
import { useLocation } from "react-router-dom";
import assets from "../assets/assets.js";
import "./LeftBar.css";
import { matchWithessages } from "../matchWithessages/matchWithessages";

export const LeftBar = ({ selectedMessage }) => {
  const location = useLocation();

  const selectedMatch = location.state?.selectedMessage;
  const [messages, setMessages] = useState([]);

  // This useEffect is responsible for initializing messages
  // If viewing from the dashboard, we set messages to only include the most recent message
  useEffect(() => {
    // Use selectedMessage if available, otherwise default to the most recent message
    const messageToShow =
      selectedMessage || matchWithessages[matchWithessages.length - 1];
    setMessages([{ sender: "match-msg", text: messageToShow.message }]);
  }, [selectedMessage]);

  const [userMsg, setUserMsg] = useState("");
  const textAreaRef = useRef(null);
  const messagesEndRef = useRef(null);

  const handleChange = (e) => setUserMsg(e.target.value);

  const handleSendMessage = () => {
    if (!userMsg.trim()) return;
    const newMessage = { sender: "users-msg", text: userMsg };
    setMessages((prevMessages) => [...prevMessages, newMessage]);
    setUserMsg("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  useEffect(() => {
    textAreaRef.current.style.height = "auto";
    textAreaRef.current.style.height = `${textAreaRef.current.scrollHeight}px`;
  }, [userMsg]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const isDashboard = location.pathname === "/";
  const leftBarClasses = isDashboard ? "main-ctn" : "left-bar-non-dashboard";

  return (
    <div className={leftBarClasses}>
      <div className="top">
        <img
          src={selectedMatch?.image || assets.defaultUserImage}
          alt={selectedMatch?.name || "User"}
        />
        <h2>{selectedMatch?.name || "User Name"}</h2>
      </div>
      <div className="messages-ctn">
        {messages.map((message, index) => (
          <div key={index} className={`${message.sender}-wrapper`}>
            <p className={message.sender}>{message.text}</p>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>
      <div className="input-ctn">
        <div className="input">
          <textarea
            value={userMsg}
            ref={textAreaRef}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            rows="1"
            placeholder="Write a message..."
          ></textarea>
        </div>
        <img
          onClick={handleSendMessage}
          className="send-btn"
          src={assets.send}
          alt="Send"
        />
      </div>
      {!isDashboard && (
        <div className="non-dashboard-element">
          <img src={assets.rLogo} alt="" className="logo-non-dash" />
        </div>
      )}
    </div>
  );
};
