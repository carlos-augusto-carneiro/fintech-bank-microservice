package com.fintech.jakarta.infra.persistence.connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoConnection implements DatabaseConnection {

    private MongoClient mongoClient;
    private MongoDatabase database;

    private final String connectionString = "mongodb://localhost:27017";
    private final String dbName = "fintech_query_db";

    @Override
    @PostConstruct
    public void connect() {
        try{
            MongoClientURI uri = new MongoClientURI(connectionString);
            this.mongoClient = new MongoClient(uri);
            this.database = mongoClient.getDatabase(dbName);
            System.out.println("MongoDB conectado!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar no Mongo", e);
        }
    }

    @Override
    public void disconnect() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }

    @Override
    public boolean isConnected() {
        return this.mongoClient != null;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }
}
