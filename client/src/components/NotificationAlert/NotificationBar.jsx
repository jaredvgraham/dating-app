import React from "react";
import { useNotifications } from "../../context/NotificationContext";
import "./NotificationBar.css";

const NotificationBar = () => {
  const { notifications, removeNotification } = useNotifications();

  return (
    <div className="notification-bar">
      {console.log("Rendering notifications:", notifications)}
      {notifications.map((notification) => (
        <div
          key={notification.id}
          className="notification"
          onClick={() => removeNotification(notification.id)}
        >
          {notification.message}
        </div>
      ))}
    </div>
  );
};

export default NotificationBar;
