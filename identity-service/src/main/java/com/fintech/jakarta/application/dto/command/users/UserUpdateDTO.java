package com.fintech.jakarta.application.dto.command.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @NotNull(message = "O ID do usuário é obrigatório para atualização")
    private UUID id;

    private String firstName;
    private String lastName;

    private String email;

    private String phoneNumber;

    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String zip;
    private String country;
}