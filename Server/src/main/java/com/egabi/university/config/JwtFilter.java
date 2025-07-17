package com.egabi.university.config;

import com.egabi.university.entity.authentication.User;
import com.egabi.university.service.authentication.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserService userService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        // Validate that the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extract the JWT from the Authorization header
        jwt = authHeader.substring(7);
        
        // Extract the username from the JWT
        userEmail = jwtService.extractUsername(jwt);
        
        // If the userEmail is not null and there is no authentication in the security context,
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, user)) {
                // Create an authentication token with user details
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                
                // Set the details of the authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
