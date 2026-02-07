package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.service.ProductRawMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

 //Controller para gerenciar associação entre Produtos e Matérias-Primas
 //Base URL: /api/products/{productId}/materials
@Slf4j
@RestController
@RequestMapping("/api/products/{productId}/materials")
@RequiredArgsConstructor
@Tag(name = "Product-Material Association", description = "Endpoints to manage product and raw material associations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ProductRawMaterialController {
    
    private final ProductRawMaterialService service;
    
     //GET /api/products/{productId}/materials
     //lista matérias-primas de um produto
    @GetMapping
    @Operation(summary = "List materials of a product")
    public ResponseEntity<List<ProductRawMaterialDTO>> getProductMaterials(@PathVariable Long productId) {
        log.info("GET /api/products/{}/materials - Listing materials", productId);
        List<ProductRawMaterialDTO> materials = service.findByProductId(productId);
        return ResponseEntity.ok(materials);
    }
    
    //POST /api/products/{productId}/materials
     //Adiciona matéria-prima ao produto
     //Request Body:
     //{
     //   "rawMaterialId": 1,
     //   "requiredQuantity": 2.5
     //}
    @PostMapping
    @Operation(summary = "Add material to product")
    public ResponseEntity<ProductRawMaterialDTO> addMaterialToProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRawMaterialDTO dto) {
        log.info("POST /api/products/{}/materials - Adding material {}", productId, dto.getRawMaterialId());
        ProductRawMaterialDTO created = service.addMaterialToProduct(productId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    //PUT /api/products/{productId}/materials/{materialId}
     //Atualiza quantidade necessária
    @PutMapping("/{materialId}")
    @Operation(summary = "Update material quantity")
    public ResponseEntity<ProductRawMaterialDTO> updateMaterialQuantity(
            @PathVariable Long productId,
            @PathVariable Long materialId,
            @Valid @RequestBody ProductRawMaterialDTO dto) {
        log.info("PUT /api/products/{}/materials/{} - Updating quantity", productId, materialId);
        ProductRawMaterialDTO updated = service.updateMaterialQuantity(productId, materialId, dto);
        return ResponseEntity.ok(updated);
    }
    
    //DELETE /api/products/{productId}/materials/{materialId}
    //Remove matéria-prima do produto
    @DeleteMapping("/{materialId}")
    @Operation(summary = "Remove material from product")
    public ResponseEntity<Void> removeMaterialFromProduct(
            @PathVariable Long productId,
            @PathVariable Long materialId) {
        log.info("DELETE /api/products/{}/materials/{} - Removing material", productId, materialId);
        service.removeMaterialFromProduct(productId, materialId);
        return ResponseEntity.noContent().build();
    }
}
