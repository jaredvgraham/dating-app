import React, { useState } from "react";

const SwipeableCard = ({ profile, onSwipe, currentImage, style = {} }) => {
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [translation, setTranslation] = useState(0);

  const onDragStart = (clientX) => {
    setIsDragging(true);
    setStartX(clientX);
  };

  const onDragMove = (clientX) => {
    if (isDragging) {
      const currentTranslation = clientX - startX;
      setTranslation(currentTranslation);
    }
  };

  const onDragEnd = () => {
    setIsDragging(false);
    // Threshold for considering it a swipe
    if (Math.abs(translation) > 100) {
      onSwipe(translation > 0 ? "right" : "left");
    }
    setTranslation(0);
  };

  // Touch Events
  const onTouchStart = (e) => onDragStart(e.touches[0].clientX);
  const onTouchMove = (e) => onDragMove(e.touches[0].clientX);
  const onTouchEnd = () => onDragEnd();

  // Mouse Events
  const onMouseDown = (e) => onDragStart(e.clientX);
  const onMouseMove = (e) => {
    if (isDragging) {
      onDragMove(e.clientX);
    }
  };
  const onMouseUp = () => onDragEnd();
  const onMouseLeave = () => {
    if (isDragging) {
      onDragEnd();
    }
  };

  return (
    <div
      style={{
        transform: `translateX(${translation}px)`,
        transition: isDragging ? "none" : "transform 300ms ease-out",
        cursor: "grab",
        position: "absolute",
        width: "100%",
        height: "100%",

        ...style,
      }}
      onTouchStart={onTouchStart}
      onTouchMove={onTouchMove}
      onTouchEnd={onTouchEnd}
      onMouseDown={onMouseDown}
      onMouseMove={onMouseMove}
      onMouseUp={onMouseUp}
      onMouseLeave={onMouseLeave}
    >
      <div className="img-slider">
        <img
          src={currentImage?.imageUrl || "/default-profile.jpg"}
          alt={profile.firstName}
          className={`slide active`}
        />
      </div>
      <div className="large-screen-ctn">
        <div className="person-info">
          <h2>{profile.firstName}</h2>
          <h2>{profile.distance} miles away</h2>
        </div>
      </div>
    </div>
  );
};

export default SwipeableCard;
