package jocture.todo.web.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jocture.todo.data.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {
    private static final String SECRET_KEY = "TN27SU13BIN8205";

    //JWT 토큰 생성
    public String create(User user) {

        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                // Header, Signature
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // Payload
                .setSubject(user.getId()) //sub
                .setIssuer("Todo App") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    // JWT 토큰에서 회원 ID 반황
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)// 주의 : vs. parseClaimsJw't'() 가 아님
                .getBody();
        return claims.getSubject();

    }
}
