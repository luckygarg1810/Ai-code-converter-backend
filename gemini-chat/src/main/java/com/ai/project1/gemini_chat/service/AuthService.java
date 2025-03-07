package com.ai.project1.gemini_chat.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ai.project1.gemini_chat.auth.JwtResponse;
import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.jwt.JwtTokenProvider;
import com.ai.project1.gemini_chat.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthService {
 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	

	
	public JwtResponse login(String username, String password) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		if(!passwordEncoder.matches( password, user.getPassword())) {
			 throw new RuntimeException("Invalid credentials");
		}
		
		
		
		String token = tokenProvider.generateToken(user.getUsername());
		return new JwtResponse(token, user.getId());

	}
	
	
	 public void register(@Valid String fullName, @Valid String username,@Valid String email, @Valid String password) {
		 if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
	            throw new RuntimeException("Username or email already exists");
	        }
		 
		 User user = new User(fullName, username,email, passwordEncoder.encode(password), PlanType.FREE, 0);
		 
		 userRepository.save(user);
	 }
	 
	    public void logout() {
	        // Handle logout, such as invalidating JWT on the client-side.
	    }
	    
	    
	    @Transactional
	    public JwtResponse loginWithOAuth(String email, String fullName, String provider, String providerId) {
	        // Check if the user already exists by email and provider
	        User user = userRepository.findByEmailAndProvider(email, provider).orElse(null);

	        if (user == null) {
	            // If no user exists, create a new one
	            user = new User(fullName, email, provider, providerId, PlanType.FREE);
	            userRepository.save(user);
	        }

	        // Generate a JWT token for the user
	        String token = tokenProvider.generateToken(email);

	        // Return the token and user ID
	        return new JwtResponse(token, user.getId());
	    }


}
