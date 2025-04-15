INSERT INTO gender (id, name) VALUES (1, 'Masculino');
INSERT INTO gender (id, name) VALUES (2, 'Femenino');
INSERT INTO marital_status (id, name) VALUES (1,'Casado');
INSERT INTO marital_status (id, name) VALUES (2,'Viudo');
INSERT INTO marital_status (id, name) VALUES (3,'Divorciado');
INSERT INTO marital_status (id, name) VALUES (4,'Soltero');
INSERT INTO contract_type (id, name) VALUES (1,'Fijo');
INSERT INTO contract_type (id, name) VALUES (2,'Indefinido');
INSERT INTO contract_type (id, name) VALUES (3,'Obra');
INSERT INTO country (id, name, nationality) VALUES (1, 'Chile', 'Chilena');
INSERT INTO region (id, country_id, name) VALUES (1, 1,'RM');
INSERT INTO city (id, region_id, name) VALUES (1, 1,'Santiago');
INSERT INTO commune (id, city_id, name, sii_code, tgr_code) VALUES (1, 1, 'El Bosque', NULL, NULL);


--Usar employee cuando haya branch,company y jobScheduler listo
INSERT INTO employee (UUID,branch_id,job_scheduler_id,commune_id,nationality_id,gender_id,marital_status_id,contract_type_id,job_type_id,code,
    name,last_name,mother_last_name,birth_date,phone,email,address,contract_date,contract_end_date,active
) VALUES (
    '5898d6cf-64d5-4624-b9e4-80f6c110685a',4,1,1,1,1,1,NULL,4,'20975750-8',
    'John', 'Doe',NULL, '1980-01-01', '1234567890','john.doe@example.com','123 Main St','2020-01-01','2025-01-01', TRUE
);