package ru.spb.tksoft.banking.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

/**
 * DB JPA config.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"ru.spb.tksoft.banking.entity"})
@EnableJpaRepositories(
                entityManagerFactoryRef = "bankingEntityManagerFactory",
                transactionManagerRef = "bankingTransactionManager",
                basePackages = {"ru.spb.tksoft.banking.repository"})
@RequiredArgsConstructor
public class BankDatabaseConfig {

        private final Environment environment;

        /**
         * Create fabric of entities and sessions for banking.
         * 
         * @param builder Instance of EntityManagerFactoryBuilder.
         * @param dataSource DataSource for banking.
         * @return Instance of entity manager factory.
         */
        @Bean(name = "bankingEntityManagerFactory")
        public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
                        EntityManagerFactoryBuilder builder,
                        @Qualifier("bankDataSource") DataSource dataSource) {

                HashMap<String, Object> properties = new HashMap<>();
                properties.put("hibernate.dialect",
                                environment.getProperty("spring.jpa.database-platform"));

                return builder.dataSource(dataSource)
                                .packages("ru.spb.tksoft.banking.entity")
                                .properties(properties).build();
        }

        /**
         * Create transaction manager for banking.
         * 
         * @param entityManagerFactory Fabric of entities and sessions for banking.
         * @return Instance of transaction manager for banking.
         */
        @Bean(name = "bankingTransactionManager")
        public PlatformTransactionManager postgresTransactionManager(
                        @Qualifier("bankingEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

                return new JpaTransactionManager(entityManagerFactory);
        }
}
