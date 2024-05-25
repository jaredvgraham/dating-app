import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";
import "./ProfileCreation.css";
import assets from "../../assets/assets";

export const ProfileCreation = () => {
  const navigate = useNavigate();
  const { auth, setAuth } = useAuth();
  const axiosPrivate = useAxiosPrivate();

  const [profileData, setProfileData] = useState({
    biography: "",
    hobbies: "",
    schoolOrWork: "",
    musicalArtists: "",
    pronouns: "",
    images: [],
  });

  const [profileImage, setProfileImage] = useState([]);
  const [imgToDisplay, setImgToDisplay] = useState([]);
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
    const files = Array.from(e.target.files); // Convert FileList to Array
    const oversizedFiles = files.filter((file) => file.size > MAX_FILE_SIZE);

    if (oversizedFiles.length > 0) {
      // Handle the case where one or more files are too big
      window.alert("One or more files are too big.");
      setFileSizeError(
        "One or more files are too large. Please select files smaller than 5MB each."
      );
    } else {
      // All files are of acceptable size
      setProfileImage((prev) => [...prev, ...files]);
      const fileURLs = files.map((file) => URL.createObjectURL(file));
      setImgToDisplay((prevUrls) => [...prevUrls, ...fileURLs]); // Store URLs for display
      setFileSizeError(""); // Clear fileSizeError
      // Set the state with an array of files
    }
  };

  const handleRemoveImg = (index) => {
    // Remove the image from the imgToDisplay array for the preview
    const newImgToDisplay = imgToDisplay.filter((_, i) => i !== index);
    setImgToDisplay(newImgToDisplay);

    // Remove the image from the profileImage array for the backend
    const newProfileImage = profileImage.filter((_, i) => i !== index);
    setProfileImage(newProfileImage);
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
      profileImage.forEach((file) => {
        formData.append("images", file); // Note the simple 'images' key
      });
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
      setAuth((prev) => ({
        ...prev,
        profileExists: true,
      }));
      console.log(formData.pronouns);
      navigate("/isUser"); // Proceed to navigate after successful request
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
    <>
      <div className="non-dashboard-element">
        <img src={assets.rLogo} alt="" className="logo-non-dash" />
      </div>
      <div className="profile-creation-container">
        <div className="profile-creation-box">
          <h2>Profile Information</h2>
          <div className="profile-three-ctn">
            {fileSizeError && <p className="error">{fileSizeError}</p>}
            {error && <p className="error">{error}</p>}{" "}
            {/* Display error messages here */}
            <form
              onSubmit={handleSubmit}
              encType="multipart/form-data"
              method="POST"
              className="profile-input-container"
            >
              {/* Input fields remain unchanged */}
              <label htmlFor="biography">Biography:</label>
              <textarea
                id="biography"
                name="biography"
                value={profileData.biography}
                onChange={handleChange}
                required
              />

              <label htmlFor="pronouns">Pronouns:</label>
              <input
                type="text"
                id="pronouns"
                name="pronouns"
                value={profileData.pronouns}
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
              <div className="profile-creation-file-upload-wrapper">
                <h3>Up to 7 Images:</h3>
                <label className="btn-file-upload" htmlFor="images">
                  Upload Images
                </label>
                <input
                  className="imgs-input-"
                  type="file"
                  id="images"
                  accept="image/*"
                  name="images"
                  onChange={handleImageChange}
                  multiple
                />
              </div>

              <button
                className="profile-creation-submit"
                type="submit"
                disabled={isLoading}
              >
                {isLoading ? "Saving..." : "Save Profile"}
              </button>
            </form>
            <div className="profile-creation-img-ctn">
              {imgToDisplay &&
                imgToDisplay.map((url, index) => (
                  <>
                    <div className="img-obj">
                      <img
                        className="profile-creation-img"
                        key={index}
                        src={url}
                        alt="Profile preview"
                      />
                      <button
                        className="remove-img-btn"
                        onClick={() => handleRemoveImg(index)}
                      >
                        x
                      </button>
                    </div>
                  </>
                ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
