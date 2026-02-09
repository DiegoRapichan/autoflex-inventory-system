package com.autoflex.inventory.entity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Long count = (Long) em.createQuery("SELECT COUNT(p) FROM Product p").getSingleResult();
        
        if (count == 0) {
            System.out.println("🧹 Limpando dados antigos...");
            
            // Limpar dados na ordem correta (respeitar foreign keys)
            em.createNativeQuery("DELETE FROM product_raw_materials").executeUpdate();
            em.createNativeQuery("DELETE FROM products").executeUpdate();
            em.createNativeQuery("DELETE FROM raw_materials").executeUpdate();
            
            // Resetar as sequences (opcional, mas recomendado)
            em.createNativeQuery("ALTER SEQUENCE products_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE raw_materials_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE product_raw_materials_id_seq RESTART WITH 1").executeUpdate();
            
            System.out.println("✅ Dados antigos removidos!");
            
            // Inserir matérias-primas
            System.out.println("📦 Inserindo matérias-primas...");
            em.createNativeQuery("""
                INSERT INTO raw_materials 
                    (code, name, unit, stock_quantity, minimum_stock, unit_cost, created_at, updated_at) 
                VALUES
                    ('MP001', 'Aço Inoxidável', 'kg', 500.00, 100.00, 25.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP002', 'Alumínio 6061', 'kg', 300.00, 50.00, 18.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP003', 'Borracha EPDM', 'kg', 150.00, 30.00, 12.30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP004', 'Plástico ABS', 'kg', 200.00, 40.00, 8.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP005', 'Parafuso M8', 'unidade', 5000.00, 1000.00, 0.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP006', 'Tinta Automotiva Preta', 'litro', 80.00, 20.00, 45.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP007', 'Vidro Temperado', 'm²', 50.00, 10.00, 85.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('MP008', 'Espuma Poliuretano', 'kg', 100.00, 25.00, 15.60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """).executeUpdate();
            
            // Inserir produtos
            System.out.println("🚗 Inserindo produtos...");
            em.createNativeQuery("""
                INSERT INTO products 
                    (code, name, value, created_at, updated_at) 
                VALUES
                    ('PROD001', 'Retrovisor Externo Direito', 285.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('PROD002', 'Para-choque Dianteiro', 450.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('PROD003', 'Capô do Motor', 680.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('PROD004', 'Banco Dianteiro Motorista', 1250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                    ('PROD005', 'Painel de Instrumentos', 890.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """).executeUpdate();
            
            // Buscar os IDs reais dos produtos inseridos
            @SuppressWarnings("unchecked")
            List<BigInteger> productIds = em.createNativeQuery(
                "SELECT id FROM products WHERE code IN ('PROD001', 'PROD002', 'PROD003', 'PROD004', 'PROD005') ORDER BY code"
            ).getResultList();
            
            // Buscar os IDs das matérias-primas
            @SuppressWarnings("unchecked")
            List<Object[]> rawMaterialIds = em.createNativeQuery(
                "SELECT code, id FROM raw_materials WHERE code IN ('MP001', 'MP002', 'MP003', 'MP004', 'MP005', 'MP006', 'MP007', 'MP008') ORDER BY code"
            ).getResultList();
            
            // Criar mapa de códigos para IDs
            java.util.Map<String, Long> rmMap = new java.util.HashMap<>();
            for (Object[] row : rawMaterialIds) {
                rmMap.put((String) row[0], ((BigInteger) row[1]).longValue());
            }
            
            Long prod1 = productIds.get(0).longValue();
            Long prod2 = productIds.get(1).longValue();
            Long prod3 = productIds.get(2).longValue();
            Long prod4 = productIds.get(3).longValue();
            Long prod5 = productIds.get(4).longValue();
            
            // Inserir relações usando os IDs reais
            System.out.println("🔗 Criando relações produto-matéria prima...");
            em.createNativeQuery(String.format("""
                INSERT INTO product_raw_materials 
                    (product_id, raw_material_id, required_quantity) 
                VALUES
                    (%d, %d, 0.800),
                    (%d, %d, 0.150),
                    (%d, %d, 3.000),
                    (%d, %d, 4.500),
                    (%d, %d, 0.300),
                    (%d, %d, 8.000),
                    (%d, %d, 12.000),
                    (%d, %d, 0.500),
                    (%d, %d, 0.200),
                    (%d, %d, 3.500),
                    (%d, %d, 2.200),
                    (%d, %d, 12.000),
                    (%d, %d, 1.800),
                    (%d, %d, 0.250)
                """,
                prod1, rmMap.get("MP004"),
                prod1, rmMap.get("MP007"),
                prod1, rmMap.get("MP005"),
                prod2, rmMap.get("MP004"),
                prod2, rmMap.get("MP006"),
                prod2, rmMap.get("MP005"),
                prod3, rmMap.get("MP001"),
                prod3, rmMap.get("MP006"),
                prod3, rmMap.get("MP003"),
                prod4, rmMap.get("MP008"),
                prod4, rmMap.get("MP002"),
                prod4, rmMap.get("MP005"),
                prod5, rmMap.get("MP004"),
                prod5, rmMap.get("MP007")
            )).executeUpdate();
            
            System.out.println("✅ Database seeded successfully!");
            System.out.println("   - 8 matérias-primas inseridas");
            System.out.println("   - 5 produtos inseridos");
            System.out.println("   - 14 relações criadas");
        } else {
            System.out.println("ℹ️ Database already has data. Skipping seed.");
        }
    }
}