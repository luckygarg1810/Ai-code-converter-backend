package com.ai.project1.gemini_chat.controller;

import java.util.Map;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ai.project1.gemini_chat.database.PlanType;
import com.ai.project1.gemini_chat.service.RazorpayService;
import com.ai.project1.gemini_chat.service.UserService;


@RestController
@RequestMapping("/api/payment")
public class UpgradeController {

	@Autowired
	private RazorpayService razorpayService;
	@Autowired
	private UserService userService;	
	
	@PostMapping("/razorpay")
	public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request ){
		try {
			double amount = Double.parseDouble(request.get("amount").toString());
			String currency = request.get("currency").toString();
			System.out.println(amount);
			System.out.println(currency);
			JSONObject order = razorpayService.createOrder(amount, currency);
			System.out.println(order.toString());
			return ResponseEntity.ok(order.toString());
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error creating Razorpay order: " + e.getMessage());
		}
	}
	
	@PostMapping("/upgrade/{userId}")
    public ResponseEntity<?> upgradePlan(@PathVariable Long userId
    		, @RequestBody Map<String, Object> paymentDetails){
		try {
			    String paymentId = paymentDetails.get("razorpay_payment_id").toString();
	            String orderId = paymentDetails.get("razorpay_order_id").toString();
	            String signature = paymentDetails.get("razorpay_signature").toString();
	            boolean isAnnual = Boolean.parseBoolean(paymentDetails.get("isAnnual").toString());
	            String planTypeString = (String) paymentDetails.get("planType"); 
	            
	            System.out.println("Payment ID: " + paymentId);
	            System.out.println("Order ID: " + orderId);
	            System.out.println("Signature: " + signature);  // Check if it prints
	            
	            PlanType planType = PlanType.valueOf(planTypeString.toUpperCase());
	            
	     boolean isPaymentValid = razorpayService.verifyPaymentSignature(paymentId, orderId, signature);
	         
	     if(isPaymentValid) {
	    	userService.updateUserPlan(userId, planType, isAnnual);
	    	return ResponseEntity.ok("Plan upgraded successfully to " + planType);
	     }
	     else {
	    	 return ResponseEntity.badRequest().body("Invalid payment signature");
	     }
	            
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error upgrading plan: " + e.getMessage());
		}
	}
}
