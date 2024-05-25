import { useLocation, Navigate, Outlet } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const RequireAuth = () => {
  const { auth, isLoading } = useAuth();
  console.log("RequireAuth rendering, auth:", auth);
  const location = useLocation();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return auth?.isAuthenticated ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};
export default RequireAuth;
