import React, { useState, useEffect } from "react";
import LoginForm from "./pages/LoginForm";
import MainPage from "./pages/MainPage";
import NotFoundPage from "./pages/NotFoundPage";
import SessionManager from "./session/SessionManager";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";

const App: React.FC = () => {

  const [token, setToken] = useState<string | null>(localStorage.getItem("authToken"));

  useEffect(() => {
    const handleStorageChange = () => {
      setToken(localStorage.getItem("authToken"));
    };
    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  return (
    <Router>
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
        <Route path="*" element={<Navigate to="/auth/login" />} />
      </Routes>
    </Router>
  );
};

export default App;
