package com.example.roster.security;

import org.springframework.stereotype.Component;

import com.example.roster.config.JWTConfig;
import com.example.roster.entity.Role;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private final Key key;
    private final long expiration;

    public JWTUtil(JWTConfig config) {
        this.key=Keys.hmacShaKeyFor(config.getSecret().getBytes());
        this.expiration=config.getExpiration();
    }
    public String generateToken(Long userId,Role role,Long organizationId) {
        return Jwts.builder().
                setSubject(userId.toString()).
                claim("role", role.name())
                .claim("orgId", organizationId)
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key)
                .compact();
    }
    public Claims validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
