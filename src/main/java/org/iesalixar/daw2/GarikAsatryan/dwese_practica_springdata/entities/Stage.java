package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.stage.name.notEmpty}")
    @Size(max = 100, message = "{msg.stage.name.notEmpty}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "{msg.stage.capacity.notNull}")
    @Column(name = "capacity", nullable = false)
    private BigInteger capacity;

    // Relación OneToMany: Un escenario alberga muchos conciertos
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Concert> concerts = new ArrayList<>();
}