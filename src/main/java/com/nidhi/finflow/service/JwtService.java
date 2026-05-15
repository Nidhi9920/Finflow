package com.nidhi.finflow.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    public String generateToken(String email, String role){
        SecretKey key = Keys.hmacShaKeyFor(
          secretKey.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60)) //Expiration in 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }
    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }
    public boolean isTokenValid(String token){
        //To prevents app crash try-catch is used
        try {
            extractEmail(token);
            return true;
        }catch (Exception e){
            //Exception can happen - invalid signature, expired token, malformed token, missing token
            return false;
        }
    }
    public Claims extractAllClaims(String token){
        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
