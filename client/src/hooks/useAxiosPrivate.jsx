import { axiosPrivate } from "../api/axios";
import { useEffect } from "react";
import { useRefreshToken } from "./useRefreshToken";
import axios from "axios";

import useAuth from "./useAuth";
import { useSafariRefresh } from "./useSafariRefresh";

export const useAxiosPrivate = () => {
  const refresh = useRefreshToken();
  const safariRefresh = useSafariRefresh();
  const { auth } = useAuth();

  useEffect(() => {
    const requestIntercept = axiosPrivate.interceptors.request.use(
      (config) => {
        if (!config.headers["Authorization"]) {
          config.headers["Authorization"] = `Bearer ${auth?.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    const responseIntercept = axiosPrivate.interceptors.response.use(
      (response) => response,
      async (error) => {
        const prevRequest = error?.config;
        if (error?.response?.status === 403 && !prevRequest?.sent) {
          prevRequest.sent = true;
          const newAccessToken = auth.isSafari
            ? await safariRefresh()
            : refresh(); // Attempt to refresh token

          // If refreshing the token is successful, update the Authorization header
          // with the new token and resend the original request.
          if (newAccessToken) {
            axios.defaults.headers.common[
              "Authorization"
            ] = `Bearer ${newAccessToken}`;
            prevRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
            return axiosPrivate(prevRequest);
          }
        }
        return Promise.reject(error);
      }
    );

    // Cleanup on unmount
    return () => {
      axiosPrivate.interceptors.request.eject(requestIntercept);
      axiosPrivate.interceptors.response.eject(responseIntercept);
    };
  }, [auth, refresh, safariRefresh]); // Re-run effect if auth or refresh function changes

  return axiosPrivate;
};
