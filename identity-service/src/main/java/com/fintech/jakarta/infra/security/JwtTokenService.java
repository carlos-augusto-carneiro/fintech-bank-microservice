package com.fintech.jakarta.infra.security;

import com.fintech.jakarta.application.irepository.auth.ITokenService;
import com.fintech.jakarta.domain.entities.User;
import io.jsonwebtoken.Jwts;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

@ApplicationScoped
public class JwtTokenService implements ITokenService {

    @Inject
    @ConfigProperty(name = "jwt.secret", defaultValue = "UmaSenhaMuitoSecretaEMuitoLongaParaSerSegura123")
    private String secretKey;

    @Inject
    @ConfigProperty(name = "jwt.expiration.access", defaultValue = "900000")
    private long accessExpiration;

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail().getValue())
                .claim("id", user.getId().toString())
                .claim("type", user.getUserType().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        long refreshExpiration = 900000L;

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
