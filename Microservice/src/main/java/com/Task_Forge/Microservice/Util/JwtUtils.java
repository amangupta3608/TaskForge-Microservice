package com.Task_Forge.Microservice.Util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.String;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
   private  String secretKey;

    public String getSecretKey(){
        return secretKey;
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (JwtException ex){
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
