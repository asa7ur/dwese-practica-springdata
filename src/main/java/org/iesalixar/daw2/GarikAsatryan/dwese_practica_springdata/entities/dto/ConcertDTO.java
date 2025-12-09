package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDTO {
    private List<Concert> concerts;
    private int pages;
    private int currentPage;
}
