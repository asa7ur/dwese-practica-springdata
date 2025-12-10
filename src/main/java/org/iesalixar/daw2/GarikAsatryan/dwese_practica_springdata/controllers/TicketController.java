package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Ticket;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.TicketDTO;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.AttendeeRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listTickets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        logger.info("Listando entradas. Pág: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        // Ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Ticket> ticketPage;

        if (keyword == null || keyword.isEmpty()) {
            ticketPage = ticketRepository.findAll(pageable);
        } else {
            // Búsqueda por Tipo o Nombre de Asistente
            ticketPage = ticketRepository.searchTickets(keyword, pageable);
        }

        // DTO
        TicketDTO ticketDTO = new TicketDTO(
                ticketPage.getContent(),
                ticketPage.getTotalPages(),
                page
        );

        model.addAttribute("listTickets", ticketDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");

        return "ticket";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario de venta de entrada...");
        model.addAttribute("ticket", new Ticket());
        // Cargar asistentes para el desplegable
        model.addAttribute("allAttendees", attendeeRepository.findAll());
        return "ticket-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        Optional<Ticket> ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isEmpty()) {
            String message = messageSource.getMessage("msg.ticket.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/tickets";
        }

        model.addAttribute("ticket", ticketOpt.get());
        model.addAttribute("allAttendees", attendeeRepository.findAll());
        return "ticket-form";
    }

    @PostMapping("/insert")
    public String insertTicket(@Valid @ModelAttribute("ticket") Ticket ticket,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Si falla, recargamos la lista de asistentes
            model.addAttribute("allAttendees", attendeeRepository.findAll());
            return "ticket-form";
        }

        ticketRepository.save(ticket);

        String message = messageSource.getMessage("msg.ticket.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/tickets";
    }

    @PostMapping("/update")
    public String updateTicket(@Valid @ModelAttribute("ticket") Ticket ticket,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("allAttendees", attendeeRepository.findAll());
            return "ticket-form";
        }

        ticketRepository.save(ticket);

        String message = messageSource.getMessage("msg.ticket.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/tickets";
    }

    @PostMapping("/delete")
    public String deleteTicket(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {

        ticketRepository.deleteById(id);

        String message = messageSource.getMessage("msg.ticket.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/tickets";
    }
}