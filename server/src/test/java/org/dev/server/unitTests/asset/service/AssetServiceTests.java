package org.dev.server.unitTests.asset.service;

import net.datafaker.Faker;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.exception.AssetAlreadyExistsException;
import org.dev.server.exception.AssetNotFoundException;
import org.dev.server.mapper.AssetMapper;
import org.dev.server.model.Asset;
import org.dev.server.repository.AssetRepository;
import org.dev.server.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//o sa urmam o arhitectura de trophy testing, adica cateva unit tests basic si multe integration tests
@ExtendWith(MockitoExtension.class)
public class AssetServiceTests {
    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    //fake data
    private AssetRequestDto request;
    private AssetResponseDto response;
    private Asset asset;

    @BeforeEach
    public void initTestData() {
        Faker faker = new Faker();

        String rawSymbol =  faker.cryptoCoin().coin(); // ETH, BTC
        String symbol = rawSymbol.substring(0, Math.min(10, rawSymbol.length()));

        String rawName = faker.company().name();
        String name = rawName.substring(0, Math.min(50, rawName.length()));

        Double price = faker.number().randomDouble(2, 0, 10000);

        request = new AssetRequestDto(symbol, name, price);
        asset = new Asset(1L, request.symbol(), request.name(), request.price());
        response = new AssetResponseDto(1L, request.symbol(), request.name(), request.price());
    }
    @Test
    void createAsset_shouldCreateSuccessfully() {
        when(assetRepository.existsByNameOrSymbol(request.name(), request.symbol())).thenReturn(false);
        when(assetRepository.save(any())).thenReturn(asset);

        AssetResponseDto result = assetService.createAsset(request);

        assertThat(result).usingRecursiveComparison().isEqualTo(AssetMapper.toDto(asset));
        verify(assetRepository).save(any());
    }

    @Test
    void createAsset_shouldThrowWhenAlreadyExists() {
        when(assetRepository.existsByNameOrSymbol(request.name(), request.symbol())).thenReturn(true);

        assertThrows(AssetAlreadyExistsException.class, () -> assetService.createAsset(request));
        verify(assetRepository, never()).save(any());
    }

    @Test
    void getAsset_shouldReturnAsset() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        AssetResponseDto result = assetService.getAsset(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(AssetMapper.toDto(asset));
    }

    @Test
    void getAsset_shouldThrowIfNotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> assetService.getAsset(1L));
    }

    @Test
    void getAllAssets_shouldReturnAll() {
        when(assetRepository.findAll()).thenReturn(List.of(asset));


        List<AssetResponseDto> result = assetService.getAllAssets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(AssetMapper.toDto(asset));
    }

    @Test
    void updateAsset_shouldUpdateSuccessfully() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.existsByNameOrSymbolAndIdNot(request.name(), request.symbol(), 1L)).thenReturn(false);
        when(assetRepository.save(any())).thenReturn(asset);

        AssetResponseDto result = assetService.updateAsset(1L, request);

        assertThat(result).usingRecursiveComparison().isEqualTo(AssetMapper.toDto(asset));
    }

    @Test
    void updateAsset_shouldThrowIfNameOrSymbolExistsOnOther() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.existsByNameOrSymbolAndIdNot(request.name(), request.symbol(), 1L)).thenReturn(true);

        assertThrows(AssetAlreadyExistsException.class, () -> assetService.updateAsset(1L, request));
    }

    @Test
    void updateAsset_shouldThrowIfNotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(1L, request));
    }

    @Test
    void deleteAsset_shouldDeleteSuccessfully() {
        when(assetRepository.existsById(1L)).thenReturn(true);

        assetService.deleteAsset(1L);

        verify(assetRepository).deleteById(1L);
    }

    @Test
    void deleteAsset_shouldThrowIfNotFound() {
        when(assetRepository.existsById(1L)).thenReturn(false);

        assertThrows(AssetNotFoundException.class, () -> assetService.deleteAsset(1L));
    }


}
