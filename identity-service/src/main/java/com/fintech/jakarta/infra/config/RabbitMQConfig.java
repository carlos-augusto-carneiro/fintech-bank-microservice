package com.fintech.jakarta.infra.config;

import com.rabbitmq.client.ConnectionFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RabbitMQConfig {

    @Produces
    @ApplicationScoped
    public ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        return factory;
    }
}