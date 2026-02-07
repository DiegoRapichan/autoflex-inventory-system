package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByCode(String code);
    
    List<Product> findAllByOrderByValueDesc();
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.rawMaterials WHERE p.id = :id")
    Optional<Product> findByIdWithMaterials(Long id);
    
    boolean existsByCode(String code);
}