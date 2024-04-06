import React from "react";
import assets from "../../assets/assets";

import "./Discover.css";
import { useState } from "react";
import { Head } from "../../components/Head/Head.jsx";
import LogoutButton from "../../components/Logout.jsx";

export const Discover = () => {
  // Placeholder functions for your onClick events
  const [currentSlide, setCurrentSlide] = useState(0);
  const images = [assets.wom5alt, assets.wom5]; // add the rest of your images to this array

  const moveSlide = (direction) => {
    setCurrentSlide((prev) => {
      let newIndex = prev + direction;
      if (newIndex < 0) {
        newIndex = images.length - 1; // wrap back to the last image
      } else if (newIndex >= images.length) {
        newIndex = 0; // wrap around to the first image
      }
      return newIndex;
    });
  };

  const ratePerson = (rating) => {
    // Logic to rate the person
    console.log("Rate with", rating);
  };

  return (
    <>
      <div className="card">
        <LogoutButton />
        <Head />
        <div className="img-slider">
          <span className="prev" onClick={() => moveSlide(-1)}>
            &#10094;
          </span>
          {images.map((src, index) => (
            <img
              key={src}
              className={`slide ${index === currentSlide ? "active" : ""}`}
              src={src}
              alt={`Person image ${index + 1}`}
            />
          ))}
          <span className="next" onClick={() => moveSlide(1)}>
            &#10095;
          </span>
        </div>

        <div className="large-screen-ctn">
          <div className="person-info">
            <h2>Person name</h2>
            <p>Person distance 12 miles</p>
            <h3 className="rate">Rate:</h3>
          </div>
          <div className="scale">
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((number) => (
              <button key={number} onClick={() => ratePerson(number)}>
                {number}
              </button>
            ))}
          </div>
        </div>

        <div className="like-notLike">
          <img
            className="dislike"
            src={assets.dislike}
            alt="Not Like"
            onClick={() => ratePerson("dislike")}
          />
          <img
            className="like"
            src={assets.like}
            alt="Like"
            onClick={() => ratePerson("like")}
          />
        </div>
      </div>
    </>
  );
};
