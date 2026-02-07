package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawMaterialService {
    
    private final RawMaterialRepository rawMaterialRepository;
    private static final BigDecimal LOW_STOCK_THRESHOLD = BigDecimal.valueOf(10);
    
    @Transactional(readOnly = true)
    public List<RawMaterialDTO> findAll() {
        log.info("Finding all raw materials");
        return rawMaterialRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RawMaterialDTO findById(Long id) {
        log.info("Finding raw material by id: {}", id);
        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
        return toDTO(material);
    }
    
    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        log.info("Creating new raw material with code: {}", dto.getCode());
        
        if (rawMaterialRepository.existsByCode(dto.getCode())) {
            throw new BusinessException("Raw material with code '" + dto.getCode() + "' already exists");
        }
        
        RawMaterial material = toEntity(dto);
        RawMaterial saved = rawMaterialRepository.save(material);
        
        log.info("Raw material created successfully with id: {}", saved.getId());
        return toDTO(saved);
    }
    
    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        log.info("Updating raw material with id: {}", id);
        
        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
        
        // Validação de código único
        if (!material.getCode().equals(dto.getCode())) {
            if (rawMaterialRepository.existsByCode(dto.getCode())) {
                throw new BusinessException("Raw material with code '" + dto.getCode() + "' already exists");
            }
        }
        
        material.setCode(dto.getCode());
        material.setName(dto.getName());
        material.setStockQuantity(dto.getStockQuantity());
        material.setUnit(dto.getUnit());
        
        RawMaterial updated = rawMaterialRepository.save(material);
        log.info("Raw material updated successfully");
        
        return toDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        log.info("Deleting raw material with id: {}", id);
        
        if (!rawMaterialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Raw material not found with id: " + id);
        }
        
        rawMaterialRepository.deleteById(id);
        log.info("Raw material deleted successfully");
    }
    

    //Atualizar estoque de uma matéria-prima, Usado após produção
    @Transactional
    public void updateStock(Long id, BigDecimal newQuantity) {
        log.info("Updating stock for material id: {} to quantity: {}", id, newQuantity);
        
        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
        
        material.setStockQuantity(newQuantity);
        rawMaterialRepository.save(material);
    }
    

    
    public RawMaterialDTO toDTO(RawMaterial entity) {
        return RawMaterialDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .stockQuantity(entity.getStockQuantity())
                .unit(entity.getUnit())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .lowStock(entity.getStockQuantity().compareTo(LOW_STOCK_THRESHOLD) < 0)
                .usedInProductsCount(entity.getProducts() != null ? entity.getProducts().size() : 0)
                .build();
    }
    
    private RawMaterial toEntity(RawMaterialDTO dto) {
        RawMaterial material = new RawMaterial();
        material.setCode(dto.getCode());
        material.setName(dto.getName());
        material.setStockQuantity(dto.getStockQuantity());
        material.setUnit(dto.getUnit());
        return material;
    }
}
