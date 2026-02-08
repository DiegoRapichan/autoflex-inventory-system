-- Limpar dados existentes (cuidado em produção!)
-- DELETE FROM product_raw_materials;
-- DELETE FROM products;
-- DELETE FROM raw_materials;

-- Reset IDs (PostgreSQL)
-- ALTER SEQUENCE products_id_seq RESTART WITH 1;
-- ALTER SEQUENCE raw_materials_id_seq RESTART WITH 1;
-- ALTER SEQUENCE product_raw_materials_id_seq RESTART WITH 1;

-- Inserir Matérias-Primas
INSERT INTO raw_materials (code, name, unit, stock_quantity, minimum_stock, unit_cost) VALUES
('MP001', 'Aço Inoxidável', 'kg', 500.00, 100.00, 25.50),
('MP002', 'Alumínio 6061', 'kg', 300.00, 50.00, 18.75),
('MP003', 'Borracha EPDM', 'kg', 150.00, 30.00, 12.30),
('MP004', 'Plástico ABS', 'kg', 200.00, 40.00, 8.90),
('MP005', 'Parafuso M8', 'unidade', 5000.00, 1000.00, 0.25),
('MP006', 'Tinta Automotiva Preta', 'litro', 80.00, 20.00, 45.00),
('MP007', 'Vidro Temperado', 'm²', 50.00, 10.00, 85.00),
('MP008', 'Espuma Poliuretano', 'kg', 100.00, 25.00, 15.60);

-- Inserir Produtos
INSERT INTO products (code, name, description, unit_value, stock_quantity, minimum_stock) VALUES
('PROD001', 'Retrovisor Externo Direito', 'Retrovisor externo elétrico com desembaçador', 285.00, 45, 10),
('PROD002', 'Para-choque Dianteiro', 'Para-choque em plástico ABS reforçado', 450.00, 20, 5),
('PROD003', 'Capô do Motor', 'Capô em aço estampado com tratamento anticorrosivo', 680.00, 15, 3),
('PROD004', 'Banco Dianteiro Motorista', 'Banco ergonômico com ajuste elétrico', 1250.00, 12, 2),
('PROD005', 'Painel de Instrumentos', 'Painel digital LCD com computador de bordo', 890.00, 8, 2);

-- Vincular Matérias-Primas aos Produtos

-- PROD001: Retrovisor (Plástico + Vidro + Parafusos)
INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
(1, 4, 0.800),  -- 0.8 kg de Plástico ABS
(1, 7, 0.150),  -- 0.15 m² de Vidro
(1, 5, 3.000);  -- 3 Parafusos

-- PROD002: Para-choque (Plástico + Tinta + Parafusos)
INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
(2, 4, 4.500),  -- 4.5 kg de Plástico ABS
(2, 6, 0.300),  -- 0.3 L de Tinta
(2, 5, 8.000);  -- 8 Parafusos

-- PROD003: Capô (Aço + Tinta + Borracha)
INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
(3, 1, 12.000), -- 12 kg de Aço
(3, 6, 0.500),  -- 0.5 L de Tinta
(3, 3, 0.200);  -- 0.2 kg de Borracha

-- PROD004: Banco (Espuma + Alumínio + Parafusos)
INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
(4, 8, 3.500),  -- 3.5 kg de Espuma
(4, 2, 2.200),  -- 2.2 kg de Alumínio
(4, 5, 12.000); -- 12 Parafusos

-- PROD005: Painel (Plástico + Vidro)
INSERT INTO product_raw_materials (product_id, raw_material_id, required_quantity) VALUES
(5, 4, 1.800),  -- 1.8 kg de Plástico
(5, 7, 0.250);  -- 0.25 m² de Vidro