package com.esliceu.forum.interceptors;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.esliceu.forum.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {
        String string = req.getHeader("Autoritzation");
        System.out.println(string);
        String tok = tokenService.getTokenFromHeader(string);
        if (tok == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String email;
        try {
            email = tokenService.verifyAndGetUserFromToken(tok).getUserEmail();
        }catch (JWTDecodeException jwtDecodeException){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
