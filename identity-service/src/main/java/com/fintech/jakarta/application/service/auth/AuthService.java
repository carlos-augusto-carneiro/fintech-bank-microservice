package com.fintech.jakarta.application.service.auth;

import com.fintech.jakarta.application.dto.auth.LoginRequestDTO;
import com.fintech.jakarta.application.dto.auth.TokenResponseDTO;
import com.fintech.jakarta.application.irepository.auth.ITokenService;
import com.fintech.jakarta.application.irepository.command.IUserCommandRepository;
import com.fintech.jakarta.domain.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

@ApplicationScoped
public class AuthService {
    @Inject
    private IUserCommandRepository userRepository;

    @Inject
    private ITokenService tokenService;

    public TokenResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotAuthorizedException("Credenciais inválidas"));

        if (!user.login(request.getPassword())) {
            throw new NotAuthorizedException("Credenciais inválidas");
        }
        if (!user.isActive()) {
            throw new NotAuthorizedException("Utilizador inativo");
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new TokenResponseDTO(accessToken, refreshToken, "Bearer", 9000L);
    }
}
