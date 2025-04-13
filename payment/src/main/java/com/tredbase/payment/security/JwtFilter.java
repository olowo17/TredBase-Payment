package com.tredbase.payment.security;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.tredbase.payment.utils.GeneralConstants.HEADER_STRING;
import static com.tredbase.payment.utils.GeneralConstants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthUserService authUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HEADER_STRING);
        final String jwtToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
     try {
         userEmail = jwtService.extractUsername(jwtToken);
         if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
             UserDetails userDetails = this.authUserService.loadUserByUsername(userEmail);
             if (jwtService.validateToken(jwtToken, userDetails)) {
                 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                         userDetails, null, userDetails.getAuthorities()
                 );
                 authenticationToken.setDetails(
                         new WebAuthenticationDetailsSource().buildDetails(request)
                 );
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             }
         }
         filterChain.doFilter(request, response);
     }catch (SignatureException e) {
            // Handle invalid signature exception
            logger.error("Invalid JWT signature: " + e.getMessage(), e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"message\": \"Missing or Invalid JWT signature.\"}");
        } catch (Exception e) {
            // Handle other unexpected exceptions
            logger.error("Authentication error: " + e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("{\"message\": \"An error occurred while processing the request.\"}");
        }
    }
}