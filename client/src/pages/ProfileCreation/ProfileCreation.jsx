import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";

export const ProfileCreation = () => {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const axiosPrivate = useAxiosPrivate();

  const [profileData, setProfileData] = useState({
    biography: "",
    hobbies: "",
    schoolOrWork: "",
    musicalArtists: "",
    images: "",
  });

  const [profileImage, setProfileImage] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [fileSizeError, setFileSizeError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfileData((prevProfileData) => ({
      ...prevProfileData,
      [name]: value,
    }));
  };

  // Define a maximum file size (e.g., 5MB)
  const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > MAX_FILE_SIZE) {
        // Update fileSizeError for oversized files
        setFileSizeError(
          "File too large. Please select a file smaller than 5MB."
        );
      } else {
        // Acceptable file size, update profileImage and clear fileSizeError
        setProfileImage(file);
        setFileSizeError(""); // Clear fileSize error
      }
    } else {
      // No file selected, clear fileSizeError
      setFileSizeError("");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (error || fileSizeError) {
      console.log("Form submission blocked due to errors.");
      return; // Stop the submission
    }

    setIsLoading(true);
    setError("");

    const formData = new FormData();
    Object.entries(profileData).forEach(([key, value]) => {
      formData.append(key, value);
    });

    if (profileImage) {
      formData.append("images", profileImage);
    }

    for (let [key, value] of formData.entries()) {
      console.log(`${key}: ${value}`);
    }

    try {
      // Use await to wait for the post request to complete
      await axiosPrivate.post("/profile", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${auth.accessToken}`,
        },
      });

      navigate("/"); // Proceed to navigate after successful request
    } catch (err) {
      console.error("Profile creation error:", err);
      // Set error from server response or a generic error message
      setError(
        err.response?.data?.message ||
          "An error occurred during profile creation."
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="profile-creation-container">
      <h2>Profile Information</h2>
      {fileSizeError && <p className="error">{fileSizeError}</p>}
      {error && <p className="error">{error}</p>}{" "}
      {/* Display error messages here */}
      <form onSubmit={handleSubmit} encType="multipart/form-data" method="POST">
        {/* Input fields remain unchanged */}
        <label htmlFor="biography">Biography:</label>
        <textarea
          id="biography"
          name="biography"
          value={profileData.biography}
          onChange={handleChange}
          required
        />

        <label htmlFor="hobbies">Hobbies:</label>
        <input
          type="text"
          id="hobbies"
          name="hobbies"
          value={profileData.hobbies}
          onChange={handleChange}
          required
        />

        <label htmlFor="schoolOrWork">School/Work:</label>
        <input
          type="text"
          id="schoolOrWork"
          name="schoolOrWork"
          value={profileData.schoolOrWork}
          onChange={handleChange}
          required
        />

        <label htmlFor="musicalArtists">Favorite Musical Artists:</label>
        <input
          type="text"
          id="musicalArtists"
          name="musicalArtists"
          value={profileData.musicalArtists}
          onChange={handleChange}
          required
        />

        <label htmlFor="images">Profile Images:</label>
        <input
          type="file"
          id="images"
          name="images"
          onChange={handleImageChange}
        />

        <button type="submit" disabled={isLoading}>
          {isLoading ? "Saving..." : "Save Profile"}
        </button>
      </form>
    </div>
  );
};
