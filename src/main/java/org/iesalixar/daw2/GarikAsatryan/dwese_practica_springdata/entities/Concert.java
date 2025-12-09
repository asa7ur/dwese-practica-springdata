package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "concerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{msg.concert.startTime.notNull}")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "{msg.concert.endTime.notNull}")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // Relación ManyToOne: Muchos conciertos pueden pertenecer a un artista
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    // Relación ManyToOne: Muchos conciertos ocurren en un escenario
    @NotNull(message = "{msg.concert.stage.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;
}