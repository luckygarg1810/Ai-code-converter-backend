package com.ai.project1.gemini_chat.auth;

import java.time.LocalDate;

public class UserDetailsDto {

	    private Long id;
	    private String fullName;
	    private String username;
	    private String email;
	    private String planType;
	    private int usedTokens;
	    private LocalDate planExpiryDate;

	    public UserDetailsDto() {
	    }
	    
	    // Getters and setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPlanType() {
	        return planType;
	    }

	    public void setPlanType(String planType) {
	        this.planType = planType;
	    }

	    public int getUsedTokens() {
	        return usedTokens;
	    }

	    public void setUsedTokens(int usedTokens) {
	        this.usedTokens = usedTokens;
	    }

		public LocalDate getPlanExpiryDate() {
			return planExpiryDate;
		}

		public void setPlanExpiryDate(LocalDate planExpiryDate) {
			this.planExpiryDate = planExpiryDate;
		}
	}
	

