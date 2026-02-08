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
public class ProductionResponseDTO {
    private List<ProductionSuggestionDTO> suggestions;
    private BigDecimal totalValue;
    private BigDecimal totalProductionValue;
    private Integer totalUnits;
    private List<String> warnings;
    private Integer totalProductTypes;
    private String generatedAt;


}
