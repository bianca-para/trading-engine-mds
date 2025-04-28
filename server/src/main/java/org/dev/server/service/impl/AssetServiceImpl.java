package org.dev.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.exception.AssetAlreadyExistsException;
import org.dev.server.exception.AssetNotFoundException;
import org.dev.server.mapper.AssetMapper;
import org.dev.server.model.Asset;
import org.dev.server.repository.AssetRepository;
import org.dev.server.service.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Override
    public AssetResponseDto createAsset(AssetRequestDto assetRequestDto) {
        if (assetRepository.existsByNameOrSymbol(assetRequestDto.name(), assetRequestDto.symbol())) {
            throw new AssetAlreadyExistsException("Asset with the given name or symbol already exists.");
        }

        Asset asset = AssetMapper.toEntity(assetRequestDto);
        Asset savedAsset = assetRepository.save(asset);
        return AssetMapper.toDto(savedAsset);
    }

    @Override
    public AssetResponseDto getAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset with id " + id + " not found."));
        return AssetMapper.toDto(asset);
    }

    @Override
    public List<AssetResponseDto> getAllAssets(){
        List<Asset> assets = assetRepository.findAll();
        return assets
                .stream()
                .map(AssetMapper::toDto)
                .toList();
    }

    @Override
    public AssetResponseDto updateAsset(Long id, AssetRequestDto assetRequestDto){
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset with id " + id + " not found."));

        //verificam ca datele symbol si nume sa nu se regaseasca in alt asset, decat cel pe care il modificam
        if(assetRepository.existsByNameOrSymbolAndIdNot(assetRequestDto.name(), assetRequestDto.symbol(), id)){
            throw new AssetAlreadyExistsException("Asset with the given name or symbol already exists.");
        }

        asset.setName(assetRequestDto.name());
        asset.setSymbol(assetRequestDto.symbol());
        asset.setPrice(assetRequestDto.price());

        Asset updatedAsset = assetRepository.save(asset);

        return AssetMapper.toDto(updatedAsset);
    }

    @Override
    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new AssetNotFoundException("Asset with id " + id + " not found.");
        }
        assetRepository.deleteById(id);
    }
}
