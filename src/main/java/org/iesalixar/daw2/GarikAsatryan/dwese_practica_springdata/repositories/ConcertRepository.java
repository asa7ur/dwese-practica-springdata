package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Query("SELECT c FROM Concert c WHERE " +
            "LOWER(c.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.stage.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Concert> searchConcerts(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByStageId(Long stageId);

    boolean existsByArtistId(Long artistId);

    // Obtener los próximos 3 conciertos ordenados por fecha
    List<Concert> findTop3ByStartTimeAfterOrderByStartTimeAsc(LocalDateTime now);

    // Comprueba que un concierto no solape con otro en el mismo escenario (excluye a si mismo)
    @Query("SELECT COUNT(c) > 0 FROM Concert c WHERE " +
            "c.stage.id = :stageId AND " +
            "c.startTime < :endTime AND " +
            "c.endTime > :startTime AND " +
            "(:excludeId IS NULL OR c.id != :excludeId)")
    boolean existsOverlappingConcert(@Param("stageId") Long stageId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("excludeId") Long excludeId);
}
