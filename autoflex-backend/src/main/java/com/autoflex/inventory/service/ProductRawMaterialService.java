package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRawMaterialService {
    
    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialService rawMaterialService;
    
    /**
     * Lista todas as matérias-primas de um produto
     */
    @Transactional(readOnly = true)
    public List<ProductRawMaterialDTO> findByProductId(Long productId) {
        log.info("Finding materials for product id: {}", productId);
        
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return productRawMaterialRepository.findByProductId(productId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Adiciona matéria-prima a um produto
     */
    @Transactional
    public ProductRawMaterialDTO addMaterialToProduct(Long productId, ProductRawMaterialDTO dto) {
        log.info("Adding material {} to product {}", dto.getRawMaterialId(), productId);
        
        // Buscar produto
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        // Buscar matéria-prima
        RawMaterial material = rawMaterialRepository.findById(dto.getRawMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + dto.getRawMaterialId()));
        
        // Verificar se já existe associação
        if (productRawMaterialRepository.findByProductIdAndRawMaterialId(productId, dto.getRawMaterialId()).isPresent()) {
            throw new BusinessException("This material is already associated with this product");
        }
        
        // Criar associação
        ProductRawMaterial association = new ProductRawMaterial();
        association.setProduct(product);
        association.setRawMaterial(material);
        association.setRequiredQuantity(dto.getRequiredQuantity());
        
        ProductRawMaterial saved = productRawMaterialRepository.save(association);
        log.info("Material added successfully to product");
        
        return toDTO(saved);
    }
    
    /**
     * Atualiza quantidade necessária de uma matéria-prima
     */
    @Transactional
    public ProductRawMaterialDTO updateMaterialQuantity(Long productId, Long materialId, ProductRawMaterialDTO dto) {
        log.info("Updating material {} quantity for product {}", materialId, productId);
        
        ProductRawMaterial association = productRawMaterialRepository
                .findByProductIdAndRawMaterialId(productId, materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Association not found"));
        
        association.setRequiredQuantity(dto.getRequiredQuantity());
        ProductRawMaterial updated = productRawMaterialRepository.save(association);
        
        log.info("Material quantity updated successfully");
        return toDTO(updated);
    }
    
    /**
     * Remove matéria-prima de um produto
     */
    @Transactional
    public void removeMaterialFromProduct(Long productId, Long materialId) {
        log.info("Removing material {} from product {}", materialId, productId);
        
        if (!productRawMaterialRepository.findByProductIdAndRawMaterialId(productId, materialId).isPresent()) {
            throw new ResourceNotFoundException("Association not found");
        }
        
        productRawMaterialRepository.deleteByProductIdAndRawMaterialId(productId, materialId);
        log.info("Material removed successfully from product");
    }
    
    // ========================================================================
    // CONVERSÃO
    // ========================================================================
    
    private ProductRawMaterialDTO toDTO(ProductRawMaterial entity) {
        return ProductRawMaterialDTO.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .rawMaterialId(entity.getRawMaterial().getId())
                .rawMaterial(rawMaterialService.toDTO(entity.getRawMaterial()))
                .requiredQuantity(entity.getRequiredQuantity())
                .availableStock(entity.getRawMaterial().getStockQuantity())
                .hasSufficientStock(
                    entity.getRawMaterial().getStockQuantity()
                        .compareTo(entity.getRequiredQuantity()) >= 0
                )
                .build();
    }
}