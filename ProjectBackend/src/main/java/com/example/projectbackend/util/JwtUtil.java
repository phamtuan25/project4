package com.example.projectbackend.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final String secret = "secret";

    public String generateJwtToken(Authentication authentication) {
        String email = authentication.getName();
        long expiration = 3600000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Boolean validateToken(String token){
        String msg;
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e){
            msg = "Invalid JWT Signature";
        } catch (MalformedJwtException e){
            msg = "Invalid JWT Token";
        } catch (ExpiredJwtException e){
            msg = "Expired JWT Token";
        } catch (UnsupportedJwtException e){
            msg = "Unsupported JWT Token";
        } catch (IllegalArgumentException e){
            msg = "JWT claims string is empty";
        }
        throw new AccessDeniedException(msg);
    }
}
