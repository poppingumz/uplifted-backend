package fontys.s3.uplifted.config.security;

import fontys.s3.uplifted.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SecretKey = "VerySecretKeyThatShouldBeLongEnough123!";
    private static final long ExpirationTime = 1000L * 60 * 60 * 24; // 24 hours
    private static final Key key = Keys.hmacShaKeyFor(SecretKey.getBytes());

    public String generateToken(User user) {
        if (user.getRole() == null) {
            throw new IllegalStateException("User role is null for user: " + user.getEmail());
        }

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
