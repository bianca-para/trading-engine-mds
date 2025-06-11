package org.dev.server.repository;

import org.dev.server.model.Order;
import org.dev.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;                         // ← import Query
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
    @Query("SELECT o FROM Order o WHERE o.asset.id = :assetId")
    List<Order> fetchByAssetJpa(@Param("assetId") Long assetId);
}
