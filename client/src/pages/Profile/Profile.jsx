import React, { useState, useEffect } from "react";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";
import "./Profile.css";

export const Profile = () => {
  const axiosPrivate = useAxiosPrivate();

  const [profileText, setProfileText] = useState({
    firstName: "",
    age: "",
    hobbies: "",
    biography: "",
    schoolOrWork: "",
    musicalArtists: "",
    pronouns: "",
    rating: null,
  });
  const [profileImgs, setProfileImgs] = useState([]);
  const [currentImgIndex, setCurrentImgIndex] = useState(0); // New state for current image index
  const [editMode, setEditMode] = useState(null);
  const [newImageFiles, setNewImageFiles] = useState([]);
  const [isEditingImages, setIsEditingImages] = useState(false);
  const [initialImageFiles, setInitialImageFiles] = useState([]); // New state to keep track of initial images

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
          pronouns: response.data.pronouns,
          rating: response.data.rating,
        });

        const imageUrls = response.data.images.map((img) => img.imageUrl);
        setProfileImgs(imageUrls);
        setInitialImageFiles(imageUrls); // Set initial images
      } catch (error) {
        console.error("Failed to fetch profile:", error);
      }
    };
    getProfile();
  }, [axiosPrivate]);

  const handleEdit = (field) => {
    setEditMode(field);
  };

  const handleSave = async () => {
    try {
      const formData = new FormData();

      // Append the text fields as individual parameters
      formData.append("biography", profileText.biography);
      formData.append("hobbies", profileText.hobbies);
      formData.append("schoolOrWork", profileText.schoolOrWork);
      formData.append("musicalArtists", profileText.musicalArtists);
      formData.append("pronouns", profileText.pronouns);

      // Append all image files (existing URLs and new files) to the 'images' field
      newImageFiles.forEach((file) => {
        if (typeof file === "string") {
          // Fetch the blob of the existing image
          fetch(file)
            .then((res) => res.blob())
            .then((blob) => {
              formData.append(
                "images",
                new File([blob], file.split("/").pop(), { type: blob.type })
              );
              // Log form data to see what is being sent
              for (let pair of formData.entries()) {
                console.log(`${pair[0]}: ${pair[1]}`);
              }
            })
            .catch((err) => console.error("Failed to fetch image blob:", err));
        } else {
          // Append new image files
          formData.append("images", file);
        }
      });

      // Wait for all images to be appended before making the request
      await new Promise((resolve) => setTimeout(resolve, 1000));

      await axiosPrivate.post("/profile-update", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setEditMode(null);
      setIsEditingImages(false);
      setNewImageFiles([]);
      setInitialImageFiles([]); // Reset initialImageFiles

      // Fetch updated profile data to reflect the changes
      const response = await axiosPrivate.get("/profile");
      const updatedImageUrls = response.data.images.map((img) => img.imageUrl);
      setProfileImgs(updatedImageUrls);
    } catch (error) {
      console.error("Failed to update profile:", error);
    }
  };

  const handleInputChange = (e, field) => {
    setProfileText({ ...profileText, [field]: e.target.value });
  };

  const handleImageChange = (e) => {
    setNewImageFiles((prevImages) => [
      ...prevImages,
      ...Array.from(e.target.files),
    ]);
  };

  const handleImageDelete = (index) => {
    setNewImageFiles((prevImages) => prevImages.filter((_, i) => i !== index));
  };

  const handleEditImages = () => {
    setIsEditingImages(true);
    setNewImageFiles([...initialImageFiles]);
  };

  const handleCancelEditImages = () => {
    setIsEditingImages(false);
    setNewImageFiles([]);
  };

  const handlePrevImage = () => {
    setCurrentImgIndex((prevIndex) =>
      prevIndex === 0 ? profileImgs.length - 1 : prevIndex - 1
    );
  };

  const handleNextImage = () => {
    setCurrentImgIndex((prevIndex) =>
      prevIndex === profileImgs.length - 1 ? 0 : prevIndex + 1
    );
  };

  return (
    <div className="profile-page-container">
      <div className="profile-page-content">
        <div className="profile-page-images">
          {!isEditingImages &&
            profileImgs.map((imgUrl, index) => (
              <div
                key={index}
                className={`profile-page-image ${
                  index === currentImgIndex ? "profile-page-active" : ""
                }`}
                style={{
                  display: index === currentImgIndex ? "block" : "none",
                  backgroundImage: `url(${imgUrl})`,
                }}
              >
                <div className="profile-large-screen-ctn">
                  <div className="profile-person-info">
                    <h2>{profileText.firstName}</h2>
                    <p>Distance: 0 miles away</p>
                  </div>
                </div>
              </div>
            ))}
          {!isEditingImages && (
            <>
              <span className="profile-prev" onClick={handlePrevImage}>
                &#10094;
              </span>
              <span className="profile-next" onClick={handleNextImage}>
                &#10095;
              </span>
            </>
          )}
          {isEditingImages && (
            <div className="edit-all-imgs-ctn">
              <label className="edit-img-btn" htmlFor="profile-images">
                Upload Images
              </label>
              <input
                type="file"
                id="profile-images"
                multiple
                onChange={handleImageChange}
              />
              <div className="profile-images-edit">
                {newImageFiles.map((file, index) => (
                  <div key={index} className="edit-img-ctn">
                    <img
                      src={
                        typeof file === "string"
                          ? file
                          : URL.createObjectURL(file)
                      }
                      alt="Profile"
                      className="edit-img"
                    />
                    <button onClick={() => handleImageDelete(index)}>
                      Delete
                    </button>
                  </div>
                ))}
              </div>
              <div className="edit-buttons">
                <button className="img-save" onClick={handleSave}>
                  Save Images
                </button>
                <button className="img-cancel" onClick={handleCancelEditImages}>
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
      <div className="profile-page-details">
        {[
          "hobbies",
          "biography",
          "schoolOrWork",
          "musicalArtists",
          "pronouns",
        ].map((field) => (
          <div className="profile-field" key={field}>
            {editMode === field ? (
              <input
                type="text"
                value={profileText[field]}
                onChange={(e) => handleInputChange(e, field)}
              />
            ) : (
              <p>
                <strong>{`${
                  field.charAt(0).toUpperCase() + field.slice(1)
                }:`}</strong>{" "}
                {profileText[field]}
              </p>
            )}
            {editMode === field ? (
              <>
                <button onClick={handleSave}>Save</button>
                <button onClick={() => setEditMode(null)}>Cancel</button>
              </>
            ) : (
              <button onClick={() => handleEdit(field)}>Edit</button>
            )}
          </div>
        ))}
        {!isEditingImages && (
          <button className="Editt-Images" onClick={handleEditImages}>
            Edit Images
          </button>
        )}
      </div>
    </div>
  );
};
