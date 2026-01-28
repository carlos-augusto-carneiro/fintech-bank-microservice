package com.fintech.jakarta.application.irepository.auth;

import com.fintech.jakarta.domain.entities.User;

public interface ITokenService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);

    String validateToken(String token);
}