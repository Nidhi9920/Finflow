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
    public String generateToken(String email){
        SecretKey key = Keys.hmacShaKeyFor(
          secretKey.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60)) //Expiration in 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmail(String token){
        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
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
}
