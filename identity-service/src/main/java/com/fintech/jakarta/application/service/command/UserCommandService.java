package com.fintech.jakarta.application.service.command;

import com.fintech.jakarta.application.dto.command.users.*;
import com.fintech.jakarta.application.irepository.command.IUserCommandRepository;
import com.fintech.jakarta.domain.entities.User;
import com.fintech.jakarta.domain.enuns.UserType;
import com.fintech.jakarta.domain.valueobject.Address;
import com.fintech.jakarta.domain.valueobject.CPF;
import com.fintech.jakarta.domain.valueobject.Email;
import com.fintech.jakarta.domain.valueobject.PhoneNumber;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;

@Stateless
public class UserCommandService {

    @Inject
    private IUserCommandRepository repository;

    @Inject
    private Event<UserCreatedEventDTO> eventPublisher;

    @Inject
    private Event<UserUpdatedEventDTO> eventPublisherUpdate;

    @Inject
    private Event<UserDeletedEventDTO> eventPublisherDeleted;

    public void createUser(UserCreateDTO dto) {
        Email email = new Email(dto.getEmail());
        CPF cpf = new CPF(dto.getCpf());
        PhoneNumber phone = (dto.getPhoneNumber() != null) ? new PhoneNumber(dto.getPhoneNumber()) : null;

        Address address = new Address(
                dto.getStreet(), dto.getNeighborhood(), dto.getCity(),
                dto.getState(), dto.getZip(), dto.getCountry()
        );

        User user = new User(
                dto.getFirstName(),
                dto.getLastName(),
                email,
                dto.getPassword(),
                cpf,
                UserType.valueOf(dto.getUserType()),
                phone,
                address
        );

        user.changeAddress(address);

        repository.save(user);

        UserCreatedEventDTO eventDTO = new UserCreatedEventDTO(
                user.getId().toString(),
                user.getFirstName(),
                user.getEmail().getValue(),
                user.getAddress(),
                user.getCpf().getFormatted()
        );

        eventPublisher.fire(eventDTO);
    }

    public void updateUser(UserUpdateDTO dto) {
        User user = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (dto.getFirstName() != null || dto.getLastName() != null) {
            String newName = dto.getFirstName() != null ? dto.getFirstName() : user.getFirstName();
            String newLastName = dto.getLastName() != null ? dto.getLastName() : user.getLastName();
            user.updateProfile(newName, newLastName);
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.updateEmail(dto.getEmail());
        }

        if (dto.getStreet() != null) {
            Address newAddress = new Address(
                    dto.getStreet(),
                    dto.getNeighborhood(),
                    dto.getCity(),
                    dto.getState(),
                    dto.getZip(),
                    dto.getCountry()
            );
            user.changeAddress(newAddress);
        }

        if (dto.getPhoneNumber() != null) {
            user.updatePhoneNumber(dto.getPhoneNumber());
        }

        repository.save(user);

        UserUpdatedEventDTO event = new UserUpdatedEventDTO(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().getValue(),
                user.getAddress(),
                user.getPhoneNumber() != null ? user.getPhoneNumber().getFormatted() : null
        );

        eventPublisherUpdate.fire(event);
    }

    public void softDeleteUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.deactivate();
        repository.save(user);

        UserDeletedEventDTO event = new UserDeletedEventDTO(
                id,
                LocalDateTime.now()
        );

        eventPublisherDeleted.fire(event);
    }
}
