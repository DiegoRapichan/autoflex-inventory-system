package com.autoflex.inventory.config;

import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    @Profile("dev") // Só executa em ambiente de desenvolvimento
    CommandLineRunner initDatabase(
            RawMaterialRepository rawMaterialRepository,
            ProductRepository productRepository,
            ProductRawMaterialRepository productRawMaterialRepository) {
        
        return args -> {
            
            // Verificar se já tem dados
            if (rawMaterialRepository.count() > 0) {
                log.info("Database already populated. Skipping initialization.");
                return;
            }
            
            log.info("Initializing database with sample data...");
            
            // Matérias-Primas
            RawMaterial aco = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP001")
                    .name("Aço Inoxidável")
                    .unit("kg")
                    .stockQuantity(new BigDecimal("500.00"))
                    .minimumStock(new BigDecimal("100.00"))
                    .unitCost(new BigDecimal("25.50"))
                    .build());

            RawMaterial aluminio = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP002")
                    .name("Alumínio 6061")
                    .unit("kg")
                    .stockQuantity(new BigDecimal("300.00"))
                    .minimumStock(new BigDecimal("50.00"))
                    .unitCost(new BigDecimal("18.75"))
                    .build());

            RawMaterial borracha = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP003")
                    .name("Borracha EPDM")
                    .unit("kg")
                    .stockQuantity(new BigDecimal("150.00"))
                    .minimumStock(new BigDecimal("30.00"))
                    .unitCost(new BigDecimal("12.30"))
                    .build());

            RawMaterial plastico = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP004")
                    .name("Plástico ABS")
                    .unit("kg")
                    .stockQuantity(new BigDecimal("200.00"))
                    .minimumStock(new BigDecimal("40.00"))
                    .unitCost(new BigDecimal("8.90"))
                    .build());

            RawMaterial parafuso = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP005")
                    .name("Parafuso M8")
                    .unit("unidade")
                    .stockQuantity(new BigDecimal("5000.00"))
                    .minimumStock(new BigDecimal("1000.00"))
                    .unitCost(new BigDecimal("0.25"))
                    .build());

            RawMaterial tinta = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP006")
                    .name("Tinta Automotiva Preta")
                    .unit("litro")
                    .stockQuantity(new BigDecimal("80.00"))
                    .minimumStock(new BigDecimal("20.00"))
                    .unitCost(new BigDecimal("45.00"))
                    .build());

            RawMaterial vidro = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP007")
                    .name("Vidro Temperado")
                    .unit("m²")
                    .stockQuantity(new BigDecimal("50.00"))
                    .minimumStock(new BigDecimal("10.00"))
                    .unitCost(new BigDecimal("85.00"))
                    .build());

            RawMaterial espuma = rawMaterialRepository.save(RawMaterial.builder()
                    .code("MP008")
                    .name("Espuma Poliuretano")
                    .unit("kg")
                    .stockQuantity(new BigDecimal("100.00"))
                    .minimumStock(new BigDecimal("25.00"))
                    .unitCost(new BigDecimal("15.60"))
                    .build());

            // Produtos
            Product retrovisor = productRepository.save(Product.builder()
                    .code("PROD001")
                    .name("Retrovisor Externo Direito")
                    .description("Retrovisor externo elétrico com desembaçador")
                    .unitValue(new BigDecimal("285.00"))
                    .stockQuantity(45)
                    .minimumStock(10)
                    .build());

            Product parachoque = productRepository.save(Product.builder()
                    .code("PROD002")
                    .name("Para-choque Dianteiro")
                    .description("Para-choque em plástico ABS reforçado")
                    .unitValue(new BigDecimal("450.00"))
                    .stockQuantity(20)
                    .minimumStock(5)
                    .build());

            Product capo = productRepository.save(Product.builder()
                    .code("PROD003")
                    .name("Capô do Motor")
                    .description("Capô em aço estampado com tratamento anticorrosivo")
                    .unitValue(new BigDecimal("680.00"))
                    .stockQuantity(15)
                    .minimumStock(3)
                    .build());

            Product banco = productRepository.save(Product.builder()
                    .code("PROD004")
                    .name("Banco Dianteiro Motorista")
                    .description("Banco ergonômico com ajuste elétrico")
                    .unitValue(new BigDecimal("1250.00"))
                    .stockQuantity(12)
                    .minimumStock(2)
                    .build());

            Product painel = productRepository.save(Product.builder()
                    .code("PROD005")
                    .name("Painel de Instrumentos")
                    .description("Painel digital LCD com computador de bordo")
                    .unitValue(new BigDecimal("890.00"))
                    .stockQuantity(8)
                    .minimumStock(2)
                    .build());

            // Vincular Matérias-Primas aos Produtos
            
            // Retrovisor
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(retrovisor)
                    .rawMaterial(plastico)
                    .requiredQuantity(new BigDecimal("0.800"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(retrovisor)
                    .rawMaterial(vidro)
                    .requiredQuantity(new BigDecimal("0.150"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(retrovisor)
                    .rawMaterial(parafuso)
                    .requiredQuantity(new BigDecimal("3.000"))
                    .build());

            // Para-choque
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(parachoque)
                    .rawMaterial(plastico)
                    .requiredQuantity(new BigDecimal("4.500"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(parachoque)
                    .rawMaterial(tinta)
                    .requiredQuantity(new BigDecimal("0.300"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(parachoque)
                    .rawMaterial(parafuso)
                    .requiredQuantity(new BigDecimal("8.000"))
                    .build());

            // Capô
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(capo)
                    .rawMaterial(aco)
                    .requiredQuantity(new BigDecimal("12.000"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(capo)
                    .rawMaterial(tinta)
                    .requiredQuantity(new BigDecimal("0.500"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(capo)
                    .rawMaterial(borracha)
                    .requiredQuantity(new BigDecimal("0.200"))
                    .build());

            // Banco
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(banco)
                    .rawMaterial(espuma)
                    .requiredQuantity(new BigDecimal("3.500"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(banco)
                    .rawMaterial(aluminio)
                    .requiredQuantity(new BigDecimal("2.200"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(banco)
                    .rawMaterial(parafuso)
                    .requiredQuantity(new BigDecimal("12.000"))
                    .build());

            // Painel
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(painel)
                    .rawMaterial(plastico)
                    .requiredQuantity(new BigDecimal("1.800"))
                    .build());
            productRawMaterialRepository.save(ProductRawMaterial.builder()
                    .product(painel)
                    .rawMaterial(vidro)
                    .requiredQuantity(new BigDecimal("0.250"))
                    .build());

            log.info("✅ Database populated successfully!");
            log.info("   - {} raw materials", rawMaterialRepository.count());
            log.info("   - {} products", productRepository.count());
            log.info("   - {} product-material links", productRawMaterialRepository.count());
        };
    }
}