package com.fintech.jakarta.infra.messaging;

import com.fintech.jakarta.application.dto.command.users.UserCreatedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserDeletedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserUpdatedEventDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

@ApplicationScoped
public class UserMessageDispatcher {

    @Inject
    private RabbitMQProducer producer;

    // O parâmetro 'during = TransactionPhase.AFTER_SUCCESS' é a chave aqui
    public void onUserCreated(@Observes(during = TransactionPhase.AFTER_SUCCESS) UserCreatedEventDTO event) {
        producer.publishUserCreated(event);
    }

    public void onUserDeleted(@Observes(during = TransactionPhase.AFTER_SUCCESS)UserDeletedEventDTO event){
        producer.publishUserDeleted(event);
    }

    public void onUserUpdated(@Observes(during = TransactionPhase.AFTER_SUCCESS) UserUpdatedEventDTO event) {
        producer.publishUserUpdate(event);
    }
}