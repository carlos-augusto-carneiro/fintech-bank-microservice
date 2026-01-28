package com.fintech.jakarta.infra.config;

import org.flywaydb.core.Flyway;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

@Singleton
@Startup
public class DatabaseMigration {

    @Resource(lookup = "jdbc/IdentityDS")
    private DataSource dataSource;

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void migrate() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();
            System.out.println("Flyway: Migração da base de dados concluída com sucesso!");
        } catch (Exception e) {
            System.err.println("Flyway: Erro ao migrar base de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}