package org.dev.server.integrationTests.asset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AssetControllerIntTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void registerTestDb(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private Flyway flyway;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Faker faker = new Faker();
    private String baseUrl;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
        baseUrl = "http://localhost:" + port + "/asset";
    }

    private AssetRequestDto randomAssetDto() {
        String name = faker.company().name();
        return new AssetRequestDto(
                "SYM" + faker.number().randomDigit(),
                name.substring(0, Math.min(50, name.length())),
                faker.number().randomDouble(2, 10, 10000)
        );
    }


    @Test
    void fullCrudControllerIntegrationTest() {

        AssetRequestDto request = randomAssetDto();
        ResponseEntity<AssetResponseDto> createResp = restTemplate.postForEntity(baseUrl, request, AssetResponseDto.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AssetResponseDto created = createResp.getBody();
        assertThat(created).isNotNull();
        Long id = created.assetId();


        ResponseEntity<AssetResponseDto> getResp = restTemplate.getForEntity(baseUrl + "/" + id, AssetResponseDto.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody().symbol()).isEqualTo(request.symbol());


        AssetRequestDto updated = new AssetRequestDto("UPD", "Updated Asset", 9999.99);
        HttpEntity<AssetRequestDto> updateEntity = new HttpEntity<>(updated);
        ResponseEntity<AssetResponseDto> updateResp = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, updateEntity, AssetResponseDto.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody().name()).isEqualTo("Updated Asset");


        ResponseEntity<AssetResponseDto[]> all = restTemplate.getForEntity(baseUrl, AssetResponseDto[].class);
        assertThat(all.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all.getBody()).isNotEmpty();


        restTemplate.delete(baseUrl + "/" + id);


        try {
            restTemplate.getForEntity(baseUrl + "/" + id, String.class);
            assertThat(true).as("Expected 404 but found the resource").isFalse();
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

}
