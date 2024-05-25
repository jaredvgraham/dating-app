import React, { useState, useEffect } from "react";
import Slider from "rc-slider";
import "rc-slider/assets/index.css";
import { useAxiosPrivate } from "../../hooks/useAxiosPrivate";
import "./Preferences.css";

export const Preferences = () => {
  const axiosPrivate = useAxiosPrivate();
  const [prefData, setPrefData] = useState({
    gender: [],
    minAge: 18,
    maxAge: 34,
    distance: 24,
    global: false,
  });
  const [error, setError] = useState("");

  useEffect(() => {
    // Fetch initial data for form
    const fetchPreferences = async () => {
      try {
        const response = await axiosPrivate.get("/preferences");
        setPrefData({
          gender: response.data.genderPref, // Assuming the backend sends it as 'genderPref'
          minAge: response.data.minAge,
          maxAge: response.data.maxAge,
          distance: response.data.maxDist,
          global: response.data.global,
          // Assuming the backend sends it as 'maxDistance'
        });
      } catch (err) {
        console.error("Failed to fetch preferences", err);
        setError("Failed to load your preferences.");
      }
    };
    fetchPreferences();
  }, [axiosPrivate]);

  const onAgeRangeChange = (newRange) => {
    if (newRange[0] < newRange[1]) {
      setPrefData({ ...prefData, minAge: newRange[0], maxAge: newRange[1] });
    } else {
      setError("Minimum age must be less than maximum age.");
    }
  };

  const onDistanceChange = (newDistance) => {
    setPrefData({ ...prefData, distance: parseInt(newDistance) });
  };

  const handleGenderChange = (e) => {
    if (e.target.value === "both") {
      setPrefData({ ...prefData, gender: ["male", "female"] });
    } else {
      setPrefData({ ...prefData, gender: [e.target.value] });
    }

    // Convert single gender selection into an array
  };
  const handleGlobalChange = () => {
    setPrefData((prev) => ({ ...prev, global: !prev.global }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const payload = {
        ...prefData,
        maxDist: prefData.distance,
        genderPref: prefData.gender,
      };
      await axiosPrivate.post("/preferences/update", payload);
      alert("Preferences saved successfully!");
    } catch (error) {
      console.error("Error updating preferences", error);
      setError("Failed to save preferences.");
    }
  };

  return (
    <>
      <div className="pref-ctn">
        <div className="pref-ctn-two">
          {error && <p className="error">{error}</p>}
          <form onSubmit={handleSubmit}>
            <label htmlFor="gender-preference">Show me:</label>
            <select
              name="gender"
              value={prefData.gender[0] || ""}
              onChange={handleGenderChange}
            >
              <option value="">Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="both">Both</option>
            </select>

            <label htmlFor="age-range">Age Range:</label>
            <div className="slider-values">
              {`${prefData.minAge} - ${prefData.maxAge}`}
            </div>
            <Slider
              range
              min={18}
              max={100}
              defaultValue={[prefData.minAge, prefData.maxAge]}
              onChange={onAgeRangeChange}
            />

            <label htmlFor="distance-preference">Distance (miles):</label>
            <div className="distance-display">{`${prefData.distance} miles`}</div>
            <input
              type="range"
              min="1"
              max="100"
              value={prefData.distance}
              onChange={(e) => onDistanceChange(e.target.value)}
              name="distance"
            />
            <label htmlFor="global-search">Global Search:</label>
            <input
              type="checkbox"
              checked={prefData.global}
              onChange={handleGlobalChange}
              id="global-search"
            />

            <button type="submit">Save</button>
          </form>
        </div>
      </div>
    </>
  );
};
