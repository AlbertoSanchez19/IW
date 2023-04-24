-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (1, TRUE,'ADMIN,USER', 'Admin', 'Administra',  'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (3, TRUE, 'USER,PROFESOR', 'Alberto', 'Apellido', 'albertinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (4, TRUE, 'USER,PROFESOR', 'Adrian', 'Apellido', 'adrinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (5, TRUE, 'USER,PROFESOR', 'Daniel', 'Apellido', 'danizinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (6, TRUE, 'USER,PROFESOR', 'Gonzalo', 'Apellido', 'gonzalinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (7, TRUE, 'USER,PROFESOR', 'David', 'Apellido', 'danielinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO "PUBLIC"."CLASES" VALUES
(1, '3D', 1);            
INSERT INTO "PUBLIC"."CUESTIONARIO" VALUES
(1, ' fsadfasdf', 'asdfasd', 1);  
ALTER TABLE "PUBLIC"."EVENTO" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_7A" PRIMARY KEY("ID");      
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.EVENTO;  
INSERT INTO "PUBLIC"."EVENTO" VALUES
(1, 'E9aiVtth', 1);     
INSERT INTO "PUBLIC"."PREGUNTA" VALUES
(1, 'asdf', 'asdfafsd', 'TRUE_FALSE', 1);             
INSERT INTO "PUBLIC"."RESPUESTA" VALUES
(1, 10.0, 'V', 1),
(2, 0.0, 'F', 1);


-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
