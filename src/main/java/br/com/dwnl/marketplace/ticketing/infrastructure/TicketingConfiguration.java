package br.com.dwnl.marketplace.ticketing.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Qualifier("ticketing")
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
        basePackages = "br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository",
        entityManagerFactoryRef = "ticketingEntityManagerFactory",
        transactionManagerRef = "ticketingTransactionManager"
)
@EnableRedisRepositories(basePackages = "dwnl.marketplace.ticketing", redisTemplateRef = "ticketingRedisTemplate")
public class TicketingConfiguration {

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.datasource")
    public DataSourceProperties ticketingDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.datasource.configuration")
    public HikariDataSource ticketingDataSource(@Qualifier("ticketing") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("ticketing.jpa")
    public JpaProperties ticketingJpaProperties() {
        return new JpaProperties();
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public LocalContainerEntityManagerFactoryBean ticketingEntityManagerFactory(
            @Qualifier("ticketing") HikariDataSource ticketingDataSource,
            @Qualifier("ticketing") JpaProperties ticketingJpaProperties) {

        var builder = new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(),
                x -> ticketingJpaProperties.getProperties(),
                null
        );

        return builder
                .dataSource(ticketingDataSource)
                .packages("br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity")
                .persistenceUnit("ticketing")
                .build();
    }

    @Qualifier("ticketing")
    @Bean
    public PlatformTransactionManager ticketingTransactionManager(
            @Qualifier("ticketing") LocalContainerEntityManagerFactoryBean ticketingEntityManagerFactory) {
        assert ticketingEntityManagerFactory.getObject() != null;
        return new JpaTransactionManager(ticketingEntityManagerFactory.getObject());
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public RedisConnectionFactory ticketingRedisConnectionFactory(@Value("${ticketing.redis.host}") String hostName,
                                                                  @Value("${ticketing.redis.port}") int port){
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(hostName, port));
    }

    @Qualifier("ticketing")
    @Bean(defaultCandidate = false)
    public RedisTemplate<?, ?> ticketingRedisTemplate(@Qualifier("ticketing") RedisConnectionFactory connectionFactory){
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}