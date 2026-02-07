package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


//DTO para transferência de dados de Produto, Usado para receber dados do frontend e enviar respostas
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    //ID do produto (gerado automaticamente, não enviar no POST)
    private Long id;
    
    //Código único do produto
    @NotBlank(message = "Product code is required")
    private String code;
    

    //Nome do produto
    @NotBlank(message = "Product name is required")
    private String name;
    

    //Valor unitário do produto


    @NotNull(message = "Product value is required")
    @Positive(message = "Product value must be positive")
    private BigDecimal value;
    
    //Lista de matérias-primas necessárias
    private List<ProductRawMaterialDTO> rawMaterials;
    
    //Data de criação (preenchido automaticamente)
    private LocalDateTime createdAt;
    
     //Data da última atualização
    private LocalDateTime updatedAt;
    

    //Quantidade máxima que pode ser produzida
    //Calculado dinamicamente baseado no estoque
    //Usado apenas em respostas de sugestão de produção

    private Integer maxQuantityProducible;
    

    //Valor total se produzir maxQuantityProducible
    //Usado apenas em respostas de sugestão de produção
    private BigDecimal totalValue;
}
