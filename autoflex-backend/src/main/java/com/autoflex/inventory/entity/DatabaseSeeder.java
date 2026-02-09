package com.autoflex.inventory.entity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Long count = (Long) em.createQuery("SELECT COUNT(p) FROM Product p").getSingleResult();
        
        if (count == 0) {
            // Inserir matérias-primas
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
            
            // Inserir relação produto-matéria prima
            em.createNativeQuery("""
                INSERT INTO product_raw_materials 
                    (product_id, raw_material_id, required_quantity) 
                VALUES
                    (1, 4, 0.800),
                    (1, 7, 0.150),
                    (1, 5, 3.000),
                    (2, 4, 4.500),
                    (2, 6, 0.300),
                    (2, 5, 8.000),
                    (3, 1, 12.000),
                    (3, 6, 0.500),
                    (3, 3, 0.200),
                    (4, 8, 3.500),
                    (4, 2, 2.200),
                    (4, 5, 12.000),
                    (5, 4, 1.800),
                    (5, 7, 0.250)
            """).executeUpdate();
            
            System.out.println("✅ Database seeded successfully!");
        } else {
            System.out.println("ℹ️ Database already has data. Skipping seed.");
        }
    }
}