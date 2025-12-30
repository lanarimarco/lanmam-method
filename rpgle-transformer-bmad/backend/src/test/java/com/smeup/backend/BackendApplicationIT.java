package com.smeup.backend;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BackendApplicationIT extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
        assertThat(postgres.isRunning()).isTrue();
    }
}
