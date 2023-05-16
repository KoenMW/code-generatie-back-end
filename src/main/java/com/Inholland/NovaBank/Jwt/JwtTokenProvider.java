package com.Inholland.NovaBank.Jwt;

import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Autowired
    JwtKeyProvider keyProvider;
    @Autowired
    UserDetailService userDetailService;

    public String createToken(String username, Role role) throws JwtException {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", role.name());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(keyProvider.getPrivateKey()) // <- this is important, we need a key to sign the jwt
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(keyProvider.getPrivateKey()).build().parseClaimsJws(token);
            String username = claims.getBody().getSubject();
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails,
                    ""
                    , userDetails.getAuthorities());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Bearer token not valid");
        }
    }
}
