-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (1, TRUE,'ADMIN,USER', 'Pedrito', 'Picapiedra',  'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles,  first_name, last_name,username, password)
VALUES (2, TRUE, 'USER,PROFESOR', 'Manolito', 'Perez', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles,  first_name, last_name,username, password)
VALUES (3, TRUE, 'USER', 'Juliancito', 'Rodriguez', 'c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (4, TRUE, 'USER,PROFESOR', 'Alberto', 'Apellido', 'albertinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (5, TRUE, 'USER,PROFESOR', 'Adrian', 'Apellido', 'adrinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (6, TRUE, 'USER,PROFESOR', 'Daniel', 'Apellido', 'danizinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (7, TRUE, 'USER', 'Gonzalo', 'Apellido', 'gonzalinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, first_name, last_name, username, password)
VALUES (8, TRUE, 'USER', 'David', 'Apellido', 'danielinho',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');



-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
