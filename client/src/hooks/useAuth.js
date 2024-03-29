import { useContext } from "react";
/* import AuthContext from "../context/AuthProvider";

const useAuth = () => {
  return useContext(AuthContext);
};

export default useAuth;
 */

// import AuthContext from "../context/AuthProvider"; // Comment this out for testing

const useAuth = () => {
  // return useContext(AuthContext); // Comment this out for testing
  const auth = { username: "testUser" }; // Simulate an authenticated user
  console.log("Auth state:", auth);
  return { auth };
};

export default useAuth;
