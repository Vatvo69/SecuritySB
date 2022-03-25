package com.example.demo.security.jwt;

import com.example.demo.security.userprincipal.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final Logger logger= LoggerFactory.getLogger(JwtProvider.class);
    private String JWT_KEY="secret";
    private int JWT_EXPIRATION=86400;

    public String createJwt(Authentication authentication){
        UserPrincipal userPrincipal= (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+JWT_EXPIRATION*1000))
                .signWith(SignatureAlgorithm.HS512,JWT_KEY)
                .compact();
    }
    public String getUsernameFromJwt(String token){
        String username=Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).getBody().getSubject();
        return username;
    }
    public Boolean validateJwt(String token){
        try{
            Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT Token");
        }catch (ExpiredJwtException e){
            logger.error("Expired JWT Token");
        }catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token");
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
