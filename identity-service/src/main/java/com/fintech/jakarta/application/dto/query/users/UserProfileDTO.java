package com.fintech.jakarta.application.dto.query.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String fullAddress;
    private String phoneNumber;
    private boolean active;

}
