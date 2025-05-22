package com.ai.project1.gemini_chat.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class RazorpayService {

	private final RazorpayClient razorpayClient;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Value("${razorpay.key_id}")
    private String keyId;
    
    // Constructor initializing the Razorpay client with keyId and keySecret
    public RazorpayService(@Value("${razorpay.key_id}") String keyId,
                           @Value("${razorpay.key_secret}") String keySecret) throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
        this.keySecret = keySecret;
    }

    // Create an order using RazorpayClient
    public JSONObject  createOrder(double amount, String currency) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", (int) (amount * 100));  // Convert to paise (as Razorpay expects the amount in paise)
        options.put("currency", currency);
        options.put("receipt", "txn_" + System.currentTimeMillis());
        options.put("payment_capture", 1);  // 1 for auto-capture payment

        Order order = razorpayClient.orders.create(options); // Correct method to create an order
        
        JSONObject response = new JSONObject();
        response.put("orderId", order.get("id").toString()); // Get the order ID from the Razorpay response
        response.put("key", keyId);  // Get the Razorpay key from the client

        return response;
    }


    // Verify the payment signature
    public boolean verifyPaymentSignature(String paymentId, String orderId, String signature) throws Exception {
        JSONObject attributes = new JSONObject();
        
        attributes.put("razorpay_payment_id", paymentId);
        attributes.put("razorpay_order_id", orderId);
        attributes.put("razorpay_signature", signature);
        
        return Utils.verifyPaymentSignature(attributes, keySecret);  // Correct verification method
    }
}
