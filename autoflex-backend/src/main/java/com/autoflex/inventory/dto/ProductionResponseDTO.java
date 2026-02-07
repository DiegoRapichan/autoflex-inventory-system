package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

//DTO para resposta completa do endpoint de sugestão de produção, Contém lista de sugestões e totalizadores 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionResponseDTO {
    
    //Lista de sugestões de produção ordenadas por valor (maior primeiro)
    private List<ProductionSuggestionDTO> suggestions;
    

    //Valor total que pode ser obtido produzindo todos os produtos sugeridos
    private BigDecimal totalProductionValue;
    
    //Quantidade total de produtos diferentes que podem ser produzidos
    private Integer totalProductTypes;
    
     //Quantidade total de unidades que podem ser produzidas (soma de todos)
    private Integer totalUnits;
    
    //Timestamp da geração do relatório
    private String generatedAt;
    

    //Mensagens ou avisos sobre o cálculo
    private List<String> warnings;
}
