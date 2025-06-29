package com.noor.auth.security;

import com.noor.auth.service.JwtService;
import com.noor.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                System.out.println("🔐 JWT Token: " + token);

                String username = jwtService.extractUserName(token);
                String role = jwtService.extractRole(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                            username,
                            "",
                            Collections.singleton(new SimpleGrantedAuthority(role))
                    );

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (ExpiredJwtException e) {
                System.err.println("❌ Token expired: " + e.getMessage());
            } catch (UnsupportedJwtException e) {
                System.err.println("❌ Unsupported token: " + e.getMessage());
            } catch (MalformedJwtException e) {
                System.err.println("❌ Malformed token: " + e.getMessage());
            } catch (SecurityException e) {
                System.err.println("❌ Invalid signature: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Illegal argument token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}