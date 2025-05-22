package com.ai.project1.gemini_chat.database;

import java.time.LocalDate;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@NotBlank(message = "Full name is required")
	@Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Full name can only contain letters and spaces")
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
	@Column(unique = true, nullable = false)
	private String username;
	
	
	@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
	@Column(unique = true, nullable = false)
	private String email;
	
	@NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters long")
	@Column(nullable = false)
	private String password;

	 @Enumerated(EnumType.STRING)
	  @Column(nullable = false)
	 private PlanType planType;
	 
    @Column(nullable = false)
    private int usedTokens =0;

	@Column
    private LocalDate planExpiryDate;

	public User(String fullName, String username, String email, String encode, PlanType free, int usedTokens,
				LocalDate planExpiryDate) {

	}
}
