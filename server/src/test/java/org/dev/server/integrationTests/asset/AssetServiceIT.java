//package org.dev.server.integrationTests.asset;
//
//import net.datafaker.Faker;
//import org.dev.server.dto.asset.AssetRequestDto;
//import org.dev.server.dto.asset.AssetResponseDto;
//import org.dev.server.model.Asset;
//import org.dev.server.repository.AssetRepository;
//import org.dev.server.service.AssetService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//public class AssetServiceIT {
//
//    @Autowired
//    private AssetService assetService;
//
//    @Autowired
//    private AssetRepository assetRepository;
//
//    private AssetRequestDto request;
//    private AssetResponseDto response;
//    private Asset asset;
//
//    @BeforeEach
//    public void initTestData() {
//        Faker faker = new Faker();
//
//        String rawSymbol =  faker.cryptoCoin().coin(); // ETH, BTC
//        String symbol = rawSymbol.substring(0, Math.min(10, rawSymbol.length()));
//
//        String rawName = faker.company().name();
//        String name = rawName.substring(0, Math.min(50, rawName.length()));
//
//        Double price = faker.number().randomDouble(2, 0, 10000);
//
//        request = new AssetRequestDto(symbol, name, price);
//        asset = new Asset(1L, request.symbol(), request.name(), request.price());
//        response = new AssetResponseDto(1L, request.symbol(), request.name(), request.price());
//    }
//    @Test
//    void fullCrud_shouldSucceed() {
//        // Create asset
//        AssetResponseDto created = assetService.createAsset(request);
//        Long id = created.assetId();
//
//        AssetResponseDto found = assetService.getAsset(id);
//        assertThat(found.name()).isEqualTo(request.name());
//
//        assertThat(assetService.getAllAssets().size()).isEqualTo(1);
//
//        AssetRequestDto updatedRequest = new AssetRequestDto("UPDT", "Updated Name", 123.45);
//        AssetResponseDto updated = assetService.updateAsset(id, updatedRequest);
//        assertThat(updated.symbol()).isEqualTo("UPDT");
//
//        assetService.deleteAsset(id);
//        assertThatThrownBy(() -> assetService.getAsset(id))
//                .isInstanceOf(org.dev.server.exception.AssetNotFoundException.class);
//    }
//
//    @Test
//    void exceptionPaths_shouldThrowCorrectly() {
//        AssetResponseDto saved = assetService.createAsset(request);
//
//        assertThatThrownBy(() -> assetService.createAsset(request))
//                .isInstanceOf(org.dev.server.exception.AssetAlreadyExistsException.class);
//
//        assertThatThrownBy(() -> assetService.getAsset(9999L))
//                .isInstanceOf(org.dev.server.exception.AssetNotFoundException.class);
//
//        assertThatThrownBy(() -> assetService.updateAsset(9999L, request))
//                .isInstanceOf(org.dev.server.exception.AssetNotFoundException.class);
//
//        AssetRequestDto second = new AssetRequestDto("DOGE", "Dogecoin", 0.5);
//        AssetResponseDto secondSaved = assetService.createAsset(second);
//
//        assertThatThrownBy(() -> assetService.updateAsset(secondSaved.assetId(), request))
//                .isInstanceOf(org.dev.server.exception.AssetAlreadyExistsException.class);
//
//        assertThatThrownBy(() -> assetService.deleteAsset(9999L))
//                .isInstanceOf(org.dev.server.exception.AssetNotFoundException.class);
//    }
//}
//
//
