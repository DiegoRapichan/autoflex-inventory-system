package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    Optional<RawMaterial> findByCode(String code);
    boolean existsByCode(String code);
}
