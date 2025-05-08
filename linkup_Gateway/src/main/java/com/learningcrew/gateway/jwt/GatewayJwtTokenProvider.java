package com.learningcrew.gateway.jwt;

import com.learningcrew.exception.CustomJwtException;
import com.learningcrew.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class GatewayJwtTokenProvider {
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            throw new CustomJwtException(ErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException(ErrorCode.EMPTY_JWT);
        }
    }

    /* Payload에서 이메일 추출 */
    public String getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

}
