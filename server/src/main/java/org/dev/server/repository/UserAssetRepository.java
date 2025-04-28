package org.dev.server.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAssetRepository {

    private final JdbcTemplate jdbcTemplate;

    public void addAssetToUser(UUID userId, Long assetId, BigDecimal quantity) {
        String sql = "INSERT INTO user_asset (user_id, asset_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, assetId, quantity);
    }

    public void removeAssetFromUser(UUID userId, Long assetId) {
        String sql = "DELETE FROM user_asset WHERE user_id = ? AND asset_id = ?";
        jdbcTemplate.update(sql, userId, assetId);
    }

    public boolean userOwnsAsset(UUID userId, Long assetId) {
        String sql = "SELECT COUNT(*) FROM user_asset WHERE user_id = ? AND asset_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, assetId);
        return count != null && count > 0;
    }

    public List<Long> getAssetIdsByUserId(UUID userId) {
        String sql = "SELECT asset_id FROM user_asset WHERE user_id = ?";
        return new ArrayList<>(jdbcTemplate.queryForList(sql, Long.class, userId));
    }

    public List<UUID> getUserIdsByAssetId(Long assetId) {
        String sql = "SELECT user_id FROM user_asset WHERE asset_id = ?";
        return new ArrayList<>(jdbcTemplate.queryForList(sql, UUID.class, assetId));
    }

    public void updateUserAssetQuantity(UUID userId, Long assetId, BigDecimal newQuantity) {
        String sql = "UPDATE user_asset SET quantity = ? WHERE user_id = ? AND asset_id = ?";
        jdbcTemplate.update(sql, newQuantity, userId, assetId);
    }

    public BigDecimal getQuantityByUserIdAndAssetId(UUID userId, Long assetId) {
        String sql = "SELECT quantity FROM user_asset WHERE user_id = ? AND asset_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId, assetId);
    }


}
