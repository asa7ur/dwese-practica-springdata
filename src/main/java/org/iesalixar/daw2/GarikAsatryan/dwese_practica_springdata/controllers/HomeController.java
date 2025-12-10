package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Ticket;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private AttendeeRepository attendeeRepository;

    @GetMapping("/")
    public String index(Model model) {
        // 1. Contadores Generales (KPIs)
        long totalArtists = artistRepository.count();
        long totalStages = stageRepository.count();
        long totalConcerts = concertRepository.count();
        long totalAttendees = attendeeRepository.count();

        // Calcular ingresos totales (controlar nulos si no hay tickets)
        Double totalSales = ticketRepository.sumTotalSales();
        if (totalSales == null) totalSales = 0.0;

        // 2. Estadísticas de Entradas (Para el gráfico de barras)
        long totalTickets = ticketRepository.count();
        long vipTickets = ticketRepository.countByType(Ticket.Type.VIP);
        long generalTickets = ticketRepository.countByType(Ticket.Type.GENERAL);
        long usedTickets = ticketRepository.countByUsed(true);

        // Porcentajes para las barras de progreso (evitar división por cero)
        int vipPercent = (totalTickets > 0) ? (int) ((vipTickets * 100) / totalTickets) : 0;
        int usedPercent = (totalTickets > 0) ? (int) ((usedTickets * 100) / totalTickets) : 0;

        // 3. Próximos Conciertos (Dashboard rápido)
        List<Concert> nextConcerts = concertRepository.findTop3ByStartTimeAfterOrderByStartTimeAsc(LocalDateTime.now());

        // Añadir al modelo
        model.addAttribute("totalArtists", totalArtists);
        model.addAttribute("totalStages", totalStages);
        model.addAttribute("totalConcerts", totalConcerts);
        model.addAttribute("totalAttendees", totalAttendees);
        model.addAttribute("totalSales", totalSales);

        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("vipTickets", vipTickets);
        model.addAttribute("generalTickets", generalTickets);
        model.addAttribute("vipPercent", vipPercent);
        model.addAttribute("generalPercent", 100 - vipPercent);

        model.addAttribute("usedTickets", usedTickets);
        model.addAttribute("unusedTickets", totalTickets - usedTickets);
        model.addAttribute("usedPercent", usedPercent);

        model.addAttribute("nextConcerts", nextConcerts);

        return "index";
    }
}