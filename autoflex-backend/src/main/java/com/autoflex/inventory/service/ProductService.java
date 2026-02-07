package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.exception.BusinessException;
import com.autoflex.inventory.exception.ResourceNotFoundException;
import com.autoflex.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

 //Service responsável pela lógica de negócio de Produtos

@Slf4j
@Service
@RequiredArgsConstructor //Lombok cria construtor com campos final
public class ProductService {
    
    private final ProductRepository productRepository;
    private final RawMaterialService rawMaterialService;
    

     //Listar todos os produtos, @return Lista de ProductDTO

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.info("Finding all products");
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
     //Buscar produto por ID, @param id ID do produto , @return ProductDTO com dados do produto,
     // @throws ResourceNotFoundException se produto não existir
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        log.info("Finding product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDTO(product);
    }
    

     //Buscar produto por ID com todas as matérias-primas, @param id ID do produto, @return ProductDTO com lista de matérias-primas
    @Transactional(readOnly = true)
    public ProductDTO findByIdWithMaterials(Long id) {
        log.info("Finding product with materials by id: {}", id);
        Product product = productRepository.findByIdWithMaterials(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDTOWithMaterials(product);
    }
    

     //Criar novo produto, @param dto Dados do produto, @return ProductDTO criado, @throws BusinessException se código já existir,
     // @throws BusinessException se código já existir
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        log.info("Creating new product with code: {}", dto.getCode());
        
        // Validação: código único
        if (productRepository.existsByCode(dto.getCode())) {
            throw new BusinessException("Product with code '" + dto.getCode() + "' already exists");
        }
        
        Product product = toEntity(dto);
        Product saved = productRepository.save(product);
        
        log.info("Product created successfully with id: {}", saved.getId());
        return toDTO(saved);
    }
    

     //Atualizar produto existente  @param id ID do produto, @param dto Novos dados
     // @return ProductDTO atualizado
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        log.info("Updating product with id: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        // Validação: se mudou o código, verificar se novo código não existe
        if (!product.getCode().equals(dto.getCode())) {
            if (productRepository.existsByCode(dto.getCode())) {
                throw new BusinessException("Product with code '" + dto.getCode() + "' already exists");
            }
        }
        
        //Atualizar campos
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setValue(dto.getValue());
        
        Product updated = productRepository.save(product);
        log.info("Product updated successfully");
        
        return toDTO(updated);
    }
    
    
     //Deletar produto, @param id ID do produto
    @Transactional
    public void delete(Long id) {
        log.info("Deleting product with id: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully");
    }
    

     //Listar produtos ordenados por valor (maior primeiro), Usado no algoritmo de sugestão de produção
    @Transactional(readOnly = true)
    public List<Product> findAllOrderedByValue() {
        log.info("Finding all products ordered by value desc");
        return productRepository.findAllByOrderByValueDesc();
    }

     //Converter Entity para DTO (sem matérias-primas)
    private ProductDTO toDTO(Product entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .value(entity.getValue())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    

     //Converter Entity para DTO (COM matérias-primas)
    private ProductDTO toDTOWithMaterials(Product entity) {
        ProductDTO dto = toDTO(entity);
        
        //Mapear matérias-primas
        List<ProductRawMaterialDTO> materials = entity.getRawMaterials().stream()
                .map(this::toMaterialDTO)
                .collect(Collectors.toList());
        
        dto.setRawMaterials(materials);
        return dto;
    }
    

    //Converter ProductRawMaterial para DTO
    private ProductRawMaterialDTO toMaterialDTO(ProductRawMaterial entity) {
        return ProductRawMaterialDTO.builder()
                .id(entity.getId())
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
    

     //Converter DTO para Entity (para criar)
    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setValue(dto.getValue());
        return product;
    }
}