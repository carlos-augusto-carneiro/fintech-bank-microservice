package com.fintech.jakarta.application.dto.command.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEventDTO {
    private UUID id;
    private LocalDateTime deletedAt;
}
