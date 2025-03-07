package com.ai.project1.gemini_chat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.auth.AuthRequest;
import com.ai.project1.gemini_chat.auth.JwtResponse;
import com.ai.project1.gemini_chat.auth.LoginRequest;
import com.ai.project1.gemini_chat.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
	  try {
		  JwtResponse jwtResponse =  authService.login(loginRequest.getUsername(), loginRequest.getPassword());
		  return ResponseEntity.ok(jwtResponse);
	  }
		
		
		
		catch (Exception e) {
	        return ResponseEntity.status(401).body("Invalid credentials: " + e.getMessage());
	    }
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody AuthRequest authRequest){
		authService.register(authRequest.getFullName(), authRequest.getUsername(),
				authRequest.getEmail(), authRequest.getPassword());
		
		return ResponseEntity.status(201).body("User Registered Successfully");
	}
	
	
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok("Logged out successfully.");
    }
	
}
