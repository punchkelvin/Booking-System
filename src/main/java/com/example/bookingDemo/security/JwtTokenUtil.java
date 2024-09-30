package com.example.bookingDemo.security;

import com.example.bookingDemo.dto.authentication.AuthenticationRequest;
import com.example.bookingDemo.model.RefreshToken;
import com.example.bookingDemo.model.User;
import com.example.bookingDemo.repository.RefreshTokenRepository;
import com.example.bookingDemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {
    //TODO Monitor and Audit Access
        // Regularly monitor access logs and audit who access secret key
        // Use alerting system to detect unauthorized access to the key access
    @Value("${jwt.signing.key}")
    private String base64SigningKey;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    public SecretKey getJwtSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(base64SigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Generate Token for user
    public String generateAccessToken(String username){
        HashMap<String, Object> extraClaims = new HashMap<>(); //this can be used to store custom claim like role
        return createAccessToken(extraClaims, username);
    }

    private String createAccessToken(HashMap<String, Object> extraClaims, String subject){
        return Jwts.builder()
                .setClaims(extraClaims)
                //Anything below here is Registered Claims that follows JWTs standard
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 60)) // 1/2 hours from current time
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

    //Generate RefreshToken
    public RefreshToken generateRefreshToken(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + ", not found"));

        return createRefreshToken(user);
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken(UUID.randomUUID().toString())
                .createdDate(LocalDateTime.now())
                .expiryDate(Instant.now().plusMillis(86400000))
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public String getNewAccessTokenWithRefresh(String refreshToken){
        RefreshToken currentRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        //Check if the refresh token has expired
        log.info("Expiry Date: " + currentRefreshToken.getExpiryDate());
        log.info("Now: " + Instant.now());
        if(currentRefreshToken.getExpiryDate().isBefore(Instant.now())){
           throw new RuntimeException("Refresh Token has expired");
        }

        String newAccessToken = generateAccessToken(currentRefreshToken.getUser().getUsername());

        return newAccessToken;

    }

}
