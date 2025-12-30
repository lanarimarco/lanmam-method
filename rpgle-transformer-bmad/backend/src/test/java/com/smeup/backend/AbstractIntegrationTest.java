package com.smeup.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base class for integration tests using Testcontainers with PostgreSQL.
 *
 * <p>Uses the Singleton Container Pattern to share a single PostgreSQL container
 * across all integration test classes. This avoids Spring context caching issues
 * where a cached context points to a dead container.
 *
 * <p>The container is started once in a static block and stays running for the
 * entire test suite. JVM shutdown hooks handle cleanup.
 *
 * <p>Configures Flyway to run migrations against the PostgreSQL container.
 * Flyway is auto-configured by Spring Boot via spring-boot-starter-flyway.
 *
 * @see <a href="https://maciejwalkowiak.com/blog/testcontainers-spring-boot-setup/">
 *     Testcontainers Spring Boot Setup</a>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  @ServiceConnection
  static PostgreSQLContainer<?> postgres;

  static {
    postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    postgres.start();
  }

}
