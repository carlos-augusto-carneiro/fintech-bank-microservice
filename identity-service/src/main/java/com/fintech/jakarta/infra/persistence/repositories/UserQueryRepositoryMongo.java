package com.fintech.jakarta.infra.persistence.repositories;

import com.fintech.jakarta.application.dto.query.users.UserProfileDTO;
import com.fintech.jakarta.application.dto.query.users.UserSummaryDTO;
import com.fintech.jakarta.application.irepository.query.IUserQueryRepository;
import com.fintech.jakarta.infra.persistence.connection.MongoConnection;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

import com.mongodb.client.MongoCollection;


@ApplicationScoped
public class UserQueryRepositoryMongo implements IUserQueryRepository {

    @Inject
    private MongoConnection mongoConnection;

    private static final String COLLECTION_NAME = "users_view";

    @Override
    public Optional<UserProfileDTO> findByUserName(String userName) {
        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection(COLLECTION_NAME);

        Document doc = collection.find(Filters.eq("firstName", userName)).first();

        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(mapToDTO(doc));
    }

    @Override
    public Optional<UserProfileDTO> findByEmail(String email) {
        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection(COLLECTION_NAME);

        Document doc = collection.find(Filters.eq("email", email)).first();

        if (doc == null) {
            return Optional.empty();
        }

        return Optional.of(mapToDTO(doc));
    }

    @Override
    public Optional<UserProfileDTO> findById(UUID id) {
        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection(COLLECTION_NAME);

        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDTO(doc));
    }

    @Override
    public Optional<UserProfileDTO> findByCpf(String cpf) {
        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection(COLLECTION_NAME);

        Document doc = collection.find(Filters.eq("cpf", cpf)).first();
        if (doc == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDTO(doc));
    }

    @Override
    public List<UserSummaryDTO> findAllActive() {
        List<UserSummaryDTO> list = new ArrayList<>();

        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection(COLLECTION_NAME);
        for (Document doc : collection.find(Filters.eq("active", false))) {
            list.add(mapToSummaryDTO(doc));
        }

        return list;

    }

    @Override
    public List<UserSummaryDTO> findAllInactive() {
        List<UserSummaryDTO> result = new ArrayList<>();

        MongoCollection<Document> collection = mongoConnection.getDatabase()
                .getCollection("users_view");
        for (Document doc : collection.find(Filters.eq("active", false))) {
            result.add(mapToSummaryDTO(doc));
        }

        return result;
    }

    private com.fintech.jakarta.application.dto.query.users.UserProfileDTO mapToDTO(Document doc) {

        UUID id = UUID.fromString(doc.getString("_id"));
        String firstName = doc.getString("firstName");
        String lastName = doc.getString("lastName");
        String email = doc.getString("email");
        String cpf = doc.getString("cpf");

        String fullAddress = doc.getString("fullAddress");
        String phoneNumber = doc.getString("phoneNumber");
        boolean active = doc.getBoolean("active", true);

        return new com.fintech.jakarta.application.dto.query.users.UserProfileDTO(
                id, firstName, lastName, email, cpf, fullAddress, phoneNumber, active
        );
    }

    private UserSummaryDTO mapToSummaryDTO(Document doc) {

        UUID id = UUID.fromString(doc.getString("_id"));

        String firstName = doc.getString("firstName");
        String lastName = doc.getString("lastName");
        String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");

        String email = doc.getString("email");

        boolean active = doc.getBoolean("active", false);

        return new UserSummaryDTO(id, fullName.trim(), email, active);
    }
}
