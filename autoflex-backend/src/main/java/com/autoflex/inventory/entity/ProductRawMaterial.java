package com.autoflex.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "product_raw_materials",
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "raw_material_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRawMaterial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;
    
    @Column(name = "required_quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal requiredQuantity;
}
