import axios from "axios";

//const BASE_URL = "https://dtebyratebackendrndr-3.onrender.com";
// const BASE_URL = "https://42f5e9aace27.ngrok.app";

// const BASE_URL = process.env.NGROK_API;

const BASE_URL = import.meta.env.VITE_PRODUCTION_URL;
//  ||
// "https://dtebyratebackendrndr-3.onrender.com";
console.log("API Base URL:", BASE_URL);
export default axios.create({
  baseURL: BASE_URL,
});
export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});
