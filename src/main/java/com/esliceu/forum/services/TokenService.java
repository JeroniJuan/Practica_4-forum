package com.esliceu.forum.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    @Value("${token.secret}")
    String secret;

    @Value("${token.expiration.time}")
    long tokenExpirationTime;

    public String buildToken(String s){
        return JWT.create()
                .withSubject(s)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String verifyAndGetEmailFromToken(String token) {
        var decoded = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
        String s = decoded.getClaim("permisos").asString();
        return decoded.getSubject();
    }
    public String getTokenFromHeader(String string) {
        if (string == null) return null;
        if (!string.startsWith("Bearer ")) return null;
        return string.substring(7);
    }
}