package com.example.bookingDemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {
    //TODO Monitor and Audit Access
        // Regularly monitor access logs and audit who access secret key
        // Use alerting system to detect unauthorized access to the key access
    @Value("${jwt.signing.key}")
    private String base64SigningKey;

    public SecretKey getJwtSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(base64SigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Generate Token for user
    public String generateToken(String username){
        HashMap<String, Object> extraClaims = new HashMap<>(); //this can be used to store custom claim like role
        return createToken(extraClaims, username);
    }

    private String createToken(HashMap<String, Object> extraClaims, String subject){
        return Jwts.builder()
                .setClaims(extraClaims)
                //Anything below here is Registered Claims that follows JWTs standard
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours from current time
                .signWith(getJwtSigningKey(), SignatureAlgorithm.HS256)
                .compact(); //finalize the creation of jwt
    }

    //Extract username from Token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Generic Claims Extraction
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        //A parser for reading JWT strings, used to convert them into a Jwt object representing the expanded JWT.
        return Jwts.parserBuilder()
                .setSigningKey(getJwtSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Check if the token has expired
    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    //Validate Token
    public boolean validateToken(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

}
