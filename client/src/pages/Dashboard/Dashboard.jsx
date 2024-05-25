// Dashboard.js
import React from "react";
import { Discover } from "../Discover/Discover";
import { LeftBar } from "../LeftBar/LeftBar";
import { RightBar } from "../RightBar/RightBar";
import "../RightBar/rightBar.css";
import "../LeftBar/LeftBar.css";
import "../../index.css";

import { matchWithessages } from "../../matchWithessages/matchWithessages";
import { useState } from "react";

const Dashboard = () => {
  const [defaultMatch, setDefaultMatch] = useState(null);
  console.log(`thi is the default match ${JSON.stringify(defaultMatch)}`);
  return (
    <>
      <LeftBar showMatch={defaultMatch} />
      <Discover />
      <RightBar setDefaultMatch={setDefaultMatch} />{" "}
      {/* Make sure you're passing the function here */}
    </>
  );
};
export default Dashboard;
