
// DESCRIÇÃO: DTO para respostas de erro padronizadas
// ============================================================================

package com.autoflex.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


 //DTO para respostas de erro padronizadas da API, Retornado quando ocorre alguma exception
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    
    //Timestamp do erro
    private LocalDateTime timestamp;
    

    //Código HTTP do status
    private Integer status;
    

    //Nome do status HTTP
    private String error;
    
    //Mensagem principal do erro
    private String message;
    
    //Lista de erros de validação (se aplicável)
    private List<String> errors;
    

    //Path da requisição que gerou o erro
    private String path;
}