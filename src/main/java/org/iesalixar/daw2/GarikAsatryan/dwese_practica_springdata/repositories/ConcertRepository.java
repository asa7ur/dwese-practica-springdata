package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Query("SELECT c FROM Concert c WHERE " +
            "LOWER(c.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.stage.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Concert> searchConcerts(@Param("keyword") String keyword, Pageable pageable);
}
