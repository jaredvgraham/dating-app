// Dashboard.js
import React from "react";
import { Discover } from "../Discover/Discover";
import { LeftBar } from "../LeftBar/LeftBar";
import { RightBar } from "../RightBar/RightBar";
import { matchWithessages } from "../matchWithessages/matchWithessages";
import { useState } from "react";

export const Dashboard = () => {
  const [selectedMessage, setSelectedMessage] = useState(
    matchWithessages[matchWithessages.length - 1] // The initial selected message
  );

  // This function updates the selected message
  const handleSelectMessage = (message) => {
    setSelectedMessage(message);
  };

  return (
    <>
      <LeftBar selectedMessage={selectedMessage} />
      <Discover />
      <RightBar onSelectMessage={handleSelectMessage} />{" "}
      {/* Make sure you're passing the function here */}
    </>
  );
};
