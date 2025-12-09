package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.attendee.dni.notEmpty}")
    @Column(name = "dni", nullable = false, length = 20)
    private String dni;

    @NotEmpty(message = "{msg.attendee.name.notEmpty}")
    @Size(max = 100, message = "{msg.attendee.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotEmpty(message = "{msg.attendee.phone.notEmpty}")
    @Size(max = 25, message = "{msg.attendee.phone.size}")
    @Column(name = "phone", nullable = false, length = 25)
    private String phone;

    @NotEmpty(message = "{msg.attendee.email.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.attendee.email.notValid}")
    @Size(max = 100, message = "{msg.attendee.email.size}")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    // Relación OneToMany: Un asistente puede comprar varios tickets
    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();
}