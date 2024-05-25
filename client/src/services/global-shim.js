if (typeof global === "undefined") {
  window.global = window;
}
/* if (matchKey && handleMessageReceived) {
  stompClient.subscribe(`/topic/chat/${matchKey}`, (message) => {
    if (typeof handleMessageReceived === "function") {
      handleMessageReceived(JSON.parse(message.body));
    } else {
      console.error(
        "handleMessageReceived is not a function",
        handleMessageReceived
      );
    }
  });
}
if (user) {
  stompClient.subscribe(`/topic/notifications/${userId}`, (message) => {
    try {
      const matchData = JSON.parse(message.body);
      matchHandler(matchData);
    } catch (error) {
      console.error("Failed to parse match notification:", error);
    }
  }); */
