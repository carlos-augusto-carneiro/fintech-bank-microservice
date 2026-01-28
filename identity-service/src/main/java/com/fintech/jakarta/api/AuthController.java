package com.fintech.jakarta.api;

import com.fintech.jakarta.application.dto.auth.LoginRequestDTO;
import com.fintech.jakarta.application.service.auth.AuthService;
import com.fintech.jakarta.domain.entities.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(email);
        loginRequestDTO.setPassword(password);
        authService.login(loginRequestDTO);

        return Response.ok().build();
    }
}
