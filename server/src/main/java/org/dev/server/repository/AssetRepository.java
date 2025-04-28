package org.dev.server.repository;

import org.dev.server.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsByNameOrSymbol(String name, String symbol);

    boolean existsByNameOrSymbolAndIdNot(String name, String symbol, Long id);
}