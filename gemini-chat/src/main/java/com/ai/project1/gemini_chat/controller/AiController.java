package com.ai.project1.gemini_chat.controller;

import com.ai.project1.gemini_chat.request.QuestionRequest;
import com.ai.project1.gemini_chat.service.CodeConvertService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class AiController {

	private final CodeConvertService codeConvertService;
	
	@PostMapping("/convertCode")
	public ResponseEntity<String> askQuestions(@RequestBody QuestionRequest request,
											   @RequestHeader(value = "Authorization", required = false)
											   String authorizationHeader){

		String token = extractToken(authorizationHeader);
		String question = request.getQuestion();
		String answer = codeConvertService.getAnswer(question, token);
		return ResponseEntity.ok(answer);
	}

	private String extractToken(String header) {
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

}
