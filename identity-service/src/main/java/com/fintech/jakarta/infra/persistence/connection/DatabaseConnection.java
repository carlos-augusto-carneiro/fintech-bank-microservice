package com.fintech.jakarta.infra.persistence.connection;

public interface DatabaseConnection {
    void connect();
    void disconnect();
    boolean isConnected();
}