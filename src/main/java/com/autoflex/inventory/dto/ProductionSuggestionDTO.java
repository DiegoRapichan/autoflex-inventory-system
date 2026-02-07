package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para resposta de sugestão de produção
 * Contém informações sobre quais produtos podem ser produzidos e em quais quantidades
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionDTO {
    
    //Informações do produto
    private ProductDTO product;
    
    //Quantidade máxima que pode ser produzida com o estoque atual, Calculado baseado na matéria-prima mais limitante
    private Integer maxQuantity;
    

    //Valor total se produzir maxQuantity unidades, Calculado como: product.value * maxQuantity
    private BigDecimal totalValue;
    
    //Lista de matérias-primas necessárias com informações de estoque
    private List<MaterialRequirementDTO> materialRequirements;
    
    //Indica se é possível produzir pelo menos 1 unidade
    private Boolean canProduce;
    

    //Matérias-primas que estão faltando ou insuficientes
    private List<String> missingMaterials;
}