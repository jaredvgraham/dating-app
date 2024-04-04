import { useState, useEffect } from "react";

export const useGeoLocation = () => {
  const [locationInfo, setLocationInfo] = useState(null);
  const [locationError, setLocationError] = useState(null);

  useEffect(() => {
    const { geolocation } = navigator;

    if (!geolocation) {
      setLocationError("Geolocation is not supported by your browser");
      return;
    }

    const successFn = (position) => {
      console.log("GeolocationPosition:", position);
      console.log("Direct position.coords:", position.coords);
      setLocationInfo({
        latitude: position.coords.latitude,
        longitude: position.coords.longitude,
      });
    };

    const errorFn = (error) => {
      console.log(error);
      setLocationError(error.message);
    };

    geolocation.getCurrentPosition(successFn, errorFn);
  }, []);

  useEffect(() => {
    console.log("Updated locationInfo:", locationInfo);
  }, [locationInfo]);

  return { locationError, locationInfo };
};
