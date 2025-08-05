package com.example.SpringProjectApplication.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;


    @Value("${jwt.refreshExpiration}")
    private long jwtRefreshExpirationMs;

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        System.out.println("Extracting claims from token: " + token);
        final Claims claims = extractClaims(token);
        System.out.println("Extracted claims: " + claims);

        return claimsResolver.apply(claims);
    }


    private Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

    }


    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isRefreshToken(String jwt) {
        try {
            Claims claims = extractClaims(jwt);
            return "refresh".equals(claims.get("type"));
        } catch (JwtException e) {
            return false; // Invalid token
        }
    }

    //For testing purposes only
//    public String generateExpiredToken(User user , Map<String, Object> claims) {
//        return Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() - 1000)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
//    }


}
