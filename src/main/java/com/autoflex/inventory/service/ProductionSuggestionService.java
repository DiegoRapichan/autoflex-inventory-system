package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.*;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela sugestão de produção
 *Buscar todos os produtos ordenados por VALOR (maior primeiro)
 *Para cada produto, calcular quantas unidades podem ser produzidas
 *Quantidade máxima = MIN(estoque_material / quantidade_necessaria) para todos os materiais
 *Priorizar produtos de maior valor
 *Retornar lista de sugestões com detalhes
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionSuggestionService {
    
    private final ProductService productService;
    private final ProductRawMaterialRepository productRawMaterialRepository;
    

    //Calcula sugestões de produção baseado no estoque atual
    //@return ProductionResponseDTO com lista de sugestões e totalizadores

    @Transactional(readOnly = true)
    public ProductionResponseDTO calculateProductionSuggestions() {
        log.info("Starting production suggestion calculation");
        
        //Buscar produtos ordenados por valor (maior primeiro)
        List<Product> products = productService.findAllOrderedByValue();
        log.info("Found {} products to analyze", products.size());
        
        //Calcular sugestões para cada produto
        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;
        int totalUnits = 0;
        
        for (Product product : products) {
            //Calcular sugestão para este produto
            ProductionSuggestionDTO suggestion = calculateForProduct(product);
            
            //adicionar apenas se for possível produzir pelo menos 1 unidade
            if (suggestion.getCanProduce() && suggestion.getMaxQuantity() > 0) {
                suggestions.add(suggestion);
                totalValue = totalValue.add(suggestion.getTotalValue());
                totalUnits += suggestion.getMaxQuantity();
            }
        }
        
        log.info("Production suggestions calculated. Total products: {}, Total units: {}, Total value: {}",
                suggestions.size(), totalUnits, totalValue);
        
        //Montar resposta
        return ProductionResponseDTO.builder()
                .suggestions(suggestions)
                .totalProductionValue(totalValue)
                .totalProductTypes(suggestions.size())
                .totalUnits(totalUnits)
                .generatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .warnings(generateWarnings(suggestions))
                .build();
    }
    
     //Calcula sugestão para UM produto específicO
    //@param product Produto a analisar
     //@return ProductionSuggestionDTO com detalhes
    private ProductionSuggestionDTO calculateForProduct(Product product) {
        log.debug("Calculating suggestion for product: {} ({})", product.getName(), product.getCode());
        
        //Buscar matérias-primas necessárias
        List<ProductRawMaterial> materials = productRawMaterialRepository.findByProductId(product.getId());
        
        //Se não tem matérias-primas cadastradas, não pode produzir
        if (materials.isEmpty()) {
            log.debug("Product {} has no materials configured", product.getCode());
            return createEmptySuggestion(product, "No materials configured for this product");
        }
        
        //Calcular quantidade máxima produzível
        CalculationResult result = calculateMaxQuantity(materials);
        
        //Montar lista de detalhes de materiais
        List<MaterialRequirementDTO> requirements = materials.stream()
                .map(m -> createMaterialRequirement(m, result.maxQuantity))
                .collect(Collectors.toList());
        
        //calcular valor total
        BigDecimal totalValue = product.getValue()
                .multiply(BigDecimal.valueOf(result.maxQuantity))
                .setScale(2, RoundingMode.HALF_UP);
        
        //Converter produto para DTO
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .value(product.getValue())
                .build();
        
        return ProductionSuggestionDTO.builder()
                .product(productDTO)
                .maxQuantity(result.maxQuantity)
                .totalValue(totalValue)
                .materialRequirements(requirements)
                .canProduce(result.maxQuantity > 0)
                .missingMaterials(result.missingMaterials)
                .build();
    }
    
     //Calcula quantidade máxima produzível

     //Para cada matéria-prima, calcular: estoque_disponivel / quantidade_necessaria
     //Quantidade máxima = MENOR valor entre todos os materiais
     //Se qualquer material tiver estoque = 0, não pode produzir
     //@param materials Lista de matérias-primas do produto
     //@return CalculationResult com maxQuantity e lista de materiais faltantes
    private CalculationResult calculateMaxQuantity(List<ProductRawMaterial> materials) {
        Integer maxQuantity = Integer.MAX_VALUE;
        List<String> missingMaterials = new ArrayList<>();
        
        for (ProductRawMaterial prm : materials) {
            BigDecimal availableStock = prm.getRawMaterial().getStockQuantity();
            BigDecimal requiredPerUnit = prm.getRequiredQuantity();
            
            //se não tem estoque, adiciona na lista de faltantes
            if (availableStock.compareTo(BigDecimal.ZERO) == 0) {
                missingMaterials.add(prm.getRawMaterial().getName() + " (out of stock)");
                maxQuantity = 0;
                continue;
            }
            
            //calcular quantas unidades podem ser produzidas com este material
            int possibleUnits = availableStock
                    .divide(requiredPerUnit, 0, RoundingMode.DOWN) // Arredonda pra baixo
                    .intValue();
            
            //O limitante é sempre o MENOR valor
            maxQuantity = Math.min(maxQuantity, possibleUnits);
            
            //Se estoque é insuficiente para 1 unidade
            if (possibleUnits == 0) {
                missingMaterials.add(prm.getRawMaterial().getName() + " (insufficient stock)");
            }
        }
        
        //Se maxQuantity ficou como Integer.MAX_VALUE, significa que não tinha materiais
        if (maxQuantity == Integer.MAX_VALUE) {
            maxQuantity = 0;
        }
        
        return new CalculationResult(maxQuantity, missingMaterials);
    }
    

    //Cria DTO com detalhes de requisito de material
    private MaterialRequirementDTO createMaterialRequirement(
            ProductRawMaterial prm, 
            Integer quantityToProduce) {
        
        BigDecimal availableStock = prm.getRawMaterial().getStockQuantity();
        BigDecimal requiredPerUnit = prm.getRequiredQuantity();
        
        //Quantidade total necessária para produzir quantityToProduce unidades
        BigDecimal totalRequired = requiredPerUnit
                .multiply(BigDecimal.valueOf(quantityToProduce))
                .setScale(3, RoundingMode.HALF_UP);
        
        //Quanto vai sobrar após produção
        BigDecimal remainingStock = availableStock
                .subtract(totalRequired)
                .setScale(3, RoundingMode.HALF_UP);
        
        return MaterialRequirementDTO.builder()
                .materialName(prm.getRawMaterial().getName())
                .materialCode(prm.getRawMaterial().getCode())
                .requiredPerUnit(requiredPerUnit)
                .availableStock(availableStock)
                .totalRequired(totalRequired)
                .remainingStock(remainingStock)
                .unit(prm.getRawMaterial().getUnit())
                .sufficient(availableStock.compareTo(totalRequired) >= 0)
                .build();
    }
    

    //Cria sugestão vazia (quando não pode produzir)
    private ProductionSuggestionDTO createEmptySuggestion(Product product, String reason) {
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .value(product.getValue())
                .build();
        
        return ProductionSuggestionDTO.builder()
                .product(productDTO)
                .maxQuantity(0)
                .totalValue(BigDecimal.ZERO)
                .materialRequirements(new ArrayList<>())
                .canProduce(false)
                .missingMaterials(List.of(reason))
                .build();
    }
    

    //Gera avisos baseado nas sugestões
    private List<String> generateWarnings(List<ProductionSuggestionDTO> suggestions) {
        List<String> warnings = new ArrayList<>();
        
        if (suggestions.isEmpty()) {
            warnings.add("No products can be produced with current stock");
        }
        
        // Verificar produtos que não podem ser produzidos
        long cannotProduceCount = suggestions.stream()
                .filter(s -> !s.getCanProduce())
                .count();
        
        if (cannotProduceCount > 0) {
            warnings.add(cannotProduceCount + " product(s) cannot be produced due to insufficient stock");
        }
        
        return warnings;
    }
    
    //Classe auxiliar para retorno do cálculo
    private static class CalculationResult {
        Integer maxQuantity;
        List<String> missingMaterials;
        
        CalculationResult(Integer maxQuantity, List<String> missingMaterials) {
            this.maxQuantity = maxQuantity;
            this.missingMaterials = missingMaterials;
        }
    }
}

