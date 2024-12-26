package com.example.githubusers.web.rest;

import com.example.githubusers.data.entities.UserEntity;
import com.example.githubusers.services.AuthService;
import com.example.githubusers.services.JwtService;
import com.example.githubusers.web.models.LoginResponse;
import com.example.githubusers.web.models.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUser loginUser) {
        UserEntity authenticatedUser = authService.authenticateUser(loginUser);

        String jwtToken = jwtService.generateToken(authenticatedUser.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.expirationTime(jwtToken));

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        String token = jwtService.extractTokenFromRequest(request);

        if (token != null) {
            jwtService.addTokenToBlacklist(token);
           // return ResponseEntity.ok("Logout successful");
        }

        response.sendRedirect("/auth/login");
    }

}
