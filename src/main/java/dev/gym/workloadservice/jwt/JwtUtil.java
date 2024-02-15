package dev.gym.workloadservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Setter
@Getter
@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secret;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public void validateToken(String token) {
        Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseSignedClaims(token);
    }

}
