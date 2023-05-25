package com.specure.repository;

import com.specure.SahApplication;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SahApplication.class, TenantRepositoryTestBase.TenantDatabaseConfig.class})
@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
public class TenantRepositoryTestBase {

    static final MySQLContainer DATABASE = new MySQLContainer(DockerImageName.parse("biarms/mysql:5.7.33-linux-arm64v8-beta-travis").asCompatibleSubstituteFor("mysql"));

    static {
        DATABASE.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username=", DATABASE::getUsername);
        registry.add("spring.datasource.password=", DATABASE::getPassword);
    }

    @TestConfiguration
    public static class TenantDatabaseConfig {

        @Bean(initMethod = "migrate")
        public Flyway flyway(DataSource dataSource) {
            return Flyway.configure()
                    .locations("classpath:db/migration/client")
                    .dataSource(dataSource)
                    .load();
        }
    }
}
