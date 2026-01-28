package com.fintech.jakarta.infra.messaging;

import com.fintech.jakarta.application.dto.command.users.UserCreatedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserDeletedEventDTO;
import com.fintech.jakarta.application.dto.command.users.UserUpdatedEventDTO;
import com.fintech.jakarta.infra.persistence.connection.MongoConnection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.rabbitmq.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class UserProjector {

    private final String QUEUE_CREATED = "user.created";
    private final String QUEUE_DELETED = "user.deleted";
    private final String QUEUE_UPDATED = "user.updated";
    private final String COLLECTION_NAME = "users_view";

    @Inject
    private MongoConnection mongoConnection;

    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_CREATED, true, false, false, null);
            channel.queueDeclare(QUEUE_DELETED, true, false, false, null);
            channel.queueDeclare(QUEUE_UPDATED, true, false, false, null);

            DeliverCallback createCallback = (consumerTag, delivery) -> {
                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Evento Create recebido: " + json);
                synchronizeCreation(json);
            };

            DeliverCallback deleteCallback = (consumerTag, delivery) -> {
                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Evento Delete recebido: " + json);
                synchronizeDeletion(json);
            };
            channel.basicConsume(QUEUE_UPDATED, true, (tag, delivery) -> {
                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Evento Update recebido: " + json);
                synchronizeUpdate(json);
            }, tag -> {});

            channel.basicConsume(QUEUE_CREATED, true, createCallback, tag -> { });
            channel.basicConsume(QUEUE_DELETED, true, deleteCallback, tag -> { });
            channel.basicConsume(QUEUE_UPDATED, true, deleteCallback, tag -> { });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void synchronizeCreation(String jsonEvent) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            UserCreatedEventDTO event = jsonb.fromJson(jsonEvent, UserCreatedEventDTO.class);

            Document doc = new Document("_id", event.getId())
                    .append("firstName", event.getFirstName())
                    .append("email", event.getEmail())
                    .append("cpf", event.getCpf())
                    .append("active", true)

                    .append("fullAddress", event.getAddress() != null ? event.getAddress().toString() : "Endereço Pendente")

                    .append("lastUpdated", java.time.Instant.now().toString());

            mongoConnection.getDatabase()
                    .getCollection(COLLECTION_NAME)
                    .insertOne(doc);

            System.out.println("Usuário criado no Mongo: " + event.getId());

        } catch (Exception e) {
            System.err.println("Erro ao projetar criação no Mongo: " + e.getMessage());
        }
    }

    private void synchronizeDeletion(String jsonEvent) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            UserDeletedEventDTO event = jsonb.fromJson(jsonEvent, UserDeletedEventDTO.class);

            mongoConnection.getDatabase()
                    .getCollection(COLLECTION_NAME)
                    .updateOne(
                            Filters.eq("_id", event.getId()),
                            Updates.combine(
                                    Updates.set("active", false),
                                    Updates.set("lastUpdated", event.getDeletedAt())
                            )
                    );

            System.out.println("Usuário inativado no Mongo: " + event.getId());

        } catch (Exception e) {
            System.err.println("Erro ao projetar delete no Mongo: " + e.getMessage());
        }
    }

    private void synchronizeUpdate(String jsonEvent) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            UserUpdatedEventDTO event = jsonb.fromJson(jsonEvent, UserUpdatedEventDTO.class);

            List<Bson> updates = new ArrayList<>();

            updates.add(Updates.set("lastUpdated", java.time.Instant.now().toString()));

            if (event.getFirstName() != null) updates.add(Updates.set("firstName", event.getFirstName()));
            if (event.getLastName() != null) updates.add(Updates.set("lastName", event.getLastName()));
            if (event.getEmail() != null) updates.add(Updates.set("email", event.getEmail()));
            if (event.getPhoneNumber() != null) updates.add(Updates.set("phoneNumber", event.getPhoneNumber()));

            if (event.getAddress() != null) {
                updates.add(Updates.set("fullAddress", event.getAddress().getFullAddress()));

                Document addressDoc = new Document("street", event.getAddress().getStreet())
                        .append("city", event.getAddress().getCity())
                        .append("state", event.getAddress().getState())
                        .append("zip", event.getAddress().getZip());
                updates.add(Updates.set("address", addressDoc));
            }

            mongoConnection.getDatabase()
                    .getCollection(COLLECTION_NAME)
                    .updateOne(
                            Filters.eq("_id", event.getId()),
                            Updates.combine(updates)
                    );

            System.out.println("Usuário atualizado no Mongo: " + event.getId());

        } catch (Exception e) {
            System.err.println("Erro ao projetar update no Mongo: " + e.getMessage());
        }
    }
}