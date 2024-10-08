package com.SecurityDemo.jwttest.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = "305c300d06092a864886f70d0101010500034b003048024100c3f9eeeb416425606f7604dd2185cea32306a8d183224047049d45c4ca9a9b834851b07df48d09ad1003d7faf01be9623c50710737ac89030e12da0787f294ef0203010001";
    ;

// -------------------- Extract the main parameter (user Email, userName)from the token
//  3-  method to get the main parameter in the token (email or userName) which called subject
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    2- method to exctract all the parameters saved in the token (req for method 3)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    1- method to exctract all the parameters saved in the token (req for method 2)
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // note , some of those methode deprecated in v 12.6
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

// min size id 256 bit
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    --------------------- Generate Token Key ------------------------
//   1- Method if need to pass only the user details with no extra data
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

//    2- Method for generate the token key valid for 24h
    public String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // subject is the main parameter in the jwt which here is the Email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

// ---------------------- Token Validation & Expiration Check --------------------

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
