package com.example.projectbackend.config;

import com.example.projectbackend.handler.CustomAccessDenieHandler;
import com.example.projectbackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final CustomUserDetailService userDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDenieHandler accessDenieHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(8).split("\"")[0];
        }
        try {
            if(StringUtils.hasText(token) && jwtUtil.validateToken(token)){
                email = jwtUtil.getEmailFromJwtToken(token);
            }
            if(StringUtils.hasText(email) && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e){
            accessDenieHandler.handle(request, response, new AccessDeniedException(e.getLocalizedMessage(), e));
        }
    }
}
