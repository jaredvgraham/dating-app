import React, { createContext, useContext, useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import useAuth from "../hooks/useAuth";

import { useNotifications } from "./NotificationContext";

const WebSocketContext = createContext(null);

export const WebSocketProvider = ({ children }) => {
  const { auth } = useAuth();
  const { addNotification } = useNotifications();
  const [client, setClient] = useState(null);

  useEffect(() => {
    const serverUrl = "https://42f5e9aace27.ngrok.app/ws";
    const stompClient = Stomp.over(() => new SockJS(serverUrl));

    stompClient.connect(
      { Authorization: `Bearer ${auth.accessToken}` },
      () => {
        console.log("WebSocket Connected");

        // Subscriptions can be added here
        if (auth.userId) {
          stompClient.subscribe(
            `/topic/notifications/${auth.userId}`,
            (message) => {
              console.log("Notification was sent :", message.body);
              const notification = JSON.parse(message.body);
              addNotification(notification.msg);
            }
          );
        }

        setClient(stompClient); // Setting up client for global access
      },
      (error) => {
        console.error("Connection failed", error);
      }
    );

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, [auth.accessToken]);

  return (
    <WebSocketContext.Provider value={client}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => {
  return useContext(WebSocketContext);
};

export default WebSocketProvider;
