package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.artist.name.notEmpty}")
    @Size(max = 100, message = "{msg.artist.name.notEmpty}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotEmpty(message = "{msg.artist.genre.notEmpty}")
    @Size(max = 100, message = "{msg.artist.genre.notEmpty}")
    @Column(name = "genre", nullable = false, length = 100)
    private String genre;

    @NotEmpty(message = "{msg.artist.country.notEmpty}")
    @Size(max = 100, message = "{msg.artist.country.notEmpty}")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "image")
    private String image;

    // Relación OneToMany: Un artista puede tener muchos conciertos en varios escenarios
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Concert> concerts = new ArrayList<>();

}
