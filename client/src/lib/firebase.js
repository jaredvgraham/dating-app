// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import {
  getAuth,
  setPersistence,
  browserLocalPersistence,
} from "firebase/auth";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyAOYxfWpARk3qRB0z2TMFN4qo9EIYVHqRY",
  authDomain: "dating-app-65513.firebaseapp.com",
  projectId: "dating-app-65513",
  storageBucket: "dating-app-65513.appspot.com",
  messagingSenderId: "188714578938",
  appId: "1:188714578938:web:1a38cdf305f1a70cce2a43",
  measurementId: "G-M5T7R90DKB",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Authentication and get a reference to the service
const auth = getAuth(app);

// Set auth persistence to local
setPersistence(auth, browserLocalPersistence)
  .then(() => {
    console.log("Auth persistence set to LOCAL");
  })
  .catch((error) => {
    console.error("Error setting auth persistence:", error);
  });

export { app, auth };
