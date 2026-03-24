package com.example.roster.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public JWTAuthFilter(JWTUtil jwt, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwt;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String sessionId=authHeader.substring(7);
        String token = (String)redisTemplate.opsForValue().get("session:"+sessionId);

        if (token == null) {
            // Session not found or expired
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        System.out.println(token);


            try {
                Claims claims = jwtUtil.validateToken(token);
                String role = claims.get("role", String.class);
                Long orgId=claims.get("orgId",Long.class);
                Long userId = Long.parseLong(claims.getSubject());

                request.setAttribute("orgId", orgId);
                request.setAttribute("role", role);
                request.setAttribute("userId",userId);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String path=req.getRequestURI();
        System.out.println(path);
        return path.startsWith("/api/roster/user");
    }
}
