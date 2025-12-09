-- Tabla Artists
CREATE TABLE IF NOT EXISTS artists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    image VARCHAR(255)
);

-- Tabla Stages
CREATE TABLE IF NOT EXISTS stages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity BIGINT NOT NULL
);

-- Tabla Attendees
CREATE TABLE IF NOT EXISTS attendees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(25) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Tabla Concerts (Relación con Artists y Stages)
CREATE TABLE IF NOT EXISTS concerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time DATETIME(6) NOT NULL,
    end_time DATETIME(6) NOT NULL,
    artist_id BIGINT NOT NULL,
    stage_id BIGINT NOT NULL,
    CONSTRAINT fk_concert_artist FOREIGN KEY (artist_id) REFERENCES artists(id),
    CONSTRAINT fk_concert_stage FOREIGN KEY (stage_id) REFERENCES stages(id)
);

-- Tabla Tickets (Relación con Attendees)
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DOUBLE NOT NULL,
    type VARCHAR(20) NOT NULL,
    is_used BIT(1) NOT NULL,
    attendee_id BIGINT NOT NULL,
    CONSTRAINT fk_ticket_attendee FOREIGN KEY (attendee_id) REFERENCES attendees(id)
);