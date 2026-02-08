package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {
    List<ProductRawMaterial> findByProductId(Long productId);
    List<ProductRawMaterial> findByRawMaterialId(Long rawMaterialId);
    @Query("SELECT prm FROM ProductRawMaterial prm WHERE prm.product.id = :productId AND prm.rawMaterial.id = :rawMaterialId")
    Optional<ProductRawMaterial> findByProductIdAndRawMaterialId(Long productId, Long rawMaterialId);
    void deleteByProductIdAndRawMaterialId(Long productId, Long rawMaterialId);
}
