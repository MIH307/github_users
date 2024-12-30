import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { TextField, Button, Typography, Box, Alert } from "@mui/material";
import axiosInstance from "../axiosConfig";

interface LoginFormProps {
  setToken: (token: string | null) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ setToken }) => {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      const response = await axiosInstance.post("/auth/login", { username, password });
      const token = response.data.token;
      localStorage.setItem("authToken", token);
      setToken(token);
      navigate('/github/users');
    } catch (err) {
      setError("Invalid username or password");
    }
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="100vh"
    >
      <Typography variant="h4" mb={2}>
        Login
      </Typography>
      {error && (
        <Alert severity="error" onClose={() => setError(null)}>
          {error}
        </Alert>
      )}
      <Box component="form" onSubmit={handleSubmit} width="100%" maxWidth="400px">
        <TextField
          label="Username"
          variant="outlined"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <TextField
          label="Password"
          type="password"
          variant="outlined"
          fullWidth
          margin="normal"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          sx={{ mt: 2 }}
        >
          Login
        </Button>
      </Box>
    </Box>
  );
};

export default LoginForm;
