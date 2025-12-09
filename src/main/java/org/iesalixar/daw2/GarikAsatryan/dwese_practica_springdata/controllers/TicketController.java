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
        logger.info("Listing tickets. Page: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Ticket> ticketPage;
        if (keyword == null || keyword.isEmpty()) {
            // Si no hay palabra clave, traemos todos los entradas paginados
            ticketPage = ticketRepository.findAll(pageable);
        } else {
            // Si hay palabra clave, usamos el metodo de búsqueda personalizado (por nombre, dni, email...)
            ticketPage = ticketRepository.searchTickets(keyword, pageable);
        }

        // Empaquetado de datos (DTO)
        // Usamos un DTO para enviar la lista de entradas y la info de paginación a la vista de forma limpia
        TicketDTO ticketDTO = new TicketDTO(
                ticketPage.getContent(),
                ticketPage.getTotalPages(),
                page
        );

        logger.info("Se han cargado {} entradas.", ticketDTO.getTickets().size());
        model.addAttribute("listTickets", ticketDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
        return "ticket";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Solicitando formulario para nuevo agente...");
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("allAttendees", attendeeRepository.findAll());
        return "ticket-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        logger.info("Mostrando formulario de edición para la entrada con ID {}", id);
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isEmpty()) {
            logger.warn("No se encontró la entrada con ID {}", id);

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

        logger.info("Intentando insertar nueva entrada...");

        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario de entrada.");
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

        logger.info("Actualizando sucursal con ID {}", ticket.getId());

        if (result.hasErrors()) {
            logger.warn("Errores de validación al actualizar la entrada.");
            model.addAttribute("allAttendees", attendeeRepository.findAll());
            return "ticket-form";
        }

        ticketRepository.save(ticket);
        logger.info("Entrada con ID {} actualizada con éxito.", ticket.getId());

        String message = messageSource.getMessage("msg.ticket.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/tickets";
    }

    @PostMapping("/delete")
    public String deleteTicket(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        logger.info("Eliminando entrada con ID {}", id);

        ticketRepository.deleteById(id);
        logger.info("Entrada con ID {} eliminada correctamente", id);

        String message = messageSource.getMessage("msg.ticket.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/tickets";
    }
}