import React, { useState, useEffect } from "react";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";

export const Profile = () => {
  const axiosPrivate = useAxiosPrivate();

  const [profileText, setProfileText] = useState({
    firstName: "",
    age: "",
    hobbies: "",
    biography: "",
    schoolOrWork: "",
    musicalArtists: "",
  });
  const [profileImgs, setProfileImgs] = useState([]);

  useEffect(() => {
    const getProfile = async () => {
      try {
        const response = await axiosPrivate.get("/profile");
        console.log("Profile Data:", response.data);
        setProfileText({
          firstName: response.data.firstName,
          age: response.data.age,
          hobbies: response.data.hobbies,
          biography: response.data.biography,
          schoolOrWork: response.data.schoolOrWork,
          musicalArtists: response.data.musicalArtists,
        });

        const imageUrls = response.data.images.map((img) => img.imageUrl);
        setProfileImgs(imageUrls);
      } catch (error) {
        console.error("Failed to fetch profile:", error);
      }
    };
    getProfile();
  }, [axiosPrivate]);

  return (
    <div>
      <h1>Name: {profileText.firstName}</h1>
      <h1>Age: {profileText.age}</h1>
      <h1>Hobbies: {profileText.hobbies}</h1>
      <h1>Bio: {profileText.biography}</h1>
      <h1>School or Work: {profileText.schoolOrWork}</h1>
      <h1>Favorite artists: {profileText.musicalArtists}</h1>
      {profileImgs.map((imgUrl, index) => (
        <img
          key={index}
          src={imgUrl}
          alt={`Profile image ${index}`}
          onError={(e) => {
            console.error(`Failed to load image at ${imgUrl}`);
            e.target.style.display = "none";
          }}
        />
      ))}
    </div>
  );
};
