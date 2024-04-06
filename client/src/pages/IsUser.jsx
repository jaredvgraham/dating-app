import { useAxiosPrivate } from "../hooks/useAxiosPrivate";
import useAuth from "../hooks/useAuth";
import { useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
export const IsUser = () => {
  const { auth, setAuth } = useAuth();
  const accessToken = auth.accessToken;
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  useEffect(() => {
    const isUser = async () => {
      console.log("Calling is user function");
      try {
        const response = await axiosPrivate.get("/profile-exist", {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        console.log(response.data);
        console.log("inside the try block");
        const profileExists = response.data.exists;
        setAuth((prev) => ({
          ...prev,
          profileExists: profileExists === "yes",
        }));
        profileExists === "yes" ? navigate("/") : navigate("/create-account");
      } catch (error) {
        console.error("Error checking user profile:", error);
      }
    };

    isUser();
  }, [accessToken, navigate, axiosPrivate]);
  return <div></div>;
};
