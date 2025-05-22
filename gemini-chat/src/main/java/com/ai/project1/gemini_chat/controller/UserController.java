package com.ai.project1.gemini_chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.request.UpdateUserProfileRequest;
import com.ai.project1.gemini_chat.request.UserDetailsDto;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.service.UserService;


@RestController
public class UserController {

	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	
	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
	    try {
	        UserDetailsDto userDetails = userService.getUserDetails(userId);
	        return ResponseEntity.ok(userDetails);
	    } catch (Exception e) {
	        // Log the exception for debugging
	        // e.g., logger.error("Error fetching user details", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching user details: " + e.getMessage());
	    }
	}
	
	@PutMapping("/users/{userId}/update")
	public ResponseEntity<?> updateUserProfile(@PathVariable Long userId,
			@RequestBody UpdateUserProfileRequest request){
		
		try {
			 User updatedUser = userService.updateUserProfile(userId, request.getUsername(), request.getFullName());
	            return ResponseEntity.ok(updatedUser);
		}
		catch(RuntimeException e){
			   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
}
