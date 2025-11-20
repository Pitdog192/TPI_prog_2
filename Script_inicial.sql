CREATE DATABASE IF NOT EXISTS `Vehiculos_con_seguro`;
use Vehiculos_con_seguro;

-- -----------------------------------
-- Se crea la tabla vehiculos
CREATE TABLE vehiculos(
	id int primary key auto_increment, /* Aunque se tengan el dominio y nro_chasis que suelen ser únicos, por causas excepcionales podrían cambiar, por lo tanto se usa el id como identificador único por estabilidad y performance*/ 
    eliminado boolean,
    dominio varchar(10) unique not null,
    marca varchar(50) not null,
    modelo varchar(50) not null,
    anio int,
    nro_chasis varchar(50) unique,
    id_seguro int unique
);

-- Se crea la tabla seguro_vehicular
CREATE TABLE seguro_vehicular(
	id int primary key auto_increment ,
    eliminado boolean,
    aseguradora varchar(80) not null,
    nro_poliza varchar(50) unique not null,
    cobertura varchar(20) NOT NULL,
    vencimiento date not null,
    /* No se añade el id de un vehiculo al existir la posibilidad de crear un seguro tipo flota en un futuro donde se establesca una relación 1:N  */ 
    CONSTRAINT chk_seguros_cobertura CHECK (cobertura IN ('RC','TERCEROS','TODO_RIESGO'))
);

-- --------------------------------
-- Se añaden las FK
ALTER TABLE vehiculos 
	ADD CONSTRAINT fk_id_seguro 
    FOREIGN KEY (id_seguro) 
    REFERENCES seguro_vehicular(id)
    /* Se eliminó ON UPDATE CASCADE por sugerencia de IA ya que no aporta nada significativo al ser raro actualizar el id de un seguro */ 
	ON DELETE SET NULL;

ALTER TABLE vehiculos
    ADD CONSTRAINT chk_vehiculos_anio_rango
    CHECK (anio IS NULL OR (anio BETWEEN 1900 AND 2100));

-- Inserción de Seguros
INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'La Caja', 'POL-001', 'TODO_RIESGO', '2026-12-31');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Mapfre', 'POL-002', 'RC', '2025-05-20');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Allianz', 'POL-003', 'TERCEROS', '2024-11-15');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Sancor Seguros', 'POL-004', 'TODO_RIESGO', '2027-01-10');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Zurich', 'POL-005', 'RC', '2025-09-30');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Federación Patronal', 'POL-006', 'TERCEROS', '2026-03-05');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Provincia Seguros', 'POL-007', 'TODO_RIESGO', '2026-07-18');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Río Uruguay Seguros', 'POL-008', 'RC', '2025-12-01');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'La Segunda', 'POL-009', 'TERCEROS', '2024-08-22');

INSERT INTO seguro_vehicular (eliminado, aseguradora, nro_poliza, cobertura, vencimiento)
VALUES (false, 'Mercantil Andina', 'POL-010', 'TODO_RIESGO', '2027-04-11');

-- Inserción Vehiculos
INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AB110LG', 'FORD', 'MUSTANG', 2022, 'ABC123', 1);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AC200XY', 'CHEVROLET', 'CRUZE', 2020, 'CHS001', 2);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AD305BC', 'TOYOTA', 'COROLLA', 2019, 'CHS002', 3);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AE450DF', 'VOLKSWAGEN', 'GOLF', 2021, 'CHS003', 4);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AF512GH', 'RENAULT', 'SANDERO', 2018, 'CHS004', 5);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AG678JK', 'PEUGEOT', '208', 2022, 'CHS005', 6);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AH745LM', 'FIAT', 'CRONOS', 2021, 'CHS006', 7);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AI889NP', 'NISSAN', 'VERSA', 2020, 'CHS007', 8);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AJ932QR', 'HONDA', 'CIVIC', 2019, 'CHS008', 9);

INSERT INTO vehiculos (eliminado, dominio, marca, modelo, anio, nro_chasis, id_seguro)
VALUES (FALSE, 'AK010ST', 'BMW', '320i', 2023, 'CHS009', 10);