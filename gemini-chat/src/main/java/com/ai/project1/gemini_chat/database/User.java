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

@Entity
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
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain at least 1 letter and 1 numeric digit"
    )
	@Column(nullable = false)
	private String password;

	 @Enumerated(EnumType.STRING)
	  @Column(nullable = false)
	 private PlanType planType;
	 
    @Column(nullable = false)
    private int usedTokens =0;

    private LocalDate planExpiryDate;
    
    @Column(nullable = true)
    private String provider;
    
    @Column(nullable = true, unique = true)
    private String providerId;
    
    
    public User( String fullName, String username, String email, String password,
    		PlanType planType, int usedTokens) {
		super();
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.planType = planType;
		this.usedTokens = usedTokens;
	}
    
    public User(String fullName, String email, String provider, String providerId, PlanType planType) {
        this.fullName = fullName;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.planType = planType;
        this.username = email.split("@")[0];
        this.password = email+"oauth_user";
    }

    
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", username=" + username + ", email=" + email
				+ ", password=" + password + "]";
	}

	public User() {
	}

	public PlanType getPlanType() {
		return planType;
	}

	public void setPlanType(PlanType planType) {
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

}
