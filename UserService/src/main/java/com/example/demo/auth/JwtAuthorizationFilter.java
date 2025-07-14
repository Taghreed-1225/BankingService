package com.example.demo.auth;


import com.example.demo.Service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // تخطي الفلتر لبعض المسارات
        String path = request.getServletPath();
        if (path.startsWith("/activateUser") || path.startsWith("/regenerateOtp") || path.startsWith("/rest/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");

            // التحقق من وجود الـ header وصيغته
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing Authorization header");
                return;
            }

            String accessToken = authHeader.substring(7); // استخراج الـ token بعد "Bearer "

            // التحقق من صحة الـ token
            Claims claims = jwtService.resolveClaims(request);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }

            String userEmail = claims.getSubject();
            Integer userId = (Integer) claims.get("userId");

            if (userEmail == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT claims");
                return;
            }

            request.setAttribute("userId", userId);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (userDetails != null && jwtService.isTokenValid(accessToken, userDetails)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("JWT processing error", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error processing JWT token");
        }
    }
}