package com.iwantodo.infra.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    public static String generateToken(User user) {
        long expirationTime = 1000 * 60 * 60;
        return Jwts
                .builder()
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigninKey())
                .compact();
    }

    public static Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean isTokenValid(String token) {
        return !isExpired(token);
    }

    public static boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public static SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode("QPhLG9Kcjhl8ZobgvvBmTsrCyW0qjT2QOm73LogLfA8jsd6n3z");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public static String extractToken(String header) {
        System.out.println("Auth Header: " + header);
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        throw new IllegalArgumentException("Invalid Auth Header");
    }
}
