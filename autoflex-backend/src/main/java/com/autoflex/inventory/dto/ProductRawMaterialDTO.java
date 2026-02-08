package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRawMaterialDTO {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productName;

    private BigDecimal availableStock;

     private Boolean hasSufficientStock; 
    
    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;
    
    private String rawMaterialName;
    private String unit;
    
    private RawMaterialDTO rawMaterial;
    
    @Positive(message = "Required quantity must be positive")
    private BigDecimal requiredQuantity;
}
