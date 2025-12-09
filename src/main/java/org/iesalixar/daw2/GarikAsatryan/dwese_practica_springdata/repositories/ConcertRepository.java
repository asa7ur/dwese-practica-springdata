package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
