package com.ai.project1.gemini_chat.controller;


import com.ai.project1.gemini_chat.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.request.AuthRequest;
import com.ai.project1.gemini_chat.request.LoginRequest;
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
		 LoginResponse response =  authService.login(loginRequest.getUsername(), loginRequest.getPassword());
		  return ResponseEntity.ok(response);
	  }
		
		catch (Exception e) {
	        return ResponseEntity.status(401).body("Invalid credentials: " + e.getMessage());
	    }
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody AuthRequest authRequest){
		try {
			authService.register(authRequest.getFullName(), authRequest.getUsername(),
					authRequest.getEmail(), authRequest.getPassword());

			return ResponseEntity.status(201).body("User Registered Successfully");
		}catch (Exception e){
			return ResponseEntity.status(500).body("User Registration Failed: "+ e.getMessage());
		}
	}
	
}
