package lsgwr.exam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;

    public JwtService(@Value("${app.jwt-secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            bytes = (secret + "0123456789abcdef0123456789abcdef").getBytes(StandardCharsets.UTF_8);
        }
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String issue(CurrentUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.id())
                .claim("username", user.username())
                .claim("roleId", user.roleId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60L * 60 * 24)))
                .signWith(key)
                .compact();
    }

    public CurrentUser parse(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        int roleId = claims.get("roleId", Integer.class);
        return new CurrentUser(claims.getSubject(), claims.get("username", String.class), roleId);
    }
}
