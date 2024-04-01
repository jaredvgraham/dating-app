import axios from "axios";

const BASE_URL = "https://d80a-207-206-228-59.ngrok-free.app";

export default axios.create({
  baseURL: BASE_URL,
});
export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});
