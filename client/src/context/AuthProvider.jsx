// import { createContext, useEffect, useState, useCallback } from "react";
// import axios from "../api/axios";

// const AuthContext = createContext({});

// export const AuthProvider = ({ children }) => {
//   const [auth, setAuth] = useState({
//     isAuthenticated: false,
//     username: null,
//     accessToken: null,
//     userId: null,
//   });
//   const [isLoading, setIsLoading] = useState(true);

//   useEffect(() => {
//     const validateSession = async () => {
//       try {
//         const response = await axios.get("/session-status", {
//           withCredentials: true,
//         });

//         if (response.data.session) {
//           setAuth((prev) => ({
//             ...prev,
//             isAuthenticated: true,
//             profileExists: true,
//             username: response.data.username || prev.username,
//             accessToken: response.data.accessToken || prev.accessToken,
//             userId: response.data.userId || prev.userId,
//           }));
//         } else {
//           throw new Error("Session not active");
//         }
//       } catch (error) {
//         console.error("Session validation error:", error);
//         setAuth({ isAuthenticated: false, user: null, accessToken: null });
//       } finally {
//         setIsLoading(false);
//       }
//     };

//     validateSession();
//   }, []);

//   const logout = useCallback(async () => {
//     try {
//       await axios.post("/logout-user", {}, { withCredentials: true });
//       setAuth({});
//     } catch (error) {
//       console.error("Logout error:", error);
//     }
//   }, []);

//   return (
//     <AuthContext.Provider value={{ auth, setAuth, isLoading, logout }}>
//       {children}
//     </AuthContext.Provider>
//   );
// };

// export default AuthContext;

import { createContext, useEffect, useState, useCallback } from "react";

import { auth as firebaseAuth } from "../firebase";

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    username: null,
    accessToken: null,
    userId: null,
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = firebaseAuth.onAuthStateChanged(async (user) => {
      if (user) {
        const token = await user.getIdToken();
        setAuth({
          isAuthenticated: true,
          username: user.displayName,
          accessToken: token,
          userId: user.uid,
        });
      } else {
        setAuth({
          isAuthenticated: false,
          username: null,
          accessToken: null,
          userId: null,
        });
      }
      setIsLoading(false);
    });

    return () => unsubscribe();
  }, []);

  const logout = useCallback(async () => {
    try {
      await firebaseAuth.signOut();
      setAuth({});
    } catch (error) {
      console.error("Logout error:", error);
    }
  }, []);
  return (
    <AuthContext.Provider value={{ auth, setAuth, isLoading, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
export default AuthContext;
