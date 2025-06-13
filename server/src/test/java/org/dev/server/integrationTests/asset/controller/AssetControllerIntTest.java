package org.dev.server.integrationTests.asset.controller;

import net.datafaker.Faker;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

//Teoretic, in mod normal se folosesc baze de date in memory pentru efectuarea testelor de integrare
//In java, putem folosy H2 memory database, doar ca:
//Motivul pentru care folosim TestContainers este pentru ca definirea DDL-urilor este facuta folosind
//librarii specifice postgreSQL precum generarea de UUID's, ce nu pot fi importate in alte baze de date
//Solutie: folosim test containers care folosesc postgresql pentru rularea acestor teste
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//selectam profilul pentru spring, in cazul asta application-test
@ActiveProfiles("test")
public class AssetControllerIntTest {

    //Definirea containerului, cu datele de logare pentru baza de date, incluzand versiunea imaginii
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    //inregistrare dinamica pentru obtinerea conexiunii la baza de date temporara containerizata
    @DynamicPropertySource
    static void registerTestDb(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    //port local
    @LocalServerPort
    private int port;

    //field injection pentru migrarile catre baza de date
    @Autowired
    private Flyway flyway;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Faker faker = new Faker();
    private String baseUrl;

    //pentru modularitate, este efectuata inaintea fiecarui test de integrare
    //curatam baza de date si reconsturim schema
    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
        baseUrl = "http://localhost:" + port + "/asset";
    }
    //generare aleatoare pentru DTO, pentru o generalizare mai buna
    private AssetRequestDto randomAssetDto() {
        String name = faker.company().name();
        return new AssetRequestDto(
                "SYM" + faker.number().randomDigit(),
                name.substring(0, Math.min(50, name.length())),
                faker.number().randomDouble(2, 10, 10000)
        );
    }

    //se testeaza intreg fluxul CRUD

    @Test
    void fullCrudControllerIntegrationTest() {
        //create
        AssetRequestDto request = randomAssetDto();
        ResponseEntity<AssetResponseDto> createResp = restTemplate.postForEntity(baseUrl, request, AssetResponseDto.class);
        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AssetResponseDto created = createResp.getBody();
        assertThat(created).isNotNull();
        Long id = created.assetId();

        //read
        ResponseEntity<AssetResponseDto> getResp = restTemplate.getForEntity(baseUrl + "/" + id, AssetResponseDto.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody().symbol()).isEqualTo(request.symbol());

        //update
        AssetRequestDto updated = new AssetRequestDto("UPD", "Updated Asset", 9999.99);
        HttpEntity<AssetRequestDto> updateEntity = new HttpEntity<>(updated);
        ResponseEntity<AssetResponseDto> updateResp = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, updateEntity, AssetResponseDto.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody().name()).isEqualTo("Updated Asset");

        //read all
        ResponseEntity<AssetResponseDto[]> all = restTemplate.getForEntity(baseUrl, AssetResponseDto[].class);
        assertThat(all.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(all.getBody()).isNotEmpty();

        //delete
        restTemplate.delete(baseUrl + "/" + id);

        //verificam ca inregistrarea nu mai exista
        try {
            restTemplate.getForEntity(baseUrl + "/" + id, String.class);
            assertThat(true).as("Expected 404 but found the resource").isFalse();
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

}
