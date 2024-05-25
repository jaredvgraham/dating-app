import React, { useEffect, useRef } from "react";
import assets from "../../assets/assets";

import { CardStack } from "./CardStack.jsx";
import "./Discover.css";
import { useState, useCallback } from "react";
import { Head } from "../../components/Head/Head.jsx";
import useAuth from "../../hooks/useAuth.js";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate.jsx";

export const Discover = () => {
  const axiosPrivate = useAxiosPrivate();
  const { auth } = useAuth();
  const accessToken = auth.accessToken;
  const [profiles, setProfiles] = useState([]);
  const [currentProfileIndex, setCurrentProfileIndex] = useState(0);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [rate, setRate] = useState(null);
  const [noMoreUsers, setNoMoreUsers] = useState(false);
  const [cardUser, setCardUser] = useState();

  const [isFetching, setIsFetching] = useState(false);

  useEffect(() => {
    fetchProfiles();
  }, []);
  useEffect(() => {
    console.log("All profiles: ", profiles);
  }, [profiles]);

  const fetchProfiles = async () => {
    if (isFetching) return; // Prevent duplicate fetches
    setIsFetching(true);
    console.log("fetching pro");

    try {
      const response = await axiosPrivate.get("/profile-feed");
      console.log("New profiles fetched: ", response.data);
      if (profiles) {
        setProfiles((prevProfiles) => {
          const existingIds = new Set(prevProfiles.map((p) => p.userId));
          const newProfiles = response.data.filter(
            (p) => !existingIds.has(p.userId)
          );
          return [...prevProfiles, ...newProfiles];
        });
      } else {
        setProfiles(response.data);
      }

      setNoMoreUsers(false);
      setIsFetching(false);
    } catch (error) {
      if (error.response && error.response.status === 404) {
        setNoMoreUsers(true);
      }
      console.error("Failed to fetch profiles:", error);
      setIsFetching(false);
    }
  };

  const currentProfile = profiles[currentProfileIndex];

  const handleSwipeAction = async (direction, userId) => {
    if (direction === "right") {
      if (userId) {
        document.getElementById(userId).classList.add("swipe-right");
      }
      console.log(`currentProfile userId  ${currentProfile.userId}`);
      console.log(`userId passed passed ${userId} `);
      await handleLike(userId);
    } else if (direction === "left") {
      document.getElementById(userId).classList.add("swipe-left");
      await sendSwipeToBackend(userId);
    }
    await sendSwipeToBackend(userId);
    if (rate !== null) {
      await ratePerson(userId, rate);
      setRate(null); // Reset after sending
    }
    /*  const nextProfileIndex = currentProfileIndex + 1;
    if (nextProfileIndex < profiles.length) {
      setCurrentProfileIndex(nextProfileIndex - 1);
    } */
    console.log(`profile length before ${profiles.length}`);
    setProfiles((currentProfiles) => {
      const nextProfiles = currentProfiles.filter(
        (profile) => profile.userId !== userId
      );
      console.log(`profile length after update: ${nextProfiles.length}`); // Correctly log new length
      if (nextProfiles.length < 3 && !noMoreUsers) {
        fetchProfiles();
      }

      // Update the current profile index after filtering
      setCurrentProfileIndex((prevIndex) => {
        const newIndex = prevIndex >= nextProfiles.length ? 0 : prevIndex;
        console.log(`new profile index after filtering: ${newIndex}`);
        return newIndex;
      });

      return nextProfiles;
    });

    setCurrentImageIndex(0); // Reset image index for the new profile
    /* if (!profiles) {
      fetchProfiles();
    } */
  };

  const sendSwipeToBackend = async (userId) => {
    try {
      await axiosPrivate.post(`/viewed/${userId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        withCredentials: true,
      });
    } catch (error) {
      console.error("Error sending profile viewed status to backend:", error);
    }
  };

  const ratePerson = async (userId, rating) => {
    setRate(rating);
    try {
      await axiosPrivate.post("/rate-user", { userId, rating });
    } catch (error) {
      console.log("error sending rate", error);
    }

    console.log("Rate with", rating);
  };

  async function handleLike(userId) {
    try {
      await axiosPrivate.post(`/like/${userId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        withCredentials: true,
      });
      console.log(
        "Profile viewed status sent to the backend for user:",
        userId
      );
    } catch (error) {
      console.error("Error sending profile viewed status to backend:", error);
    }
  }
  const getRateReady = (rating) => {
    setRate(rating);
  };
  async function handleDislike() {}

  const moveSlide = (direction) => {
    const currentImages = profiles[currentProfileIndex].images;
    setCurrentImageIndex((prev) => {
      let newIndex = prev + direction;
      if (newIndex < 0) {
        newIndex = currentImages.length - 1; // wrap back to the last image
      } else if (newIndex >= currentImages.length) {
        newIndex = 0; // wrap around to the first image
      }
      return newIndex;
    });
  };

  const cardStackRef = useRef(null);

  const handleLikeButtonClick = () => {
    if (cardStackRef.current) {
      const userId = profiles[currentProfileIndex].userId;
      cardStackRef.current.handleSwipe("right", userId);
    }
  };
  const handleDisLikeButtonClick = () => {
    if (cardStackRef.current) {
      const userId = profiles[currentProfileIndex].userId;
      cardStackRef.current.handleSwipe("left", userId);
    }
  };

  /*   if (isFetching) {
    return (
      <div className="everything">
        <div className="card-container">Loading Profiles...</div>
      </div>
    );
  } */
  if (noMoreUsers || profiles.length === 0) {
    return (
      <>
        <Head />
        <div
          style={{
            border: "1px solid black",
            height: "80vh",
            marginTop: "50px",
            borderRadius: "10px",
          }}
          className="everything"
        >
          <div className="card-container">No more users to show.</div>
        </div>
      </>
    );
  }
  return (
    <>
      <Head />
      <div className="everything">
        <div className="round-it">
          <div className="cards-container">
            <span className="prev" onClick={() => moveSlide(-1)}>
              &#10094;
            </span>
            <CardStack
              ref={cardStackRef}
              profiles={profiles}
              onSwipe={handleSwipeAction}
              currentImageIndex={currentImageIndex}
              currentProfileIndex={currentProfileIndex}
            />
            <span className="next" onClick={() => moveSlide(1)}>
              &#10095;
            </span>
          </div>
          <div className="like-notLike">
            <img
              className="dislike"
              src={assets.dislike}
              alt="Not Like"
              onClick={handleDisLikeButtonClick}
              draggable="false"
            />
            <img
              className="like"
              src={assets.like}
              alt="Like"
              /* onClick={() =>
                handleSwipeAction("right", profiles[currentProfileIndex].userId)
              } */
              onClick={handleLikeButtonClick}
              draggable="false"
            />
          </div>
          <div className="rate-ctn">
            <h3 className="rate">Rate: {rate}</h3>
            <div className="scale">
              {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((number) => (
                <button
                  className={`rateBtn ${rate === number ? "selected" : ""}`}
                  key={number}
                  onClick={() => getRateReady(number)}
                >
                  {number}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
