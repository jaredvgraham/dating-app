import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "../../api/axios";
import useAuth from "../../hooks/useAuth";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";
import "./CreateAccount.css";

const CreateAccount = () => {
  const navigate = useNavigate();
  const { auth, setAuth } = useAuth();
  const accessToken = auth.accessToken;
  const axiosPrivate = useAxiosPrivate();
  const [exists, setExists] = useState("");

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    dob: "",
    gender: "",
    sexualOrientation: "",
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };
  const handleSetGenderMale = () => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      gender: "male",
    }));
  };

  const handleSetGenderFemale = () => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      gender: "female",
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");
    console.log(`JWT Token: ${accessToken}`); // Add this line to log the JWT token

    try {
      const response = await axiosPrivate.post(
        `/create-account`,
        formData, // Assuming `formData` is the data you want to send
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`, // Include the JWT token here
          },
          withCredentials: true,
        }
      );
      setAuth((prevAuth) => ({
        ...prevAuth,
        firstName: formData.firstName,
      }));
      console.log(auth);
      setIsLoading(false);
      navigate(`/profile-creation`);
    } catch (err) {
      console.error("Account creation error:", err);

      console.log(err.response);

      let errorMessage = "An unexpected error occurred. Please try again.";

      if (err.response && err.response.data) {
        errorMessage = err.response.data.message || "Account creation failed.";
      }

      setError(errorMessage);

      setIsLoading(false);
    }
  };
  const capitalizeFirstLetter = (string) => {
    if (!string) return ""; // Return empty string if input is falsy
    return string.charAt(0).toUpperCase() + string.slice(1);
  };

  return (
    <div className="a-create-ctn">
      <div className="a-hold">
        <h2>Create Account</h2>

        {error && <p className="a-error">{error}</p>}
        <form className="a-form" onSubmit={handleSubmit}>
          <div className="names-ctn">
            <div className="names-stack">
              <label htmlFor="firstName"></label>
              <input
                type="text"
                placeholder="First Name"
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="names-stack">
              <label htmlFor="lastName"></label>
              <input
                type="text"
                placeholder="Last Name"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <div className="dob-ctn">
            <label htmlFor="dob">Date of Birth:</label>
            <input
              className="dob-in"
              type="date"
              id="dob"
              name="dob"
              value={formData.dob}
              onChange={handleChange}
              required
            />
          </div>
          <div className="gender-ctn">
            <label>Gender: {capitalizeFirstLetter(formData.gender)}</label>
            <div className="gen-btns">
              <button
                className="male-btn"
                type="button"
                onClick={handleSetGenderMale}
              >
                Male
              </button>
              <button
                className="wom-btn"
                type="button"
                onClick={handleSetGenderFemale}
              >
                Female
              </button>
            </div>
          </div>
          <div className="sO-ctn">
            <label htmlFor="sexualOrientation">Sexual Orientation:</label>
            <input
              type="text"
              className="sO-in"
              placeholder="Straight / Gay / Bi etc..."
              id="sexualOrientation"
              name="sexualOrientation"
              value={formData.sexualOrientation}
              onChange={handleChange}
              required
            />
          </div>
          <button className="submit-btn" type="submit" disabled={isLoading}>
            {isLoading ? "Creating Account..." : "Create Account"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateAccount;
