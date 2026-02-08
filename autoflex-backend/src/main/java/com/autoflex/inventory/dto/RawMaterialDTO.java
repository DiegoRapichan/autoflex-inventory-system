package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDTO {
    private Long id;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotBlank(message = "Name is required")
    private String name;

    private Integer usedInProductsCount;

    
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private BigDecimal stockQuantity;
    
    @NotBlank(message = "Unit is required")
    private String unit;
    
    private Boolean lowStock;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
