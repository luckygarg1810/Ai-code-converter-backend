package com.ai.project1.gemini_chat.service;

import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.database.User;
import com.ai.project1.gemini_chat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ConversionService {

	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	private final WebClient webClient;
	private final UserService userService;
    private final UserRepository userRepository;
    private int guestTokensUsed = 0;
	private static final int GUEST_TOKEN_LIMIT = 10;
	private static final int FREE_PLAN_LIMIT = 100;
	private static final int BASE_PLAN_LIMIT = 100;

	public ConversionService(WebClient.Builder webClient, UserService userService, UserRepository userRepository) {
		this.webClient = webClient.build();
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Transactional
	public String getAnswer(String question, String jwtToken) {
		handleTokensUsage(jwtToken);

		Map<String, Object> requestBody = Map.of("contents",
				new Object[] { 
		             Map.of("parts", new Object[] {
								Map.of("text", question) }) });

		return webClient.post()
		         .uri(geminiApiUrl + geminiApiKey)
		         .header("Content-Type", "application/json" )
		         .bodyValue(requestBody)
		         .retrieve()
		         .bodyToMono(String.class)
		         .block();
	}

	private void handleTokensUsage(String jwtToken) {
		if (jwtToken == null || jwtToken.isEmpty()) {
			handleGuestUser();
		} else {
			handleAuthenticatedUser(jwtToken);
		}
	}

	private void handleGuestUser() {
		if (guestTokensUsed >= GUEST_TOKEN_LIMIT) {
			throw new RuntimeException("Guest token limit reached. Please log in or register for more access.");
		}
		guestTokensUsed++;
	}

	private void handleAuthenticatedUser(String jwtToken) {
		User user = userService.getUserFromToken(jwtToken);
		PlanType planType = user.getPlanType();

		switch (planType) {
			case FREE -> {
				if (user.getUsedTokens() >= FREE_PLAN_LIMIT) {
					throw new RuntimeException("Free plan token limit reached. Please upgrade your plan.");
				}
				incrementTokenUsage(user);
			}
			case BASE -> {
				if (user.getUsedTokens() >= BASE_PLAN_LIMIT) {
					throw new RuntimeException("Base plan token limit reached. Please upgrade your plan.");
				}
				incrementTokenUsage(user);
			}
			case SUPER -> {
				// Unlimited usage - do nothing
			}
			default -> throw new RuntimeException("Unknown plan type.");
		}
	}

	private void incrementTokenUsage(User user) {
		user.setUsedTokens(user.getUsedTokens() + 1);
		userRepository.save(user);
	}
}
