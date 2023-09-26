package com.appsdeveloperblog.shopify.clients.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private static final String SECRET = "5367566B5970337336763979244226452948404D6351655468576D5A71347437";
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T>T extractClaim(String token, Function<Claims, T>claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    // generate token
    public String generateToken(String shopName){
        System.out.println(shopName);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, shopName);
    }
    private String createToken(Map<String, Object> claims, String shopName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(shopName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*60))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }
}
