import React, { useState, useEffect } from "react";

const SwipeableCard = ({ children, onSwipe, style = {} }) => {
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [translation, setTranslation] = useState(0);
  const [isSwiping, setIsSwiping] = useState(false);

  // Effect for handling the end of the transition animation
  useEffect(() => {
    const swipeableElement = document.querySelector(".swipeAble");
    const handleTransitionEnd = () => {
      // Reset state and signal parent component that swipe and animation are complete
      setIsSwiping(false);
      setTranslation(0); // Ensure the card is reset to the center
    };

    swipeableElement.addEventListener("transitionend", handleTransitionEnd);
    return () =>
      swipeableElement.removeEventListener(
        "transitionend",
        handleTransitionEnd
      );
  }, []);

  const initiateSwipe = (direction) => {
    setIsSwiping(true); // Begin swipe transition
    const translateValue =
      direction === "right" ? window.innerWidth : -window.innerWidth;
    setTranslation(translateValue); // Move card out of view
    onSwipe(direction); // Notify parent component of the swipe direction
  };

  const onDragStart = (clientX) => {
    if (isSwiping) return; // Ignore new drags while swiping/transitioning
    setIsDragging(true);
    setStartX(clientX);
  };

  const onDragMove = (clientX) => {
    if (!isDragging || isSwiping) return;
    const currentTranslation = clientX - startX;
    setTranslation(currentTranslation);
  };

  const onDragEnd = () => {
    setIsDragging(false);
    if (Math.abs(translation) > window.innerWidth * 0.25) {
      // Swipe threshold (25% of the screen width)
      initiateSwipe(translation > 0 ? "right" : "left");
    } else {
      setTranslation(0); // Reset card position if not swiped enough
    }
  };

  // Handling touch and mouse events
  const onTouchStart = (e) => onDragStart(e.touches[0].clientX);
  const onTouchMove = (e) => onDragMove(e.touches[0].clientX);
  const onTouchEnd = onDragEnd;
  const onMouseDown = (e) => onDragStart(e.clientX);
  const onMouseMove = (e) => onDragMove(e.clientX);
  const onMouseUp = onDragEnd;
  const onMouseLeave = onDragEnd;

  return (
    <div
      className="swipeAble"
      style={{
        transform: `translateX(${translation}px)`,
        transition: isDragging ? "none" : "transform 1s ease-out",
        cursor: isDragging ? "grabbing" : "grab",
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
      {children}
    </div>
  );
};

export default SwipeableCard;
