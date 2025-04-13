package com.tredbase.payment.security;

import com.tredbase.payment.entities.Admin;
import com.tredbase.payment.entities.BaseUser;
import com.tredbase.payment.entities.Parent;
import com.tredbase.payment.entities.Student;
import com.tredbase.payment.entities.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.tredbase.payment.utils.GeneralConstants.EXPIRATION_TIME;
import static com.tredbase.payment.utils.GeneralConstants.HEADER_STRING;
import static com.tredbase.payment.utils.GeneralConstants.TOKEN_PREFIX;


@Service
@Slf4j
public class JwtService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateJwtToken(BaseUser user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getFirstname() + " " + user.getLastname());
        claims.put("phoneNumber", user.getPhoneNumber());

        if (user instanceof Parent) {
            claims.put("userType", Role.ROLE_PARENT.getRole());
        } else if (user instanceof Student) {
            claims.put("userType", Role.ROLE_STUDENT.getRole());
        } else if (user instanceof Admin) {
            claims.put("userType", Role.ROLE_ADMIN.getRole());
        }
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            log.error("Invalid JWT token");
            throw ex;
        }
    }


    public Optional<String> retrieveJWT(HttpServletRequest request) {
        if (hasBearerToken(request)) {
            return Optional.of(retrieveToken(request).substring(7));
        }
        return Optional.empty();
    }

    public boolean hasBearerToken(HttpServletRequest request) {
        String token = retrieveToken(request);
        return StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX);
    }

    private String retrieveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_STRING);
    }

}