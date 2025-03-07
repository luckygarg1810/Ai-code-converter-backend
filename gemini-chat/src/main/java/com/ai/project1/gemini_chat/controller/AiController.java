package com.ai.project1.gemini_chat.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.service.QnaService;

@RestController

@RequestMapping("/api/qna")
public class AiController {

	private final QnaService qnaService;
	
	@PostMapping("/ask")
	public ResponseEntity<String> askQuestions(@RequestBody Map<String, String> payload,  @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
	
		  String token = null;

	        // Extract token from Authorization header if present
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            token = authorizationHeader.replace("Bearer ", "");
	        }
		String question = payload.get("question");
		String answer = qnaService.getAnswer(question, token);
		return ResponseEntity.ok(answer);
	}

	public AiController(QnaService qnaService) {
		this.qnaService = qnaService;
	}
}
