package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("SELECT a FROM Artist a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.country) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Artist> searchArtists(@Param("keyword") String keyword, Pageable pageable);
}
