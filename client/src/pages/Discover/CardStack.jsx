// import React, {
//   useState,
//   memo,
//   useCallback,
//   useMemo,
//   useEffect,
//   forwardRef,
//   useRef,
//   useImperativeHandle,
// } from "react";
// import SwipeableCard from "./SwipeableCard";

// export const CardStack = memo(
//   ({ profiles, onSwipe, currentImageIndex, currentProfileIndex }) => {
//     /* const [cards, setCards] = useState(data); */
//     const [currentProfiles, setCurrentProfiles] = useState([]);

//     useEffect(() => {
//       setCurrentProfiles((prevProfiles) => {
//         const existingIds = new Set(prevProfiles.map((p) => p.userId));
//         const newProfiles = profiles.filter((p) => !existingIds.has(p.userId));
//         return [...prevProfiles, ...newProfiles];
//       });
//     }, [profiles]);

//     const handleSwipe = useCallback((direction, userId) => {
//       onSwipe(direction, userId); // Pass userId to the onSwipe function
//       setCurrentProfiles((prev) =>
//         prev.filter((profile) => profile.userId !== userId)
//       );
//     });

//     return (
//       <>
//         <div
//           style={{
//             position: "relative",
//             width: "100%",
//             height: "100%",

//             overflow: "hidden",
//           }}
//         >
//           {currentProfiles.map((profile, index) => (
//             <SwipeableCard
//               key={profile.userId}
//               profile={profile}
//               id={profile.userId} // Add this line
//               onSwipe={(direction) => handleSwipe(direction, profile.userId)}
//               currentImage={profile.images[currentImageIndex]}
//               style={{
//                 position: "absolute",
//                 top: 0, // Align all cards to the top of the container
//                 left: 0, // Align all cards to the left of the container
//                 right: 0, // Stretch card to fill container width
//                 bottom: 0, // Stretch card to fill container height
//                 zIndex: currentProfiles.length - index, // Ensure cards are stacked with the first card on top
//                 opacity: 1 - index * 0.1, // Optional: Reduce opacity of cards below the top card
//               }}
//             >
//               {/* Render profile information */}
//               <div
//                 style={{
//                   padding: "20px",
//                   border: "1px solid #ccc",
//                   backgroundColor: "#fff",
//                   height: "100%",
//                   boxSizing: "border-box",
//                   display: "flex",
//                   flexDirection: "column",
//                   alignItems: "center",
//                   justifyContent: "center",
//                 }}
//               ></div>
//             </SwipeableCard>
//           ))}
//         </div>
//       </>
//     );
//   }
// );

// import React, {
//   useState,
//   memo,
//   useCallback,
//   useEffect,
//   forwardRef,
//   useImperativeHandle,
// } from "react";
// import SwipeableCard from "./SwipeableCard";

// const CardStackComponent = (props, ref) => {
//   const { profiles, onSwipe, currentImageIndex } = props;
//   const [currentProfiles, setCurrentProfiles] = useState([]);

//   useEffect(() => {
//     setCurrentProfiles((prevProfiles) => {
//       const existingIds = new Set(prevProfiles.map((p) => p.userId));
//       const newProfiles = profiles.filter((p) => !existingIds.has(p.userId));
//       return [...prevProfiles, ...newProfiles];
//     });
//   }, [profiles]);

//   const handleSwipe = useCallback(
//     (direction, userId) => {
//       onSwipe(direction, userId);
//       setCurrentProfiles((prev) =>
//         prev.filter((profile) => profile.userId !== userId)
//       );
//     },
//     [onSwipe]
//   );

//   useImperativeHandle(ref, () => ({
//     handleSwipe,
//   }));

//   return (
//     <div
//       style={{
//         position: "relative",
//         width: "100%",
//         height: "100%",
//         overflow: "hidden",
//       }}
//     >
//       {currentProfiles.map((profile, index) => (
//         <SwipeableCard
//           key={profile.userId}
//           profile={profile}
//           id={profile.userId}
//           onSwipe={(direction) => handleSwipe(direction, profile.userId)}
//           currentImage={profile.images[currentImageIndex]}
//           style={{
//             position: "absolute",
//             top: 0,
//             left: 0,
//             right: 0,
//             bottom: 0,
//             zIndex: currentProfiles.length - index,
//             opacity: 1 - index * 0.1,
//           }}
//         />
//       ))}
//     </div>
//   );
// };

// export const CardStack = memo(forwardRef(CardStackComponent));

import React, {
  useState,
  memo,
  useCallback,
  useEffect,
  forwardRef,
  useImperativeHandle,
} from "react";
import SwipeableCard from "./SwipeableCard";

const CardStackComponent = (props, ref) => {
  const { profiles, onSwipe, currentImageIndex } = props;
  const [currentProfiles, setCurrentProfiles] = useState([]);
  const [swipeDirection, setSwipeDirection] = useState(null); // State to control swipe direction
  const [swipedUserId, setSwipedUserId] = useState(null);

  useEffect(() => {
    setCurrentProfiles((prevProfiles) => {
      const existingIds = new Set(prevProfiles.map((p) => p.userId));
      const newProfiles = profiles.filter((p) => !existingIds.has(p.userId));
      return [...prevProfiles, ...newProfiles];
    });
  }, [profiles]);

  const handleSwipe = useCallback(
    (direction, userId) => {
      onSwipe(direction, userId);
      setSwipeDirection(direction);
      setSwipedUserId(userId);
      setTimeout(() => {
        setCurrentProfiles((prev) =>
          prev.filter((profile) => profile.userId !== userId)
        );
        setSwipeDirection(null);
        setSwipedUserId(null);
      }, 250); // Duration of the swipe animation
    },
    [onSwipe]
  );

  useImperativeHandle(ref, () => ({
    handleSwipe,
  }));

  return (
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
          id={profile.userId}
          onSwipe={(direction) => handleSwipe(direction, profile.userId)}
          currentImage={profile.images[currentImageIndex]}
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            zIndex: currentProfiles.length - index,
            opacity: 1 - index * 0.1,
          }}
          swipeClass={
            swipeDirection && swipedUserId === profile.userId
              ? `swipe-${swipeDirection}`
              : ""
          }
        />
      ))}
    </div>
  );
};

export const CardStack = memo(forwardRef(CardStackComponent));
