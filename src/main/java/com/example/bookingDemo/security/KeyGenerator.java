package com.example.bookingDemo.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Component
@Slf4j
public class KeyGenerator {

    public String getBase64SecretKey(){
        //Generate Secure Key for HS256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        //Encode the key in Base64 Format for safe storage or transmission
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        return base64Key;
    }

}
