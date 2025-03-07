package com.ai.project1.gemini_chat.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.jwt.JwtTokenProvider;
import com.ai.project1.gemini_chat.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class QnaService {

	// Access to api key and URL [Gemini]
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	private final WebClient webClient;
	private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private int   guestTokensUsed = 0;
    private static final int GUEST_TOKEN_LIMIT = 10;

	public QnaService(WebClient.Builder webClient, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
		this.webClient = webClient.build();
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Transactional
	public String getAnswer(String question, String token) {
		

		    if (token == null || token.isEmpty()) {
	            // Handle guest user logic
	            if (guestTokensUsed >= GUEST_TOKEN_LIMIT) {
	                throw new RuntimeException("Guest token limit reached. Please log in or register for more access.");
	            }
	            guestTokensUsed++;
	        }
		    else {
		    	  if (!jwtTokenProvider.validateToken(token)) {
		                throw new RuntimeException("Invalid or expired token");
		            }

		            String username = jwtTokenProvider.getUsernameFromToken(token);
		            User user = userRepository.findByUsername(username)
		                    .orElseThrow(() -> new RuntimeException("User not found"));

		            // Check plan and token usage for authenticated users
		            if (user.getPlanType() == PlanType.FREE && user.getUsedTokens() >= 100) {
		                throw new RuntimeException("Free plan token limit reached. Please upgrade your plan.");
		            } 
		            
		            System.out.println(user.getPlanType());
		            // Increment token usage for free and base plans
		            if (user.getPlanType() != PlanType.SUPER || user.getPlanType() != PlanType.BASE) {
		            	
		                user.setUsedTokens(user.getUsedTokens() + 1);
		                userRepository.save(user);
		  
		            }
		    }
		   
	      
		// construct the request payload
		Map<String, Object> requestBody = Map.of("contents",
				new Object[] { 
		             Map.of("parts", new Object[] {
								Map.of("text", question) }) });
	
		
		// Make api call
		
		String response = webClient.post()
		         .uri(geminiApiUrl + geminiApiKey)
		         .header("Content-Type", "application/json" )
		         .bodyValue(requestBody)
		         .retrieve()
		         .bodyToMono(String.class)
		         .block();
		// return response
		return response;
	}

}
