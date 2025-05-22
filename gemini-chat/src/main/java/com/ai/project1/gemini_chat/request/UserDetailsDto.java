package com.ai.project1.gemini_chat.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

	    private Long id;
	    private String fullName;
	    private String username;
	    private String email;
	    private String planType;
	    private int usedTokens;
	    private LocalDate planExpiryDate;

	    
	}
	

