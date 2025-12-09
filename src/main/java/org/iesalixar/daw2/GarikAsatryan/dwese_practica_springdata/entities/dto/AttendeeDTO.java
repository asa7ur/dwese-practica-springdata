package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Attendee;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeDTO {
    private List<Attendee> attendees;
    private int pages;
    private int currentPage;
}
