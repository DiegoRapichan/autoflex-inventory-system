package com.autoflex.inventory.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Profile("prod")
@RequiredArgsConstructor
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public void run(String... args) {

        // Check if products already exist
        Long productCount = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM products")
                .getSingleResult()).longValue();
        if (productCount > 0) {
            System.out.println("ℹ️ Products already exist. Skipping seed.");
            return;
        }

        System.out.println("🌱 Seeding database...");

        // Insert raw materials
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

        // Insert products
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

        // Get inserted product IDs
        List<BigDecimal> productIds = em.createNativeQuery("""
            SELECT id FROM products ORDER BY code
        """).getResultList();

        // Map raw material codes to IDs
        List<Object[]> rawMaterials = em.createNativeQuery("""
            SELECT code, id FROM raw_materials
        """).getResultList();

        Map<String, Long> rmMap = new HashMap<>();
        for (Object[] row : rawMaterials) {
            rmMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        // Ensure all raw material IDs exist
        for (String code : List.of("MP001","MP002","MP003","MP004","MP005","MP006","MP007","MP008")) {
            Objects.requireNonNull(rmMap.get(code), "Raw material " + code + " not found in DB!");
        }

        Long p1 = productIds.get(0).longValue();
        Long p2 = productIds.get(1).longValue();
        Long p3 = productIds.get(2).longValue();
        Long p4 = productIds.get(3).longValue();
        Long p5 = productIds.get(4).longValue();

        // Insert product-raw_material relations (idempotent)
        em.createNativeQuery("""
            INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity)
            VALUES
                (:p1, :mp4, 0.8),
                (:p1, :mp7, 0.15),
                (:p1, :mp5, 3.0),
                (:p2, :mp4, 4.5),
                (:p2, :mp6, 0.3),
                (:p2, :mp5, 8.0),
                (:p3, :mp1, 12.0),
                (:p3, :mp6, 0.5),
                (:p3, :mp3, 0.2),
                (:p4, :mp8, 3.5),
                (:p4, :mp2, 2.2),
                (:p4, :mp5, 12.0),
                (:p5, :mp4, 1.8),
                (:p5, :mp7, 0.25)
            ON CONFLICT (product_id, raw_material_id) DO NOTHING
        """)
        .setParameter("p1", p1)
        .setParameter("p2", p2)
        .setParameter("p3", p3)
        .setParameter("p4", p4)
        .setParameter("p5", p5)
        .setParameter("mp1", rmMap.get("MP001"))
        .setParameter("mp2", rmMap.get("MP002"))
        .setParameter("mp3", rmMap.get("MP003"))
        .setParameter("mp4", rmMap.get("MP004"))
        .setParameter("mp5", rmMap.get("MP005"))
        .setParameter("mp6", rmMap.get("MP006"))
        .setParameter("mp7", rmMap.get("MP007"))
        .setParameter("mp8", rmMap.get("MP008"))
        .executeUpdate();

        System.out.println("✅ Database seeded successfully.");
    }
}
