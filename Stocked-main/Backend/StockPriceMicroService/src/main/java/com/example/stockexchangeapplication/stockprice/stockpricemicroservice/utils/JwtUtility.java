package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.utils;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.AppUser;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility {
    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 10;

    @Value("${jwt.secret}")
    private String secretKey;

    public AppUser getUserFromToken(String token) {
        Gson gson = new Gson();
        return gson.fromJson(getClaimFromToken(token, Claims::getSubject),AppUser.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        return doGenerateToken(claims, userJson);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Boolean validateToken(String token, AppUser userDetails) {
        final AppUser user = getUserFromToken(token);
        return (user.getEmail().equals(userDetails.getEmail()) && !isTokenExpired(token));
    }
}
