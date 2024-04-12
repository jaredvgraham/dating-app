import React, { useState } from "react";
import SwipeableCard from "./SwipeableCard";

export const CardStack = ({ profiles, onSwipe, currentImageIndex }) => {
  /* const [cards, setCards] = useState(data); */
  const [currentProfiles, setCurrentProfiles] = useState(profiles);

  const handleSwipe = (direction, userId) => {
    onSwipe(direction, userId); // Pass userId to the onSwipe function
    setCurrentProfiles((prev) =>
      prev.filter((profile) => profile.userId !== userId)
    );
  };

  return (
    <>
      <div
        style={{
          position: "relative",
          width: "100%",
          height: "100%",

          overflow: "hidden",
        }}
      >
        {currentProfiles.map((profile, index) => (
          <SwipeableCard
            key={profile.userId}
            profile={profile}
            onSwipe={(direction) => handleSwipe(direction, profile.userId)}
            currentImage={profile.images[currentImageIndex]}
            style={{
              position: "absolute",
              top: 0, // Align all cards to the top of the container
              left: 0, // Align all cards to the left of the container
              right: 0, // Stretch card to fill container width
              bottom: 0, // Stretch card to fill container height
              zIndex: currentProfiles.length - index, // Ensure cards are stacked with the first card on top
              opacity: 1 - index * 0.1, // Optional: Reduce opacity of cards below the top card
            }}
          >
            {/* Render profile information */}
            <div
              style={{
                padding: "20px",
                border: "1px solid #ccc",
                backgroundColor: "#fff",
                height: "100%",
                boxSizing: "border-box",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
              }}
            ></div>
          </SwipeableCard>
        ))}
      </div>

      {/* <div
        style={{
          position: "relative",
          width: "100%",
          height: "100%",

          overflow: "hidden",
        }}
      >
        {cards.map((card, index) => (
          <SwipeableCard
            key={card.id}
            onSwipe={(direction) => handleSwipe(direction, profile.id)}
            style={{
              position: "absolute",
              top: 0, // Align all cards to the top of the container
              left: 0, // Align all cards to the left of the container
              right: 0, // Stretch card to fill container width
              bottom: 0, // Stretch card to fill container height
              zIndex: cards.length - index, // Ensure cards are stacked with the first card on top
              opacity: 1 - index * 0.1, // Optional: Reduce opacity of cards below the top card
            }}
          >
            <div
              style={{
                padding: "20px",
                border: "1px solid #ccc",
                backgroundColor: "#fff",
                height: "100%",
                boxSizing: "border-box",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              {card.content}
              <div className="large-screen-ctn">
                <div className="person-info">
                  <h2>Name</h2>
                  <p>10 miles away</p>
                </div>
              </div>
            </div>
          </SwipeableCard>
        ))}
      </div> */}
    </>
  );
};
