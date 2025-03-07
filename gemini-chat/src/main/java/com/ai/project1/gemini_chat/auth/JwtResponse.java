package com.ai.project1.gemini_chat.auth;

public class JwtResponse {
	private String token;
	private Long id;

    public JwtResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    // Getter
    public String getToken() {
        return token;
    }
    
    public Long getId() {
    	return id;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
