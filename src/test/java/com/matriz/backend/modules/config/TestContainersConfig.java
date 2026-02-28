package com.matriz.backend.modules.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
                .withDatabaseName("matriz_test")
                .withUsername("test")
                .withPassword("test");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStack() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0"))
                .withServices("s3");
    }

    @Bean
    public DynamicPropertyRegistrar s3Properties(LocalStackContainer localStack) {
        return registry -> {
            registry.add("cloud.r2.endpoint", () -> localStack.getEndpoint().toString());
            registry.add("cloud.r2.access-key", localStack::getAccessKey);
            registry.add("cloud.r2.secret-key", localStack::getSecretKey);
            registry.add("cloud.r2.region", localStack::getRegion);
            registry.add("cloud.r2.bucket-name", () -> "matriz-test-bucket");
            registry.add("cloud.r2.public-url", () -> "http://test-url.com");
        };
    }
}
