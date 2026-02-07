package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


//DTO para associação entre Produto e Matéria-Prima
//Representa quanto de cada matéria-prima é necessário para produzir 1 unidade do produto
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRawMaterialDTO {
    

    //ID da associação
    private Long id;
    

    //ID do produto
    private Long productId;
    
    //Dados completos do produto
    private ProductDTO product;
    

    //ID da matéria-prima
    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;
    
    //Dados completos da matéria-prima
    //Preenchido em respostas
    private RawMaterialDTO rawMaterial;
    

    //Quantidade necessária da matéria-prima para produzir 1 unidade do produto
    @NotNull(message = "Required quantity is required")
    @Positive(message = "Required quantity must be positive")
    private BigDecimal requiredQuantity;
    
    //Quantidade disponível em estoque
    private BigDecimal availableStock;
    

    //Indica se há estoque suficiente
    private Boolean hasSufficientStock;
}