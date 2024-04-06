import axios from "../api/axios";
import useAuth from "./useAuth";

export const useRefreshToken = () => {
  const { auth, setAuth } = useAuth(); // Obtain auth and setAuth from useAuth hook

  const refresh = async () => {
    console.log("Attempting to refresh token...");

    try {
      const response = await axios.post(
        "/refresh",
        {},
        {
          withCredentials: true,
          headers: { "Content-Type": "application/json" },
        }
      );

      console.log("Response Data:", response.data);
      // Update the auth context with new tokens
      // Example response handling
      if (response.data.accessToken) {
        // Update context/state with new access token
        setAuth((prev) => ({
          ...prev,
          accessToken: response.data.accessToken,
          // Optionally update the refresh token if your backend sends a new one
        }));
      }

      console.log(`new ${response.data.accessToken}`);
      console.log(`MADE A NEW ONE`);

      return response.data.accessToken;
    } catch (error) {
      console.error("Failed to refresh token:", error);
    }
  };

  return refresh;
};
