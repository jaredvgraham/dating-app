import axios from "axios";

const BASE_URL = "https://21e6f20628c2.ngrok.app";

export default axios.create({
  baseURL: BASE_URL,
});
export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});
