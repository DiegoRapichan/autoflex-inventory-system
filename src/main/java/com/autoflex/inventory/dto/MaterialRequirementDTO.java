package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO com detalhes de requisito de material para produção
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequirementDTO {
    
    //Nome da matéria-prima
    private String materialName;
    
    //Código da matéria-prima
    private String materialCode;
    
     //Quantidade necessária por unidade de produto
    private BigDecimal requiredPerUnit;
    

    //Quantidade disponível em estoque
    private BigDecimal availableStock;
    

    //Quantidade necessária total para produzir maxQuantity
    private BigDecimal totalRequired;
    
    //Quantidade que sobrará após a produção
    private BigDecimal remainingStock;
    

    //Unidade de medida
    private String unit;
    
    //Indica se há estoque suficiente
    private Boolean sufficient;
}
