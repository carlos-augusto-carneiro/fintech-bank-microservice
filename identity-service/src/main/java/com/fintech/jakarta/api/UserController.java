package com.fintech.jakarta.api;

import com.fintech.jakarta.application.dto.command.users.UserCreateDTO;
import com.fintech.jakarta.application.dto.command.users.UserUpdateDTO;
import com.fintech.jakarta.application.dto.query.users.UserProfileDTO;
import com.fintech.jakarta.application.dto.query.users.UserSummaryDTO;
import com.fintech.jakarta.application.service.command.UserCommandService;
import com.fintech.jakarta.application.service.query.UserQueryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserCommandService userCommandService;

    @Inject
    private UserQueryService  userQueryService;

    @POST
    public Response create(UserCreateDTO dto) {
        userCommandService.createUser(dto);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, UserUpdateDTO dto) {
        dto.setId(id);
        userCommandService.updateUser(dto);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        userCommandService.softDeleteUser(id);
        return Response.status(Response.Status.OK).build();

    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        UserProfileDTO user = userQueryService.findById(id);

        return Response.ok(user).build();
    }

    @GET
    @Path("/search")
    public Response search(
            @QueryParam("cpf") String cpf,
            @QueryParam("email") String email,
            @QueryParam("username") String username) {

        UserProfileDTO user = null;

        if (cpf != null && !cpf.isEmpty()) {
            user = userQueryService.findByCpf(cpf);
        } else if (email != null && !email.isEmpty()) {
            user = userQueryService.findByEmail(email);
        } else if (username != null && !username.isEmpty()) {
            user = userQueryService.findByUserName(username);
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Informe CPF, Email ou Username para busca.").build();
        }

        return Response.ok(user).build();
    }

    @GET
    @Path("/active")
    public Response findAllActive() {
        List<UserSummaryDTO> users = userQueryService.findAllActive();

        return Response.ok(users).build();

    }

    @GET
    @Path("/inactive")
    public Response findAllDisabled() {
        List<UserSummaryDTO> users = userQueryService.findAllInactive();

        return Response.ok(users).build();

    }
}
