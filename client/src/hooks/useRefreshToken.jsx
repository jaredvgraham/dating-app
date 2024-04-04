import axios from "../api/axios";
import useAuth from "./useAuth";

export const useRefreshToken = () => {
  const { auth, setAuth } = useAuth(); // Obtain auth and setAuth from useAuth hook

  const refresh = async () => {
    console.log("Attempting to refresh token...");

    try {
      const response = await axios.post(
        "/refresh",
        {
          token: auth.refreshToken, // Correct key as expected by backend
        },
        {
          withCredentials: true,
          headers: { "Content-Type": "application/json" },
        }
      );
      console.log("Response Data:", response.data);
      // Update the auth context with new tokens
      setAuth((prev) => ({
        ...prev,
        accessToken: response.data.accessToken,
        refreshToken: response.data.refreshToken, // Assuming backend also refreshes the refreshToken
      }));
      console.log(`new ${response.data.accessToken}`);
      console.log(`MADE A NEW ONE`);

      return response.data.accessToken; // Return new access token
    } catch (error) {
      console.error("Failed to refresh token:", error);
      // Handle token refresh failure
    }
  };

  // Automatically refresh token every time refresh function is called

  return refresh;
};
