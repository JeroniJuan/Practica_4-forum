package com.esliceu.forum.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.esliceu.forum.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TokenService {
    @Value("${token.secret}")
    String secret;

    @Value("${token.expiration.time}")
    long tokenExpirationTime;

    public String buildToken(User user) {
        return JWT.create()
                .withClaim("email", user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("_id", user.getId())
                .withClaim("__v", 0)
                .withClaim("name", user.getName())
                .withClaim("role", user.getUserRole())
                .withClaim("avatarUrl", user.getAvatarUrl())
                .withClaim("moderateCategory", user.getModerateCategory())
                .withClaim("permissions", user.getPermissions())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public User verifyAndGetUserFromToken(String token) {
        DecodedJWT decoded = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);

        User user = new User();
        user.setId(decoded.getClaim("id").asInt());
        user.setName(decoded.getClaim("name").asString());
        user.setEmail(decoded.getClaim("email").asString());
        user.setUserRole(decoded.getClaim("role").asString());
        user.setAvatarUrl(decoded.getClaim("avatarUrl").asString());
        user.setModerateCategory(decoded.getClaim("moderateCategory").asString());

        Map<String, Object> permissions = decoded.getClaim("permissions").asMap();
        user.setPermissions(permissions);

        return user;
    }

    public String getTokenFromHeader(String string) {
        if (string == null) return null;
        if (!string.startsWith("Bearer ")) return null;
        return string.substring(7);
    }
}