package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private ProductDTO product;
    private BigDecimal unitValue;
    private Integer maxQuantity;
    private BigDecimal totalValue;
    private Boolean canProduce;
    private String limitingMaterial;
    private List<MaterialRequirementDTO> materialRequirements;
    private List<String> missingMaterials;

}
