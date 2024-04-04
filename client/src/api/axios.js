import axios from "axios";

const BASE_URL = "https://5de9-173-76-249-214.ngrok-free.app";

export default axios.create({
  baseURL: BASE_URL,
});
export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});
