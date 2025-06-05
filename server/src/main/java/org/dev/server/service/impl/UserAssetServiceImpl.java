package org.dev.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.user.UserResponseDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.exception.DatabaseException;
import org.dev.server.exception.InsufficientQuantityException;
import org.dev.server.exception.JdbcException;
import org.dev.server.mapper.AssetMapper;
import org.dev.server.mapper.UserMapper;
import org.dev.server.model.Asset;
import org.dev.server.model.User;
import org.dev.server.repository.AssetRepository;
import org.dev.server.repository.UserAssetRepository;
import org.dev.server.repository.UserRepository;
import org.dev.server.service.UserAssetService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAssetServiceImpl implements UserAssetService {

    private final UserAssetRepository userAssetRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    public void addAssetToUser(UUID userId, Long assetId, BigDecimal quantity) {
        try {
            userAssetRepository.addAssetToUser(userId, assetId, quantity);
        } catch (DuplicateKeyException ex) {
            throw new JdbcException("Asset " + assetId + " is already assigned to user " + userId);
        } catch (DataIntegrityViolationException ex) {
            throw new JdbcException("Failed to insert asset " + assetId + " for user " + userId + ": One of these IDs may not exist.");
        } catch (Exception ex) {
            throw new DatabaseException("A problem occurred. Please try again later.");
        }
    }

    @Override
    public void removeAssetFromUser(UUID userId, Long assetId) {
        try {
            userAssetRepository.removeAssetFromUser(userId, assetId);
        } catch (Exception ex) {
            throw new DatabaseException("Failed to remove asset " + assetId + " from user " + userId);
        }
    }

    @Override
    public List<Long> getAssetsIdByUserId(UUID userId) {
        try {
            return userAssetRepository.getAssetIdsByUserId(userId);
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch asset IDs for user " + userId);
        }
    }

    @Override
    public List<UUID> getUsersIdByAssetId(Long assetId) {
        try {
            return userAssetRepository.getUserIdsByAssetId(assetId);
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch user IDs for asset " + assetId);
        }
    }

    @Override
    public List<AssetResponseDto> getAssetsByUserId(UUID userId) {
        try {
            List<Long> assetIds = userAssetRepository.getAssetIdsByUserId(userId);
            List<Asset> assets = assetRepository.findAllById(assetIds);
            return assets.stream()
                    .map(AssetMapper::toDto)
                    .toList();
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch assets for user " + userId);
        }
    }

    @Override
    public List<UserResponseDto> getUsersByAssetId(Long assetId) {
        try {
            List<UUID> userIds = userAssetRepository.getUserIdsByAssetId(assetId);
            List<User> users = userRepository.findAllById(userIds);
            return users.stream()
                    .map(UserMapper::toDto)
                    .toList();
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch users for asset " + assetId);
        }
    }

    @Override
    public void updateAssetQuantity(UUID userId, Long assetId, BigDecimal quantityChange) {
        try {
            boolean ownsAsset = userAssetRepository.userOwnsAsset(userId, assetId);

            if (!ownsAsset) {
                if (quantityChange.compareTo(BigDecimal.ZERO) < 0) {
// daca user incearca sa vanda si nu are nici asset-ul
                    throw new InsufficientQuantityException("User " + userId + " does not own asset " + assetId + " to sell.");
                }
                userAssetRepository.addAssetToUser(userId, assetId, quantityChange);
            } else {
                BigDecimal currentQuantity = userAssetRepository.getQuantityByUserIdAndAssetId(userId, assetId);
                BigDecimal newQuantity = currentQuantity.add(quantityChange);

                if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    //daca e negativa noua cantitate
                    throw new InsufficientQuantityException("User " + userId + " does not have enough of asset " + assetId + " to sell.");
                } else if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
                    //daca am sters tot -> remove
                    userAssetRepository.removeAssetFromUser(userId, assetId);
                } else {
                    //update normal
                    userAssetRepository.updateUserAssetQuantity(userId, assetId, newQuantity);
                }
            }
        } catch (InsufficientQuantityException e) {
            throw e; // o rearunc, se ocupa @GlobalExceptionHandler
        } catch (Exception ex) {
            throw new DatabaseException("An unexpected error occurred while updating asset quantity.");
        }
    }

    @Override
    public BigDecimal getUserQuantity(UUID userId, Long assetId) {
        try {
            return userAssetRepository.getQuantityByUserIdAndAssetId(userId, assetId);
        } catch (Exception ex) {
            throw new DatabaseException("Failed to fetch quantity for user " + userId + " and asset " + assetId);
        }
    }

}
