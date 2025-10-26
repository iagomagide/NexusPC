
DROP DATABASE IF EXISTS nexuspc;

CREATE DATABASE nexuspc;

USE nexuspc;

CREATE TABLE fabricantes (
     id int unsigned auto_increment PRIMARY KEY,
     nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE categorias (
    id int unsigned auto_increment PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE productos (
    id int unsigned auto_increment PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    pvp decimal(10, 2) NOT NULL DEFAULT 0, -- Precio de Venta
    stock int NOT NULL DEFAULT 0,
    id_categoria int UNSIGNED,
    id_fabricante int UNSIGNED,
    -- si borramos un fabricante, en el producto se queda a nulo
    FOREIGN KEY (id_fabricante) REFERENCES fabricantes(id)
       ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pedidos (
    id int unsigned auto_increment PRIMARY KEY,
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaEntrega date NOT NULL,
    pvp DECIMAL(10, 2) NOT NULL DEFAULT 0,
    direccion VARCHAR(200) NOT NULL,
    cliente_nombre VARCHAR(100) NOT NULL
);

CREATE TABLE lineas_pedidos (
    id_pedido int unsigned,
    id_linea int unsigned,
    id_producto int unsigned NOT NULL,
    cantidad int unsigned NOT NULL DEFAULT 0,
    pvp_linea DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_pedido,id_linea),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);
-- Procedimiento para numeración de las lineas
DELIMITER $$

CREATE TRIGGER before_insert_lineas_pedidos
BEFORE INSERT ON lineas_pedidos
FOR EACH ROW
BEGIN
    DECLARE max_linea INT;

    -- Buscar el número de línea máximo del pedido actual
    SELECT COALESCE(MAX(id_linea), 0) + 1
    INTO max_linea
    FROM lineas_pedidos
    WHERE id_pedido = NEW.id_pedido;

    -- Asignar automáticamente el siguiente número de línea
    SET NEW.id_linea = max_linea;
END;
$$

DELIMITER ;
-- Procedimiento almacenado para descontar stock
DELIMITER $$
CREATE PROCEDURE DescontarStock(IN product_id INT, IN quantity INT)
BEGIN
    UPDATE productos
    SET stock = stock - quantity
    WHERE id = product_id AND stock >= quantity;

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente o producto no encontrado';
    END IF;
END$$
DELIMITER ;

-- Concedemos persmisos al usuario
GRANT ALL PRIVILEGES ON alumnos.* TO 'testuser'@'%';
--Añadimos los datos
INSERT INTO fabricantes (id, nombre) VALUES
(1, 'Apple'),
(2, 'Samsung'),
(3, 'Dell'),
(4, 'Lenovo'),
(5, 'Asus'),
(6, 'HP'),
(7, 'Microsoft'),
(8, 'Xiaomi'),
(9, 'Oppo'),
(10, 'Google'),
(11, 'Acer'),
(12, 'OnePlus'),
(13, 'LG'),
(14, 'Huawei'),
(15, 'Nokia'),
(16, 'Sony'),
(17, 'Garmin'),
(18, 'Fitbit'),
(19, 'JBL'),
(20, 'Bose');

INSERT INTO categorias (id, nombre) VALUES
(1, 'Portátiles'),
(2, 'Smartphones'),
(3, 'Tablets'),
(4, 'Accesorios');

INSERT INTO productos (id, nombre_producto, descripcion, pvp, stock, id_categoria, id_fabricante) VALUES
(1, 'MacBook Pro 14"', 'Portátil Apple con chip M1 Pro', 1999.99, 10, 1, 1),
(2, 'iPhone 13', 'Smartphone Apple con pantalla OLED', 799.99, 50, 2, 1),
(3, 'Galaxy S21', 'Smartphone Samsung con cámara avanzada', 699.99, 40, 2, 2),
(4, 'Dell XPS 13', 'Portátil Dell ultraligero', 999.99, 15, 1, 3),
(5, 'ThinkPad X1', 'Portátil Lenovo de alto rendimiento', 1299.99, 12, 1, 4),
(6, 'Galaxy Tab S7', 'Tablet Samsung con pantalla de 11 pulgadas', 649.99, 25, 3, 2),
(7, 'iPad Air', 'Tablet Apple con chip A14', 599.99, 30, 3, 1),
(8, 'Asus ROG Phone 5', 'Smartphone Asus para gaming', 999.99, 20, 2, 5),
(9, 'HP Spectre x360', 'Portátil HP convertible de alta gama', 1199.99, 8, 1, 6),
(10, 'Surface Pro 7', 'Tablet Microsoft con teclado desmontable', 749.99, 18, 3, 7),
(11, 'MacBook Air 13"', 'Portátil ligero Apple con chip M1', 1299.99, 40, 1, 1),
(12, 'iPhone 12 Pro Max', 'Smartphone Apple con cámara triple', 1099.99, 25, 2, 1),
(13, 'Samsung Galaxy Note 20', 'Smartphone Samsung con S Pen', 999.99, 22, 2, 2),
(14, 'Lenovo IdeaPad 3', 'Portátil Lenovo para estudiantes', 499.99, 50, 1, 4),
(15, 'Xiaomi Mi 11', 'Smartphone Xiaomi con Snapdragon 888', 749.99, 35, 2, 8),
(16, 'Microsoft Surface Laptop 4', 'Portátil Microsoft con pantalla táctil', 1399.99, 10, 1, 7),
(17, 'Oppo Find X3 Pro', 'Smartphone Oppo con cámara quad', 899.99, 18, 2, 9),
(18, 'Samsung Galaxy Tab A7', 'Tablet Samsung económica de 10.4"', 199.99, 100, 3, 2),
(19, 'Google Pixel 5', 'Smartphone Google con cámara avanzada', 699.99, 30, 2, 10),
(20, 'Acer Aspire 5', 'Portátil Acer con pantalla Full HD', 549.99, 45, 1, 11),
(21, 'MacBook Pro 16"', 'Portátil Apple de 16 pulgadas con M1 Max', 2499.99, 5, 1, 1),
(22, 'Xiaomi Mi 10T Pro', 'Smartphone Xiaomi con pantalla 144Hz', 549.99, 70, 2, 8),
(23, 'OnePlus 9 Pro', 'Smartphone OnePlus con carga rápida', 1069.99, 25, 2, 12),
(24, 'iPad Pro 12.9"', 'Tablet Apple con pantalla mini-LED', 1099.99, 12, 3, 1),
(25, 'Samsung Galaxy Z Fold 3', 'Smartphone plegable Samsung con 5G', 1799.99, 8, 2, 2),
(26, 'LG Velvet', 'Smartphone LG con pantalla OLED', 599.99, 15, 2, 13),
(27, 'Asus ZenBook 14', 'Portátil Asus con pantalla NanoEdge', 899.99, 20, 1, 5),
(28, 'Huawei MatePad Pro', 'Tablet Huawei con pantalla 10.8"', 549.99, 30, 3, 14),
(29, 'Microsoft Surface Go 2', 'Tablet Microsoft con pantalla táctil de 10"', 399.99, 50, 3, 7),
(30, 'HP Envy 13', 'Portátil HP de alta gama con Intel Core i7', 1099.99, 8, 1, 6),
(31, 'Xiaomi Redmi Note 10 Pro', 'Smartphone Xiaomi con cámara 108 MP', 279.99, 120, 2, 8),
(32, 'Nokia 8.3 5G', 'Smartphone Nokia con cámara Zeiss', 599.99, 10, 2, 15),
(33, 'Google Pixel 4a', 'Smartphone Google con pantalla OLED', 349.99, 50, 2, 10),
(34, 'Lenovo Legion 5', 'Portátil Lenovo para gaming con NVIDIA GTX', 1399.99, 10, 1, 4),
(35, 'Samsung Galaxy A52', 'Smartphone Samsung con cámara quad', 349.99, 60, 2, 2),
(36, 'iPad Mini 5', 'Tablet Apple compacta con pantalla de 7.9"', 399.99, 25, 3, 1),
(37, 'Huawei P40 Pro', 'Smartphone Huawei con cámara Leica', 899.99, 20, 2, 14),
(38, 'Acer Predator Helios 300', 'Portátil Acer para gaming con RTX 3060', 1599.99, 7, 1, 11),
(39, 'Asus VivoBook 15', 'Portátil Asus con procesador Intel Core i5', 499.99, 60, 1, 5),
(40, 'Sony Xperia 1 II', 'Smartphone Sony con cámara triple Zeiss', 1199.99, 15, 2, 16),
(41, 'MacBook Pro 13"', 'Portátil Apple con pantalla Retina y M1', 1499.99, 18, 1, 1),
(42, 'Huawei Mate 40 Pro', 'Smartphone Huawei con Kirin 9000', 1099.99, 30, 2, 14),
(43, 'LG G8X ThinQ', 'Smartphone LG con pantalla dual', 699.99, 20, 2, 13),
(44, 'Lenovo ThinkPad T14', 'Portátil Lenovo con teclado retroiluminado', 949.99, 35, 1, 4),
(45, 'Oppo Reno 4 Pro', 'Smartphone Oppo con carga de 65W', 499.99, 50, 2, 9),
(46, 'Acer Swift 3', 'Portátil Acer ultraligero con procesador AMD', 799.99, 40, 1, 11),
(47, 'Xiaomi Redmi Note 9', 'Smartphone Xiaomi con pantalla de 6.53"', 199.99, 80, 2, 8),
(48, 'HP Omen 15', 'Portátil HP para gaming con RTX 2060', 1399.99, 10, 1, 6),
(49, 'iPhone SE 2020', 'Smartphone Apple compacto con A13 Bionic', 399.99, 70, 2, 1),
(50, 'Samsung Galaxy Watch 3', 'Reloj inteligente Samsung con ECG', 399.99, 25, 4, 2),
(51, 'Apple Watch Series 6', 'Reloj inteligente Apple con pantalla siempre encendida', 399.99, 30, 4, 1),
(52, 'Garmin Fenix 6 Pro', 'Reloj inteligente Garmin con GPS', 549.99, 10, 4, 17),
(53, 'Fitbit Charge 4', 'Pulsera de actividad Fitbit con monitor de ritmo cardíaco', 129.99, 40, 4, 18),
(54, 'Samsung Galaxy Buds Live', 'Auriculares inalámbricos Samsung con cancelación activa de ruido', 169.99, 50, 4, 2),
(55, 'AirPods Pro', 'Auriculares inalámbricos Apple con cancelación de ruido', 249.99, 30, 4, 1),
(56, 'Sony WH-1000XM4', 'Auriculares Sony con cancelación de ruido', 349.99, 20, 4, 16),
(57, 'JBL Flip 5', 'Altavoz Bluetooth JBL con sonido estéreo', 119.99, 60, 4, 19),
(58, 'Bose SoundLink Revolve', 'Altavoz Bluetooth Bose con 360 grados de sonido', 199.99, 30, 4, 20),
(59, 'Beats Studio3', 'Auriculares Beats con cancelación de ruido', 299.99, 10, 4, 1);


INSERT INTO pedidos (fecha_pedido, fechaEntrega, pvp, direccion, cliente_nombre) VALUES
('2024-01-15 10:30:00', '2024-01-20', 2799.98, 'Calle Mayor 123, Madrid', 'Juan Pérez García'),
('2024-01-16 14:20:00', '2024-01-22', 1699.98, 'Avenida Libertad 45, Barcelona', 'María López Sánchez'),
('2024-01-17 09:15:00', '2024-01-25', 899.99, 'Plaza España 67, Valencia', 'Carlos Rodríguez Martín'),
('2024-01-18 16:45:00', '2024-01-23', 3249.95, 'Gran Vía 88, Sevilla', 'Ana Martínez Ruiz'),
('2024-01-19 11:20:00', '2024-01-26', 1899.97, 'Calle Alcalá 234, Madrid', 'David González Castro'),
('2024-01-20 13:10:00', '2024-01-28', 2749.94, 'Paseo de Gracia 156, Barcelona', 'Laura Fernández Díaz');

INSERT INTO lineas_pedidos (id_pedido, id_producto, cantidad, pvp_linea) VALUES
-- Pedido 1 (2 productos)
(1, 1, 1, 1999.99),   -- MacBook Pro 14"
(1, 2, 1, 799.99),    -- iPhone 13

-- Pedido 2 (2 productos)
(2, 5, 1, 1299.99),   -- ThinkPad X1
(2, 19, 1, 399.99),   -- Google Pixel 4a

-- Pedido 3 (1 producto)
(3, 11, 1, 1299.99),  -- MacBook Air 13"

-- Pedido 4 (5 productos - pedido grande)
(4, 21, 1, 2499.99),  -- MacBook Pro 16"
(4, 24, 1, 1099.99),  -- iPad Pro 12.9"
(4, 55, 1, 249.99),   -- AirPods Pro
(4, 51, 1, 399.99),   -- Apple Watch Series 6
(4, 56, 1, 349.99),   -- Sony WH-1000XM4

-- Pedido 5 (3 productos)
(5, 34, 1, 1399.99),  -- Lenovo Legion 5
(5, 8, 1, 399.99),    -- Asus ROG Phone 5 (con descuento)
(5, 57, 1, 119.99),   -- JBL Flip 5

-- Pedido 6 (4 productos)
(6, 16, 1, 1399.99),  -- Microsoft Surface Laptop 4
(6, 25, 1, 1799.99),  -- Samsung Galaxy Z Fold 3
(6, 54, 1, 169.99),   -- Samsung Galaxy Buds Live
(6, 50, 1, 399.99);   -- Samsung Galaxy Watch 3

-- Actualizar los totales de los pedidos según las líneas
UPDATE pedidos SET pvp = 2799.98 WHERE id = 1;
UPDATE pedidos SET pvp = 1699.98 WHERE id = 2;
UPDATE pedidos SET pvp = 1299.99 WHERE id = 3;
UPDATE pedidos SET pvp = 4599.95 WHERE id = 4;  -- Actualizado por las 5 líneas
UPDATE pedidos SET pvp = 1919.97 WHERE id = 5;  -- Actualizado por las 3 líneas
UPDATE pedidos SET pvp = 3769.96 WHERE id = 6;  -- Actualizado por las 4 líneas

