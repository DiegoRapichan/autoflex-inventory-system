package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.service.RawMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controller REST para Matérias-Primas
//Base URL: /api/raw-materials
@Slf4j
@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@Tag(name = "Raw Materials", description = "Raw material management endpoints")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class RawMaterialController {
    
    private final RawMaterialService rawMaterialService;
    
    @GetMapping
    @Operation(summary = "List all raw materials")
    public ResponseEntity<List<RawMaterialDTO>> getAllRawMaterials() {
        log.info("GET /api/raw-materials - Listing all raw materials");
        List<RawMaterialDTO> materials = rawMaterialService.findAll();
        return ResponseEntity.ok(materials);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get raw material by ID")
    public ResponseEntity<RawMaterialDTO> getRawMaterialById(@PathVariable Long id) {
        log.info("GET /api/raw-materials/{} - Getting raw material by id", id);
        RawMaterialDTO material = rawMaterialService.findById(id);
        return ResponseEntity.ok(material);
    }
    
    /**
     * POST /api/raw-materials
     * 
     * Request Body:
     * {
     *   "code": "MAT-001",
     *   "name": "Steel Plate",
     *   "stockQuantity": 100.500,
     *   "unit": "KG"
     * }
     */
    @PostMapping
    @Operation(summary = "Create new raw material")
    public ResponseEntity<RawMaterialDTO> createRawMaterial(@Valid @RequestBody RawMaterialDTO dto) {
        log.info("POST /api/raw-materials - Creating new material: {}", dto.getCode());
        RawMaterialDTO created = rawMaterialService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update raw material")
    public ResponseEntity<RawMaterialDTO> updateRawMaterial(
            @PathVariable Long id,
            @Valid @RequestBody RawMaterialDTO dto) {
        log.info("PUT /api/raw-materials/{} - Updating material", id);
        RawMaterialDTO updated = rawMaterialService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete raw material")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        log.info("DELETE /api/raw-materials/{} - Deleting material", id);
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}