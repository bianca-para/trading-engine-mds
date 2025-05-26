package org.dev.server.integrationTests.asset.service;

import net.datafaker.Faker;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.exception.AssetAlreadyExistsException;
import org.dev.server.exception.AssetNotFoundException;
import org.dev.server.repository.AssetRepository;
import org.dev.server.service.AssetService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class AssetServiceIntTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerTestDbProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private Flyway flyway;

    private AssetRequestDto request;

    @BeforeEach
    void setUp() {
        // Reset DB
        flyway.clean();
        flyway.migrate();

        // Prepare test data
        Faker faker = new Faker();
        String rawSymbol = faker.cryptoCoin().coin();
        String symbol = rawSymbol.substring(0, Math.min(10, rawSymbol.length()));
        String rawName = faker.company().name();
        String name = rawName.substring(0, Math.min(50, rawName.length()));
        Double price = faker.number().randomDouble(2, 0, 10000);

        request = new AssetRequestDto(symbol, name, price);
    }

    @Test
    void fullCrud_shouldSucceed() {
        AssetResponseDto created = assetService.createAsset(request);
        Long id = created.assetId();

        AssetResponseDto found = assetService.getAsset(id);
        assertThat(found.name()).isEqualTo(request.name());

        assertThat(assetService.getAllAssets().size()).isEqualTo(1);

        AssetRequestDto updatedRequest = new AssetRequestDto("UPDT", "Updated Name", 123.45);
        AssetResponseDto updated = assetService.updateAsset(id, updatedRequest);
        assertThat(updated.symbol()).isEqualTo("UPDT");

        assetService.deleteAsset(id);
        assertThatThrownBy(() -> assetService.getAsset(id))
                .isInstanceOf(AssetNotFoundException.class);
    }

    @Test
    void exceptionPaths_shouldThrowCorrectly() {
        AssetResponseDto saved = assetService.createAsset(request);

        assertThatThrownBy(() -> assetService.createAsset(request))
                .isInstanceOf(AssetAlreadyExistsException.class);

        assertThatThrownBy(() -> assetService.getAsset(9999L))
                .isInstanceOf(AssetNotFoundException.class);

        assertThatThrownBy(() -> assetService.updateAsset(9999L, request))
                .isInstanceOf(AssetNotFoundException.class);

        AssetRequestDto second = new AssetRequestDto("DOGE", "Dogecoin", 0.5);
        AssetResponseDto secondSaved = assetService.createAsset(second);

        assertThatThrownBy(() -> assetService.updateAsset(secondSaved.assetId(), request))
                .isInstanceOf(AssetAlreadyExistsException.class);

        assertThatThrownBy(() -> assetService.deleteAsset(9999L))
                .isInstanceOf(AssetNotFoundException.class);
    }
}
