package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações CRUD de Produtos
 * 
 * Base URL: /api/products
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * GET /api/products
     * Lista todos os produtos
     * 
     * @return Lista de ProductDTO
     */
    @GetMapping
    @Operation(summary = "List all products", description = "Get a list of all registered products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.info("GET /api/products - Listing all products");
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/{id}
     * Busca produto por ID
     * 
     * @param id ID do produto
     * @return ProductDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Get detailed information about a specific product")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Getting product by id", id);
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
    
    /**
     * GET /api/products/{id}/with-materials
     * Busca produto com lista de matérias-primas
     * 
     * @param id ID do produto
     * @return ProductDTO com rawMaterials preenchido
     */
    @GetMapping("/{id}/with-materials")
    @Operation(summary = "Get product with materials", description = "Get product details including all associated raw materials")
    public ResponseEntity<ProductDTO> getProductWithMaterials(@PathVariable Long id) {
        log.info("GET /api/products/{}/with-materials - Getting product with materials", id);
        ProductDTO product = productService.findByIdWithMaterials(id);
        return ResponseEntity.ok(product);
    }
    
    /**
     * POST /api/products
     * Cria novo produto
     * 
     * Request Body:
     * {
     *   "code": "PROD-001",
     *   "name": "Product Name",
     *   "value": 150.00
     * }
     * 
     * @param productDTO Dados do produto
     * @return ProductDTO criado (status 201)
     */
    @PostMapping
    @Operation(summary = "Create new product", description = "Register a new product in the system")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("POST /api/products - Creating new product: {}", productDTO.getCode());
        ProductDTO created = productService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/products/{id}
     * Atualiza produto existente
     * 
     * @param id ID do produto
     * @param productDTO Novos dados
     * @return ProductDTO atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product's information")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("PUT /api/products/{} - Updating product", id);
        ProductDTO updated = productService.update(id, productDTO);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * DELETE /api/products/{id}
     * Deleta produto
     * 
     * @param id ID do produto
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Remove a product from the system")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}