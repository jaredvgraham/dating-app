import React, { useEffect } from "react";
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

  const [noMoreUsers, setNoMoreUsers] = useState(false);

  const [isFetching, setIsFetching] = useState(false);

  useEffect(() => {
    fetchProfiles();
  }, []);

  const fetchProfiles = async () => {
    if (isFetching) return; // Prevent duplicate fetches
    setIsFetching(true);

    try {
      const response = await axiosPrivate.get("/profile-feed");

      setProfiles(response.data);

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
      await handleLike(userId);
    } else if (direction === "left") {
      await sendSwipeToBackend(userId);
    }
    const isLastProfile = currentProfileIndex >= profiles.length - 1;
    if (isLastProfile) {
      // If we're already at the last profile, check for more users
      fetchProfiles(); // Potentially fetch more profiles if your app supports that
    } else {
      // Otherwise, proceed to the next profile
      setCurrentProfileIndex((prevIndex) => prevIndex + 1);
      setCurrentImageIndex(0); // Reset image index for the new profile
    }
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

  const ratePerson = (rating) => {
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

  if (isFetching) {
    return <div>Loading profiles...</div>;
  }
  if (noMoreUsers && profiles.length === 0) {
    return <div className="card">No more users to show.</div>;
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
              profiles={profiles}
              onSwipe={handleSwipeAction}
              currentImageIndex={currentImageIndex}
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
              onClick={() => handleDislike("dislike")}
              draggable="false"
            />
            <img
              className="like"
              src={assets.like}
              alt="Like"
              onClick={() => handleLike(currentProfile.userId)}
              draggable="false"
            />
          </div>
          <div className="rate-ctn">
            <h3 className="rate">Rate:</h3>
            <div className="scale">
              {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((number) => (
                <button key={number} onClick={() => ratePerson(number)}>
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
