package com.fintech.jakarta.application.service.query;

import com.fintech.jakarta.application.dto.query.users.UserProfileDTO;
import com.fintech.jakarta.application.dto.query.users.UserSummaryDTO;
import com.fintech.jakarta.application.irepository.query.IUserQueryRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserQueryService {

    @Inject
    private IUserQueryRepository repository;

    public UserProfileDTO findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com email: " + email));
    }

    public UserProfileDTO findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com CPF: " + cpf));
    }

    public  UserProfileDTO findByUserName(String userName) {
        return repository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com username:" + userName));
    }

    public  UserProfileDTO findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public List<UserSummaryDTO> findAllActive() {
        return repository.findAllActive();
    }

    public List<UserSummaryDTO> findAllInactive() {
        return repository.findAllInactive();
    }

}
