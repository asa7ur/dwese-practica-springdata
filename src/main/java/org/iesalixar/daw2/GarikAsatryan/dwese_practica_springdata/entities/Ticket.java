package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{msg.ticket.price.notNull}")
    @Positive(message = "{msg.ticket.price.positive}")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull(message = "{msg.ticket.type.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "is_used", nullable = false)
    private boolean used;

    public enum Type {
        VIP, GENERAL;
    }

    // Relación ManyToOne: Muchos tickets pertenecen a un asistente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    @ToString.Exclude
    private Attendee attendee;
}
