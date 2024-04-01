import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "../api/axios";
import useAuth from "../hooks/useAuth";
import { useAxiosPrivate } from "../hooks/useAxiosPrivate";

const CreateAccount = () => {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const accessToken = auth.accessToken;
  const axiosPrivate = useAxiosPrivate();

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

      setIsLoading(false);
      navigate(`/dashboard`);
    } catch (err) {
      console.error("Account creation error:", err);
      setIsLoading(false);
      setError(
        err.response
          ? err.response.data.error
          : "An error occurred during account creation"
      );
    }
  };

  return (
    <div className="a-create-ctn">
      <h2>Create Account</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="names-ctn">
          <div>
            <label htmlFor="firstName">First Name:</label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="lastName">Last Name:</label>
            <input
              type="text"
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
            type="date"
            id="dob"
            name="dob"
            value={formData.dob}
            onChange={handleChange}
            required
          />
        </div>
        <div className="gender-ctn">
          <label>Gender:</label>
          <button type="button" onClick={handleSetGenderMale}>
            Male
          </button>
          <button type="button" onClick={handleSetGenderFemale}>
            Female
          </button>
        </div>
        <div className="sO-ctn">
          <label htmlFor="sexualOrientation">Sexual Orientation:</label>
          <input
            type="text"
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
  );
};

export default CreateAccount;
