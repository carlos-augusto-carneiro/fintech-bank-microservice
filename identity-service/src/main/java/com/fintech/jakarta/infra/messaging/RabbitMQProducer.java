package com.fintech.jakarta.infra.messaging;

import com.fintech.jakarta.application.dto.command.users.UserCreatedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserDeletedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserUpdatedEventDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class RabbitMQProducer {

    @Inject
    private ConnectionFactory connectionFactory;


    public void publishUserCreated(UserCreatedEventDTO event){
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            String json = JsonbBuilder.create().toJson(event);
            channel.basicPublish("", "user.created", null, json.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Falha crítica ao publicar no RabbitMQ", e);
        }
    }

    public void publishUserDeleted(UserDeletedEventDTO event) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            String json = JsonbBuilder.create().toJson(event);

            channel.basicPublish("", "user.deleted", null, json.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Erro crítico ao publicar evento de Delete no RabbitMQ", e);
        }
    }
    public void publishUserUpdate(UserUpdatedEventDTO event) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            String json = JsonbBuilder.create().toJson(event);

            channel.basicPublish("", "user.updated", null, json.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Erro crítico ao publicar atualização no RabbitMQ", e);
        }
    }
}
