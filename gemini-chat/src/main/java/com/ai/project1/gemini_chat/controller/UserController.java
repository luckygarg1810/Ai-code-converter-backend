package com.ai.project1.gemini_chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.auth.UserDetailsDto;
import com.ai.project1.gemini_chat.service.UserService;


@RestController
public class UserController {

	private UserService customUserDetailsService;
	
	public UserController(UserService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	
	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
	    try {
	        UserDetailsDto userDetails = customUserDetailsService.getUserDetails(userId);
	        return ResponseEntity.ok(userDetails);
	    } catch (Exception e) {
	        // Log the exception for debugging
	        // e.g., logger.error("Error fetching user details", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching user details: " + e.getMessage());
	    }
	}
	
	
	
}
