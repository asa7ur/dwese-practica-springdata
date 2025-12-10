package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE " +
            "LOWER(t.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.attendee.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<Ticket> searchTickets(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByAttendeeId(Long attendeeId);

    // Cuenta la cantidad de tickets por tipo y estado
    long countByType(Ticket.Type type);

    long countByUsed(boolean isUsed);

    @Query("SELECT SUM(t.price) FROM Ticket t")
    Double sumTotalSales();
}
