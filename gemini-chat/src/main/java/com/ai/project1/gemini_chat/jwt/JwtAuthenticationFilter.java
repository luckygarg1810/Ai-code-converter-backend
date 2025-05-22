package com.ai.project1.gemini_chat.jwt;


import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/api/*")  // Apply filter to all API endpoints
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    private String extractJwtFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

	@Override
	protected void doFilterInternal(@NotNull jakarta.servlet.http.HttpServletRequest request,
									@NotNull jakarta.servlet.http.HttpServletResponse response, @NotNull jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {

		  String token = extractJwtFromRequest(request);

	        if (token != null && jwtTokenProvider.validateToken(token)) {
	            String username = jwtTokenProvider.getUsernameFromToken(token);

	            // Create authentication token
	            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }

	        filterChain.doFilter(request, response);
		
	}
}
