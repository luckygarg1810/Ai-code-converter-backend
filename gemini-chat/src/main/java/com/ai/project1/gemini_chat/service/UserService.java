package com.ai.project1.gemini_chat.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ai.project1.gemini_chat.auth.UserDetailsDto;
import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
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
	    
	    public User findUserById(Long userId) {
	        return userRepository.findById(userId).orElse(null);  // Returns null if user is not found
	    }
	    
	    public User findByEmail(String email) {
	        return userRepository.findByEmail(email).orElse(null);
	    }

	    public User findByEmailAndProvider(String email, String provider) {
	        return userRepository.findByEmailAndProvider(email, provider).orElse(null);
	    }

	    public void updateUserPlan(Long userId, PlanType planType, boolean isAnnual) {
	    	 User user = userRepository.findById(userId)
	                 .orElseThrow(() -> new RuntimeException("User not found"));
	    	 
	    	 LocalDate currentDate = LocalDate.now();
	    	 LocalDate newExpiryDate;
	    	 
	    	 if(isAnnual) {
	    		 newExpiryDate = currentDate.plus(1, ChronoUnit.YEARS);
	    	 }
	    	 else {
	    		 newExpiryDate = currentDate.plus(1, ChronoUnit.MONTHS);
	    	 }
	    	 
	        if (user != null) {
	            user.setPlanType(planType);// Update the user's plan
	            user.setPlanExpiryDate(newExpiryDate);
	            userRepository.save(user);    // Save the updated user back to the database
	        }
	    }
}
