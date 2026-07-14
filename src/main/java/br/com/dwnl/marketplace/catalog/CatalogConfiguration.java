package br.com.dwnl.marketplace.catalog;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
        basePackages = "br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository",
        entityManagerFactoryRef = "catalogEntityManagerFactory",
        transactionManagerRef = "catalogTransactionManager"
)
@EnableMongoRepositories
@EnableMongoAuditing
public class CatalogConfiguration {

    @Bean
    @ConfigurationProperties("catalog.datasource")
    public DataSourceProperties catalogDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("catalog.datasource.configuration")
    public HikariDataSource catalogDataSource(DataSourceProperties catalogDataSourceProperties) {
        return catalogDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties("catalog.jpa")
    public JpaProperties catalogJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean catalogEntityManagerFactory(
            @Qualifier("catalogDataSource") HikariDataSource catalogDataSource,
            JpaProperties catalogJpaProperties) {

        var builder = new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                x -> catalogJpaProperties.getProperties(),
                null
        );

        return builder
                .dataSource(catalogDataSource)
                .packages("br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity")
                .persistenceUnit("catalog")
                .build();
    }

    @Bean
    public PlatformTransactionManager catalogTransactionManager(
            LocalContainerEntityManagerFactoryBean catalogEntityManagerFactory) {
        return new JpaTransactionManager(catalogEntityManagerFactory.getObject());
    }
}