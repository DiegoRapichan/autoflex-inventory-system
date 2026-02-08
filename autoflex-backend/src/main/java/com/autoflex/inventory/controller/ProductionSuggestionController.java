package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductionResponseDTO;
import com.autoflex.inventory.service.ProductionSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para cálculo de sugestão de produção
 * 
 * Base URL: /api/production
 */
@Slf4j
@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@Tag(name = "Production Suggestions", description = "Production calculation endpoints")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ProductionSuggestionController {
    
    private final ProductionSuggestionService productionService;
    
    /**
     * GET /api/production/suggestions
     * 
     * Calcula quais produtos podem ser produzidos com estoque atual
     * Retorna lista ordenada por valor (maior primeiro)
     * 
     * Response:
     * {
     *   "suggestions": [
     *     {
     *       "product": { "id": 1, "name": "Product A", "value": 150.00 },
     *       "maxQuantity": 10,
     *       "totalValue": 1500.00,
     *       "canProduce": true,
     *       "materialRequirements": [...]
     *     }
     *   ],
     *   "totalProductionValue": 5000.00,
     *   "totalProductTypes": 3,
     *   "totalUnits": 50
     * }
     */
    @GetMapping("/suggestions")
    @Operation(
        summary = "Calculate production suggestions",
        description = "Calculate which products can be produced with current stock, ordered by value (highest first)"
    )
    public ResponseEntity<ProductionResponseDTO> getProductionSuggestions() {
        log.info("GET /api/production/suggestions - Calculating production suggestions");
        ProductionResponseDTO suggestions = productionService.calculateProductionSuggestions();
        return ResponseEntity.ok(suggestions);
    }
}

// ============================================================================
// RESUMO DOS ENDPOINTS
// ============================================================================

/**
 * PRODUCTS:
 * GET    /api/products                          - Lista todos
 * GET    /api/products/{id}                     - Busca por ID
 * GET    /api/products/{id}/with-materials      - Busca com materiais
 * POST   /api/products                          - Cria novo
 * PUT    /api/products/{id}                     - Atualiza
 * DELETE /api/products/{id}                     - Deleta
 * 
 * RAW MATERIALS:
 * GET    /api/raw-materials                     - Lista todos
 * GET    /api/raw-materials/{id}                - Busca por ID
 * POST   /api/raw-materials                     - Cria novo
 * PUT    /api/raw-materials/{id}                - Atualiza
 * DELETE /api/raw-materials/{id}                - Deleta
 * 
 * ASSOCIATIONS:
 * GET    /api/products/{id}/materials           - Lista materiais do produto
 * POST   /api/products/{id}/materials           - Adiciona material
 * PUT    /api/products/{id}/materials/{matId}   - Atualiza quantidade
 * DELETE /api/products/{id}/materials/{matId}   - Remove associação
 * 
 * PRODUCTION:
 * GET    /api/production/suggestions            - Calcula sugestões
 */
