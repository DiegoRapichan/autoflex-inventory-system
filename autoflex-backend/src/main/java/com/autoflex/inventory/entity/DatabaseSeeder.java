package com.autoflex.inventory.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("prod")
@RequiredArgsConstructor
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    private static boolean hasRun = false; // Flag to ensure it runs only once per JVM

    @Override
    public void run(String... args) {
        if (hasRun) return; // Prevent double execution in the same JVM
        hasRun = true;

        // Check if products already exist
        Number productCount = (Number) em.createNativeQuery(
                "SELECT COUNT(*) FROM products"
        ).getSingleResult();

        if (productCount.longValue() > 0) {
            System.out.println("ℹ️ Products already exist. Seeder will not run again.");
            return;
        }

        System.out.println("🌱 Seeding database...");

        // Seed raw materials
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
            ON CONFLICT (code) DO NOTHING
        """).executeUpdate();

        em.flush();

        // Seed products
        em.createNativeQuery("""
            INSERT INTO products
                (code, name, value, created_at, updated_at)
            VALUES
                ('PROD001', 'Retrovisor Externo Direito', 285.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                ('PROD002', 'Para-choque Dianteiro', 450.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                ('PROD003', 'Capô do Motor', 680.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                ('PROD004', 'Banco Dianteiro Motorista', 1250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                ('PROD005', 'Painel de Instrumentos', 890.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT (code) DO NOTHING
        """).executeUpdate();

        em.flush();

        // Fetch product and raw material IDs safely
        List<Number> productIds = em.createNativeQuery("SELECT id FROM products ORDER BY code").getResultList();
        List<Object[]> rawMaterials = em.createNativeQuery("SELECT code, id FROM raw_materials").getResultList();

        Map<String, Long> rmMap = new HashMap<>();
        for (Object[] row : rawMaterials) {
            rmMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        // Map product IDs safely
        Long p1 = productIds.get(0).longValue();
        Long p2 = productIds.get(1).longValue();
        Long p3 = productIds.get(2).longValue();
        Long p4 = productIds.get(3).longValue();
        Long p5 = productIds.get(4).longValue();

        // Insert product_raw_materials
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
                p1, rmMap.get("MP004"),
                p1, rmMap.get("MP007"),
                p1, rmMap.get("MP005"),
                p2, rmMap.get("MP004"),
                p2, rmMap.get("MP006"),
                p2, rmMap.get("MP005"),
                p3, rmMap.get("MP001"),
                p3, rmMap.get("MP006"),
                p3, rmMap.get("MP003"),
                p4, rmMap.get("MP008"),
                p4, rmMap.get("MP002"),
                p4, rmMap.get("MP005"),
                p5, rmMap.get("MP004"),
                p5, rmMap.get("MP007")
        )).executeUpdate();

        System.out.println("✅ Database seeded successfully. Seeder will not run again.");
    }
}
