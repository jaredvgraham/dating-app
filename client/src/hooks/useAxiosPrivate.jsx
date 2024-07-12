import { useEffect } from "react";
import { axiosPrivate } from "../api/axios"; // Adjust the import path based on your project structure
import { getAuth } from "firebase/auth";
import useAuth from "./useAuth";

export const useAxiosPrivate = () => {
  const { auth } = useAuth();
  const firebaseAuth = getAuth();

  useEffect(() => {
    const requestIntercept = axiosPrivate.interceptors.request.use(
      async (config) => {
        const user = firebaseAuth.currentUser;

        if (user) {
          const token = await user.getIdToken();
          config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
      },
      (error) => Promise.reject(error)
    );

    const responseIntercept = axiosPrivate.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config;
        if (error.response.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          const user = firebaseAuth.currentUser;

          if (user) {
            const token = await user.getIdToken(true);
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return axiosPrivate(originalRequest);
          }
        }
        return Promise.reject(error);
      }
    );

    return () => {
      axiosPrivate.interceptors.request.eject(requestIntercept);
      axiosPrivate.interceptors.response.eject(responseIntercept);
    };
  }, [auth, firebaseAuth]);

  return axiosPrivate;
};
