package com.online_e_learning.virtualPathshala.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JWT Token Provider - Handles JWT token generation, validation, and parsing
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret:virtualPathshalaSecretKey2024ForJWTTokenGenerationWithSpringSecurity}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private int jwtExpirationInMs;

    /**
     * Generates a signing key from the secret
     *
     * @return SecretKey for JWT signing
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for authenticated user
     *
     * @param authentication the Spring Security authentication object
     * @return JWT token string
     */
    public String generateToken(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

            // Extract roles from authorities
            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            String token = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .claim("roles", roles)
                    .claim("type", "ACCESS")
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();

            logger.info("JWT token generated for user: {} with roles: {}", email, roles);
            return token;

        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Generates token with extra claims
     */
    public String generateTokenWithClaims(String email, Map<String, Object> claims) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token with claims: {}", e.getMessage());
            throw new RuntimeException("Failed to generate JWT token with claims", e);
        }
    }

    /**
     * Extracts email from JWT token
     *
     * @param token the JWT token
     * @return email address from token subject
     */
    public String getEmailFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts roles from JWT token
     */
    public String getRolesFromJWT(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    /**
     * Extracts expiration date from JWT token
     */
    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("Error extracting claim from JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to extract claim from JWT token", e);
        }
    }

    /**
     * Extracts all claims from token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Error extracting claims from JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to extract claims from JWT token", e);
        }
    }

    /**
     * Validates JWT token
     *
     * @param authToken the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);

            logger.debug("JWT token validated successfully");
            return true;

        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error validating JWT token: {}", ex.getMessage());
        }

        return false;
    }

    /**
     * Checks if token is expired
     *
     * @param token the JWT token
     * @return true if token is expired
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }

    /**
     * Gets remaining time until token expiration in milliseconds
     *
     * @param token the JWT token
     * @return remaining time in ms, or -1 if invalid
     */
    public long getRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration != null) {
                return expiration.getTime() - System.currentTimeMillis();
            }
        } catch (Exception e) {
            logger.error("Error calculating remaining time: {}", e.getMessage());
        }
        return -1;
    }

    /**
     * Gets the issued date from JWT token
     *
     * @param token the JWT token
     * @return issued date
     */
    public Date getIssuedDateFromToken(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Refresh token (create new token with same claims but new expiration)
     */
    public String refreshToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh token", e);
        }
    }
}