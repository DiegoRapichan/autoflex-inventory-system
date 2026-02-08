package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequirementDTO {
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String unit;
    private BigDecimal requiredPerUnit;
    private BigDecimal totalRequired;
    private BigDecimal availableStock;
    private BigDecimal remainingStock;
    private Boolean sufficient;
}
