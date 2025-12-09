INSERT IGNORE INTO artists (name, genre, country)
VALUES ('Metallica', 'Thrash Metal', 'USA'),
       ('Rammstein', 'Industrial Metal', 'Germany'),
       ('Deftones', 'Alternative Metal', 'USA'),
       ('Behemoth', 'Blackened Death Metal', 'Poland'),
       ('Fit For An Autopsy', 'Deathcore', 'USA'),
       ('Lorna Shore', 'Deathcore', 'USA'),
       ('Gojira', 'Progressive Death Metal', 'France'),
       ('Opeth', 'Progressive Metal', 'Sweden'),
       ('Pantera', 'Groove Metal', 'USA'),
       ('Mastodon', 'Progressive Metal', 'USA'),
       ('Soen', 'Progressive Metal', 'Sweden'),
       ('Igorrr', 'Avant-garde Metal', 'France'),
       ('Loathe', 'Metalcore', 'UK'),
       ('Linkin Park', 'Nu Metal', 'USA'),
       ('Whitechapel', 'Deathcore', 'USA');

INSERT IGNORE INTO stages (name, capacity)
VALUES ('Main Stage of Hell', 50000),
       ('Purgatory Stage', 15000),
       ('The Abyss', 5000);

INSERT IGNORE INTO concerts (start_time, end_time, artist_id, stage_id)
VALUES ('2025-07-10 18:00:00', '2025-07-10 19:30:00', 13, 2),
       ('2025-07-10 19:00:00', '2025-07-10 20:30:00', 5, 3),
       ('2025-07-10 20:00:00', '2025-07-10 21:30:00', 11, 2),
       ('2025-07-10 21:00:00', '2025-07-10 23:00:00', 3, 1),
       ('2025-07-10 23:30:00', '2025-07-11 01:30:00', 1, 1),

       ('2025-07-11 17:00:00', '2025-07-11 18:30:00', 15, 3),
       ('2025-07-11 18:00:00', '2025-07-11 19:30:00', 12, 2),
       ('2025-07-11 19:30:00', '2025-07-11 21:00:00', 6, 2),
       ('2025-07-11 20:00:00', '2025-07-11 21:30:00', 4, 3),
       ('2025-07-11 21:00:00', '2025-07-11 23:00:00', 7, 1),
       ('2025-07-11 23:30:00', '2025-07-12 01:30:00', 2, 1),

       ('2025-07-12 16:00:00', '2025-07-12 17:30:00', 10, 2),
       ('2025-07-12 17:30:00', '2025-07-12 19:00:00', 8, 2),
       ('2025-07-12 19:00:00', '2025-07-12 20:30:00', 9, 1),
       ('2025-07-12 21:30:00', '2025-07-12 23:30:00', 14, 1),

       ('2025-07-10 16:00:00', '2025-07-10 17:00:00', 5, 3),
       ('2025-07-11 01:30:00', '2025-07-11 03:00:00', 12, 3),
       ('2025-07-12 14:00:00', '2025-07-12 15:30:00', 11, 2),
       ('2025-07-12 15:00:00', '2025-07-12 16:00:00', 6, 3),
       ('2025-07-10 22:00:00', '2025-07-10 23:30:00', 9, 2);

INSERT IGNORE INTO attendees (dni, firstName, lastName, phone, email)
VALUES ('12345678A', 'Juan', 'García', '600111222', 'juan.garcia@email.com'),
       ('87654321B', 'María', 'López', '600333444', 'maria.lopez@email.com'),
       ('11223344C', 'Carlos', 'Martínez', '600555666', 'carlos.mtz@email.com'),
       ('44332211D', 'Laura', 'Sánchez', '600777888', 'laura.sanchez@email.com'),
       ('99887766E', 'Pedro', 'Gómez', '600999000', 'pedro.gomez@email.com'),
       ('55667788F', 'Ana', 'Fernández', '611222333', 'ana.fernandez@email.com'),
       ('22334455G', 'David', 'Díaz', '622333444', 'david.diaz@email.com'),
       ('66778899H', 'Lucía', 'Pérez', '633444555', 'lucia.perez@email.com'),
       ('77889900J', 'Javier', 'Ruiz', '644555666', 'javier.ruiz@email.com'),
       ('00112233K', 'Elena', 'Jiménez', '655666777', 'elena.jimenez@email.com'),
       ('33445566L', 'Sergio', 'Moreno', '666777888', 'sergio.moreno@email.com'),
       ('99001122M', 'Carmen', 'Muñoz', '677888999', 'carmen.munoz@email.com'),
       ('55443322N', 'Antonio', 'Álvarez', '688999000', 'antonio.alvarez@email.com'),
       ('11002299P', 'Isabel', 'Romero', '699000111', 'isabel.romero@email.com'),
       ('22113300Q', 'Miguel', 'Navarro', '700111222', 'miguel.navarro@email.com');

INSERT IGNORE INTO tickets (price, type, is_used, attendee_id)
VALUES (150.00, 'GENERAL', 1, 1),
       (150.00, 'GENERAL', 1, 2),
       (250.00, 'VIP', 0, 3),
       (150.00, 'GENERAL', 1, 4),
       (150.00, 'GENERAL', 1, 5),
       (250.00, 'VIP', 1, 6),
       (150.00, 'GENERAL', 0, 7),
       (150.00, 'GENERAL', 1, 8),
       (300.00, 'VIP', 0, 9),
       (150.00, 'GENERAL', 1, 10),
       (150.00, 'GENERAL', 0, 11),
       (150.00, 'GENERAL', 1, 12),
       (250.00, 'VIP', 1, 13),
       (150.00, 'GENERAL', 0, 14),
       (150.00, 'GENERAL', 1, 15),
       (150.00, 'GENERAL', 0, 1),
       (250.00, 'VIP', 1, 3),
       (150.00, 'GENERAL', 1, 5),
       (150.00, 'GENERAL', 0, 9),
       (250.00, 'VIP', 0, 15);