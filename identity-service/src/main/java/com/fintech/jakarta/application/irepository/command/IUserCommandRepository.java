package com.fintech.jakarta.application.irepository.command;

import com.fintech.jakarta.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserCommandRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String username);
}
