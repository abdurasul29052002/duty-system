package com.example.dutysystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {

    private final long validityPeriod = 1000*60*60*24;
    private final String secretKey = "ThisIsSecretWord123456789";

    public String generateToken(String username){
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+validityPeriod))
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
