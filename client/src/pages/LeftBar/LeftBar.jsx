import React, { useEffect, useState, useRef, useCallback } from "react";
import { useLocation } from "react-router-dom";

import "./LeftBar.css";
import assets from "../../assets/assets";

import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";
import { useWebSocket } from "../../context/WebSocketContext";

export const LeftBar = ({ msg, match, message, showMatch }) => {
  const { auth } = useAuth();
  const axiosPrivate = useAxiosPrivate();
  const username1 = auth.username;

  const accessToken = auth.accessToken;

  const location = useLocation();

  const selectedMatch =
    showMatch || location.state?.match || location.state?.msg;
  const [messages, setMessages] = useState([]);
  const isDashboard = location.pathname === "/";
  const leftBarClasses = isDashboard ? "main-ctn" : "left-bar-non-dashboard";
  const msgFromRight = message;
  const [userMsg, setUserMsg] = useState("");
  const [isConnected, setIsConnected] = useState(false);

  const stompClient = useWebSocket();
  console.log(`this is sel match ${selectedMatch}`);
  console.log(`this is sel match ${selectedMatch}`);
  console.log(`this is sel match ${selectedMatch}`);
  console.log(`this is sel match ${selectedMatch}`);
  useEffect(() => {
    if (selectedMatch?.matchKey) {
      async function getMsgs() {
        try {
          const response = await axiosPrivate.get(
            `/conversation/${selectedMatch.matchKey}`
          );
          console.log(response.data); // Debugging output to check data structure
          const messages = response.data.map((item) => {
            // Assuming item contains all necessary fields directly
            return {
              sender:
                item.senderId === selectedMatch.matchedUserId
                  ? "match-msg"
                  : "users-msg",
              text: item.message.startsWith("{")
                ? JSON.parse(item.message).content
                : item.message,
              senderId: item.senderId,
              messageId: item.messageId, // Adjust according to your actual data property names
            };
          });

          if (messages.length > 0) {
            const lastMsg = messages[messages.length - 1];
            if (lastMsg.senderId === selectedMatch.matchedUserId) {
              markMessageAsRead(lastMsg.messageId);
            }
          }
          setMessages(messages);
        } catch (error) {
          console.error("Failed to fetch messages:", error);
        }
      }
      getMsgs();
    }
  }, [axiosPrivate, selectedMatch]);

  const handleMessageReceived = useCallback(
    (msgObj) => {
      console.log("Message received:", msgObj);
      if (!selectedMatch) return; // Ensure there is a context to update messages

      setMessages((prevMessages) => {
        const uniqueKey = `${msgObj.timestamp}-${msgObj.senderId}-${msgObj.message}`;

        const messageExists = prevMessages.some(
          (m) => m.uniqueKey === uniqueKey
        );
        if (messageExists) {
          return prevMessages;
        }

        const newMessage = {
          uniqueKey,
          sender:
            msgObj.senderId === selectedMatch.matchedUserId
              ? "match-msg"
              : "users-msg",
          text: msgObj.message,
          timestamp: msgObj.timestamp,
        };

        if (selectedMatch && msgObj.senderId !== selectedMatch.userId) {
          markMessageAsRead(msgObj.messageId);
        }

        return [...prevMessages, newMessage];
      });
    },
    [selectedMatch]
  );
  useEffect(() => {
    if (selectedMatch?.matchKey && stompClient && stompClient.connected) {
      const subscription = stompClient.subscribe(
        `/topic/chat/${selectedMatch.matchKey}`,
        (message) => {
          const msgObj = JSON.parse(message.body);
          handleMessageReceived(msgObj);
        }
      );

      return () => subscription.unsubscribe(); // Cleanup subscription
    }
  }, [selectedMatch?.matchKey, stompClient, handleMessageReceived]);

  const markMessageAsRead = async (messageId) => {
    /* const numericMessageId = Number(messageId); */
    console.log(`Attempting to mark message ${messageId} as read`);
    try {
      await axiosPrivate.post(`/read-message/${messageId}`);
      console.log("Successfully called read endpoint.");
    } catch (error) {
      console.error("Error marking message as read:", error);
    }
  };

  const textAreaRef = useRef(null);
  const messagesEndRef = useRef(null);

  const handleChange = (e) => setUserMsg(e.target.value);

  const handleSendMessage = () => {
    if (userMsg.trim() && stompClient && stompClient.connected) {
      stompClient.publish({
        destination: `/app/chat/${selectedMatch.matchKey}`,
        body: JSON.stringify({
          type: "CHAT_MESSAGE",
          username: auth.username,
          matchKey: selectedMatch.matchKey,
          content: userMsg,
        }),
      });
      setUserMsg("");
    } else {
      console.log("WebSocket is not connected.");
    }
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

  // return (
  //   <div className={leftBarClasses}>
  //     <div className="top">
  //       <img src={selectedMatch?.image} alt={selectedMatch?.name || "User"} />
  //       <h2>{selectedMatch?.firstname || "User Name"}</h2>
  //     </div>
  //     <div className="messages-ctn">
  //       {messages.map((message, index) => (
  //         <div key={message.uniqueKey} className={`${message.sender}-wrapper`}>
  //           <p className={message.sender}>{message.text}</p>
  //         </div>
  //       ))}
  //       <div ref={messagesEndRef} />
  //     </div>
  //     <div className="input-ctn">
  //       <div className="input">
  //         <textarea
  //           value={userMsg}
  //           ref={textAreaRef}
  //           onChange={handleChange}
  //           onKeyDown={handleKeyDown}
  //           rows="1"
  //           placeholder="Write a message..."
  //         ></textarea>
  //       </div>
  //       <img
  //         onClick={handleSendMessage}
  //         className="send-btn"
  //         src={assets.send}
  //         alt="Send"
  //       />
  //     </div>
  //     {!isDashboard && (
  //       <div className="non-dashboard-element">
  //         <img src={assets.rLogo} alt="" className="logo-non-dash" />
  //       </div>
  //     )}
  //   </div>
  // );

  return (
    <>
      {selectedMatch !== undefined ? (
        <div className={leftBarClasses}>
          <div className="top">
            <img
              src={selectedMatch?.image}
              alt={selectedMatch?.name || "User"}
            />
            <h2>{selectedMatch?.firstname || "User Name"}</h2>
          </div>
          <div className="messages-ctn">
            {messages.map((message, index) => (
              <div
                key={message.uniqueKey}
                className={`${message.sender}-wrapper`}
              >
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
      ) : (
        <div className={leftBarClasses}>
          <div className="top">
            <h1>No match</h1>
          </div>
          <div className="messages-ctn">
            {messages.map((message, index) => (
              <div
                key={message.uniqueKey}
                className={`${message.sender}-wrapper`}
              >
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
      )}
    </>
  );
};
