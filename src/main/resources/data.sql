-- 1. ARTISTAS
INSERT IGNORE INTO artists (name, genre, country)
VALUES ('Gojira', 'Progressive Death Metal', 'France'),       -- ID 1
       ('Loathe', 'Metalcore', 'UK'),                         -- ID 2
       ('Rammstein', 'Industrial Metal', 'Germany'),          -- ID 3
       ('Faetooth', 'Doom Metal', 'USA'),                     -- ID 4
       ('Deftones', 'Alternative Metal', 'USA'),              -- ID 5
       ('Behemoth', 'Blackened Death Metal', 'Poland'),       -- ID 6
       ('Fit For An Autopsy', 'Deathcore', 'USA'),            -- ID 7
       ('Lorna Shore', 'Deathcore', 'USA'),                   -- ID 8
       ('Opeth', 'Progressive Metal', 'Sweden'),              -- ID 9
       ('Jinjer', 'Progressive Metal', 'Ukraine'),            -- ID 10
       ('Metallica', 'Thrash Metal', 'USA'),                  -- ID 11
       ('Mastodon', 'Progressive Metal', 'USA'),              -- ID 12
       ('Evanescence', 'Nu Metal', 'USA'),                    -- ID 13
       ('Tool', 'Progressive Metal', 'USA'),                  -- ID 14
       ('Whitechapel', 'Deathcore', 'USA');                   -- ID 15

-- 2. ESCENARIOS
INSERT IGNORE INTO stages (name, capacity)
VALUES ('Main Stage of Hell', 50000),  -- ID 1
       ('Purgatory Stage', 15000),     -- ID 2
       ('The Abyss', 5000);            -- ID 3

-- 3. CONCIERTOS

-- DÍA 1 (10 Julio): Rammstein cierra. Gojira sub-cabeza.
INSERT IGNORE INTO concerts (start_time, end_time, artist_id, stage_id)
VALUES
       -- Escenario Pequeño (The Abyss)
       ('2025-07-10 17:00:00', '2025-07-10 18:00:00', 2, 3),  -- Loathe
       ('2025-07-10 18:30:00', '2025-07-10 19:30:00', 7, 3),  -- Fit For An Autopsy

       -- Escenario Mediano (Purgatory)
       ('2025-07-10 19:00:00', '2025-07-10 20:30:00', 13, 2), -- Evanescence
       ('2025-07-10 21:00:00', '2025-07-10 22:30:00', 10, 2), -- Jinjer

       -- Escenario Principal (Main)
       ('2025-07-10 20:30:00', '2025-07-10 22:30:00', 1, 1),  -- Gojira (Telonero de lujo)
       ('2025-07-10 23:00:00', '2025-07-11 01:00:00', 3, 1);  -- Rammstein (Cierre)

-- DÍA 2 (11 Julio): Metallica cierra. Deftones sub-cabeza.
INSERT IGNORE INTO concerts (start_time, end_time, artist_id, stage_id)
VALUES
       -- Escenario Pequeño (The Abyss)
       ('2025-07-11 17:00:00', '2025-07-11 18:00:00', 4, 3),  -- Faetooth
       ('2025-07-11 18:30:00', '2025-07-11 19:30:00', 15, 3), -- Whitechapel

       -- Escenario Mediano (Purgatory)
       ('2025-07-11 19:30:00', '2025-07-11 21:00:00', 12, 2), -- Mastodon
       ('2025-07-11 21:30:00', '2025-07-11 23:00:00', 6, 2),  -- Behemoth

       -- Escenario Principal (Main)
       ('2025-07-11 20:30:00', '2025-07-11 22:30:00', 5, 1),  -- Deftones
       ('2025-07-11 23:00:00', '2025-07-12 01:30:00', 11, 1); -- Metallica

-- DÍA 3 (12 Julio): Tool cierra. Opeth sub-cabeza.
INSERT IGNORE INTO concerts (start_time, end_time, artist_id, stage_id)
VALUES
       -- Escenario Pequeño/Mediano (Purgatory día más fuerte)
       ('2025-07-12 18:00:00', '2025-07-12 19:30:00', 8, 2),  -- Lorna Shore (Purgatory)

       -- Escenario Principal (Main)
       ('2025-07-12 20:00:00', '2025-07-12 22:00:00', 9, 1),  -- Opeth
       ('2025-07-12 22:30:00', '2025-07-13 00:30:00', 14, 1); -- Tool


-- 4. ASISTENTES
INSERT IGNORE INTO attendees (dni, name, phone, email)
VALUES ('12345678A', 'Juan García', '600111222', 'juan.garcia@email.com'),
       ('87654321B', 'María López', '600333444', 'maria.lopez@email.com'),
       ('11223344C', 'Carlos Martínez', '600555666', 'carlos.mtz@email.com'),
       ('44332211D', 'Laura Sánchez', '600777888', 'laura.sanchez@email.com'),
       ('99887766E', 'Pedro Gómez', '600999000', 'pedro.gomez@email.com'),
       ('55667788F', 'Ana Fernández', '611222333', 'ana.fernandez@email.com'),
       ('22334455G', 'David Díaz', '622333444', 'david.diaz@email.com'),
       ('66778899H', 'Lucía Pérez', '633444555', 'lucia.perez@email.com'),
       ('77889900J', 'Javier Ruiz', '644555666', 'javier.ruiz@email.com'),
       ('00112233K', 'Elena Jiménez', '655666777', 'elena.jimenez@email.com'),
       ('33445566L', 'Sergio Moreno', '666777888', 'sergio.moreno@email.com'),
       ('99001122M', 'Carmen Muñoz', '677888999', 'carmen.munoz@email.com'),
       ('55443322N', 'Antonio Álvarez', '688999000', 'antonio.alvarez@email.com'),
       ('11002299P', 'Isabel Romero', '699000111', 'isabel.romero@email.com'),
       ('22113300Q', 'Miguel Navarro', '700111222', 'miguel.navarro@email.com');

-- 5. TICKETS
INSERT IGNORE INTO tickets (price, type, is_used, attendee_id)
VALUES (150.00, 'GENERAL', 1, 1),
       (150.00, 'GENERAL', 1, 2),
       (250.00, 'VIP', 0, 3),
       (150.00, 'GENERAL', 1, 4),
       (150.00, 'GENERAL', 1, 5),
       (250.00, 'VIP', 1, 6),
       (150.00, 'GENERAL', 0, 7),
       (150.00, 'GENERAL', 1, 8),
       (250.00, 'VIP', 0, 9),
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