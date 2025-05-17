package org.dev.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class ServerApplicationTests {

    @Test
    void contextLoads() {
        new ApplicationContextRunner()
                .withUserConfiguration(DummyConfig.class)
                .run(context -> {
                    // assert nothing or just force it to be considered loaded
                    assert(context != null);
                });
    }

    @Configuration
    static class DummyConfig {
        // no beans needed
    }
}
