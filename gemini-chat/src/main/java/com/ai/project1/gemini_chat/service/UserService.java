package com.ai.project1.gemini_chat.service;

import java.time.LocalDate;
import java.util.ArrayList;

import com.ai.project1.gemini_chat.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ai.project1.gemini_chat.request.UserDetailsDto;
import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	    }

	 
	    public UserDetailsDto getUserDetails(Long userId) {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        UserDetailsDto dto = new UserDetailsDto();
	        dto.setId(user.getId());
	        dto.setFullName(user.getFullName());
	        dto.setUsername(user.getUsername());
	        dto.setEmail(user.getEmail());
	        dto.setPlanType(user.getPlanType().toString());
	        dto.setUsedTokens(user.getUsedTokens());
	        dto.setPlanExpiryDate(user.getPlanExpiryDate());
	        
	        return dto;
	    }

		public User getUserFromToken(String token){
			if (!jwtTokenProvider.validateToken(token)) {
				throw new RuntimeException("Invalid or expired token");
			}

			String username = jwtTokenProvider.getUsernameFromToken(token);
			return userRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));
		}

	    public User findUserById(Long userId) {
	        return userRepository.findById(userId).orElse(null);  // Returns null if user is not found
	    }
	    
	    public User findByEmail(String email) {
	        return userRepository.findByEmail(email).orElse(null);
	    }

		@Transactional
	    public void updateUserPlan(Long userId, PlanType planType, boolean isAnnual) {
			if (userId == null || planType == null) {
				throw new IllegalArgumentException("User ID and plan type cannot be null");
			}

	    	 User user = userRepository.findById(userId)
	                 .orElseThrow(() -> new RuntimeException("User not found"));

			LocalDate newExpiryDate = calculatePlanExpiryDate(isAnnual);

	        if (!planType.equals(user.getPlanType()) || !newExpiryDate.equals(user.getPlanExpiryDate())) {
	            user.setPlanType(planType);// Update the user's plan
	            user.setPlanExpiryDate(newExpiryDate);
	            userRepository.save(user);    // Save the updated user back to the database
	        }
	    }

		private LocalDate calculatePlanExpiryDate(boolean isAnnual){
			LocalDate currentDate = LocalDate.now();

			if(isAnnual) {
				return currentDate.plusYears(1);
			}
			else {
				return currentDate.plusMonths(1);
			}
		}
	    
	    public User updateUserProfile(Long userId, String newUsername, String newFullName) {
			if (userId == null) {
				throw new IllegalArgumentException("User ID cannot be null");
			}
	    	User user = userRepository.findById(userId)
	    			.orElseThrow(() -> new RuntimeException("User Not found"));
	    	
	    	if(newUsername != null && !newUsername.isEmpty() && !user.getUsername().equals(newUsername)) {
	    		if(userRepository.findByUsername(newUsername).isPresent()) {
	    			  throw new RuntimeException("Username already taken");
	    		}
	    		user.setUsername(newUsername);
	    	}
			if(newFullName != null && !newFullName.isEmpty() && !user.getFullName().equals(newFullName)) {
				user.setFullName(newFullName);
			}
	    	return userRepository.save(user);
	    }
	    
}
