package com.vn.backend.configs.jwt;


import com.vn.backend.services.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

//sinh ra jwt
@Slf4j
@Component
public class JwtTokenPovider {
    @Value("${spring.plugin.springsecurity.rest.token.storage.jwt.secret}")
    private String JWT_SECRET;
    @Value("${spring.plugin.springsecurity.rest.token.storage.jwt.expiration}")
    private int JWT_EXPIRATION;
    public String generateToken(CustomUserDetails CustomUserDetails){
        Date now =new Date();
        Date dateExpiration =new Date(now.getTime()+JWT_EXPIRATION * 1000L);
        return Jwts.builder()
                    .setSubject(CustomUserDetails.getUsername())
                    .setIssuedAt(now)
                    .setExpiration(dateExpiration)
                    .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                    .compact();
    }
    public String generateToken(Authentication authentication){
        Date now =new Date();
        Date dateExpiration =new Date(now.getTime()+JWT_EXPIRATION);
        return Jwts.builder()
                    .setSubject(authentication.getName())
                    .setIssuedAt(now)
                    .setExpiration(dateExpiration)
                    .signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                    .compact();
    }
   
    public String getEmailFromJwt(String token){
        Claims claims= Jwts.parser().setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token).getBody();
     
        return claims.getSubject();
    }
    
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            log.error("Invalid JWT Token");
        }catch(ExpiredJwtException e){
            log.error("Expired JWT Token");
        }catch(UnsupportedJwtException e){
            log.error("UnsupportedJwt");
        }catch (IllegalArgumentException e){
            log.error("JWT Claims String is empty");
        }
        return false;
    }
}
