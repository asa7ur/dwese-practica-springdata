package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Attendee;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.AttendeeDTO;
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
@RequestMapping("/attendees")
public class AttendeeController {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeController.class);

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listAttendees(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        logger.info("Listando asistentes. Pág: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        // Configuración de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Attendee> attendeePage;

        if (keyword == null || keyword.isEmpty()) {
            attendeePage = attendeeRepository.findAll(pageable);
        } else {
            // Búsqueda por DNI o Nombre
            attendeePage = attendeeRepository.searchAttendees(keyword, pageable);
        }

        // DTO para la vista
        AttendeeDTO attendeeDTO = new AttendeeDTO(
                attendeePage.getContent(),
                attendeePage.getTotalPages(),
                page
        );

        model.addAttribute("listAttendees", attendeeDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");

        return "attendee";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo asistente...");
        model.addAttribute("attendee", new Attendee());
        return "attendee-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        Optional<Attendee> attendeeOpt = attendeeRepository.findById(id);

        if (attendeeOpt.isEmpty()) {
            String message = messageSource.getMessage("msg.attendee.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/attendees";
        }

        model.addAttribute("attendee", attendeeOpt.get());
        return "attendee-form";
    }

    @PostMapping("/insert")
    public String insertAttendee(@Valid @ModelAttribute("attendee") Attendee attendee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "attendee-form";
        }

        attendeeRepository.save(attendee);

        String message = messageSource.getMessage("msg.attendee.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/attendees";
    }

    @PostMapping("/update")
    public String updateAttendee(@Valid @ModelAttribute("attendee") Attendee attendee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "attendee-form";
        }

        attendeeRepository.save(attendee);

        String message = messageSource.getMessage("msg.attendee.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/attendees";
    }

    @PostMapping("/delete")
    public String deleteAttendee(@RequestParam("id") Long id,
                                 RedirectAttributes redirectAttributes) {

        // Validación: No borrar si tiene entradas compradas
        if (ticketRepository.existsByAttendeeId(id)) {
            String message = messageSource.getMessage("msg.attendee.flash.has-tickets", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/attendees";
        }

        attendeeRepository.deleteById(id);

        String message = messageSource.getMessage("msg.attendee.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/attendees";
    }
}