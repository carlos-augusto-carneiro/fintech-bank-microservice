package com.fintech.jakarta.application.irepository.query;

import com.fintech.jakarta.application.dto.query.users.UserProfileDTO;
import com.fintech.jakarta.application.dto.query.users.UserSummaryDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserQueryRepository {
    Optional<UserProfileDTO> findByUserName(String userName);
    Optional<UserProfileDTO> findByEmail(String email);
    Optional<UserProfileDTO> findById(UUID id);
    Optional<UserProfileDTO> findByCpf(String cpf);

    List<UserSummaryDTO> findAllActive();
    List<UserSummaryDTO> findAllInactive();
}
