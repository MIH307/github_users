import React, { useState, useEffect } from "react";
import LoginForm from "./pages/LoginForm";
import MainPage from "./pages/MainPage";
import NotFoundPage from "./pages/NotFoundPage";
import { BrowserRouter as Router, Route, Routes, useNavigate , Navigate } from "react-router-dom";


const App: React.FC = () => {

  const [token, setToken] = useState<string | null>(localStorage.getItem("authToken"));
  const [inactiveTime, setInactiveTime] = useState(0);
  const navigate = useNavigate();


  useEffect(() => {
    const handleActivity = () => setInactiveTime(0);
    window.addEventListener("mousemove", handleActivity);
    window.addEventListener("keydown", handleActivity);

    const interval = setInterval(() => {
      setInactiveTime((prev) => prev + 1);
      if (inactiveTime >= 5 * 60 && token) {
        alert("Session expired due to inactivity.");
        logout();
      }
    }, 1000);

    return () => {
      window.removeEventListener("mousemove", handleActivity);
      window.removeEventListener("keydown", handleActivity);
      clearInterval(interval);
    };
  }, [inactiveTime]);

  const logout = () => {
    localStorage.removeItem("authToken");
    setToken(null);
    navigate("/auth/login");
  };

  return (

    <Routes>
      <Route
        path="/auth/login"
        element={
          token ? <Navigate to="/github/users" /> : <LoginForm setToken={setToken} />
        }
      />
      <Route
        path="/github/users"
        element={
          token ? <MainPage /> : <Navigate to="/auth/login" />
        }
      />
      <Route
         path="/"
         element={
           token ? <MainPage /> : <Navigate to="/auth/login" />
         }
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default App;
