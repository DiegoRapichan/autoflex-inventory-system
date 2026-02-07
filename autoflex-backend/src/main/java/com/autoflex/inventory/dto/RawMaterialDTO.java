package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


//DTO para transferência de dados de Matéria-Prima
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDTO {
    

    //ID da matéria-prima (gerado automaticamente)
    private Long id;
    
    //Código único da matéria-prima
    @NotBlank(message = "Material code is required")
    private String code;
    
    //Nome da matéria-primA
    @NotBlank(message = "Material name is required")
    private String name;
    
    //Quantidade disponível em estoque
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private BigDecimal stockQuantity;
    

    //Unidade de medida
    private String unit;
    
    //Data de criação
    private LocalDateTime createdAt;
    
    //Data da última atualização
    private LocalDateTime updatedAt;
    
    //Indica se o estoque está baixo
    private Boolean lowStock;
    
    //Número de produtos que usam esta matéria-prima
    private Integer usedInProductsCount;
}