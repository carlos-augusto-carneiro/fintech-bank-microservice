package com.fintech.jakarta.infra.persistence.repositories;

import com.fintech.jakarta.application.irepository.command.IUserCommandRepository;
import com.fintech.jakarta.domain.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserCommandRepositoryPostgres implements IUserCommandRepository {

    @PersistenceContext(unitName = "IdentityPU")
    private EntityManager em;


    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String username){
        return Optional.ofNullable(em.find(User.class, username));
    }

}
