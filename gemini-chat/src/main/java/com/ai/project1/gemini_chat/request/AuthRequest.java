package com.ai.project1.gemini_chat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

	private String password;
	private String fullName;
	private String username;
	private String email;
    

}
