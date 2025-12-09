package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.ConcertDTO;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ArtistRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ConcertRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.StageRepository;
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
@RequestMapping("/concerts")
public class ConcertController {
    private static final Logger logger = LoggerFactory.getLogger(ConcertController.class);

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listConcerts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {
        logger.info("Listing concerts. Page: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Concert> concertPage;
        if (keyword == null || keyword.isEmpty()) {
            // Si no hay palabra clave, traemos todos los conciertos paginados
            concertPage = concertRepository.findAll(pageable);
        } else {
            // Si hay palabra clave, usamos el metodo de búsqueda personalizado (por nombre, dni, email...)
            concertPage = concertRepository.searchConcerts(keyword, pageable);
        }

        // Empaquetado de datos (DTO)
        // Usamos un DTO para enviar la lista de conciertos y la info de paginación a la vista de forma limpia
        ConcertDTO concertDTO = new ConcertDTO(
                concertPage.getContent(),
                concertPage.getTotalPages(),
                page
        );

        logger.info("Se han cargado {} conciertos.", concertDTO.getConcerts().size());
        model.addAttribute("listConcerts", concertDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
        return "concert";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Solicitando formulario para nuevo agente...");
        model.addAttribute("concert", new Concert());
        model.addAttribute("allArtists", artistRepository.findAll());
        model.addAttribute("allStages", stageRepository.findAll());
        return "concert-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        logger.info("Mostrando formulario de edición para la sucursal con ID {}", id);
        Optional<Concert> concertOpt = concertRepository.findById(id);

        if (concertOpt.isEmpty()) {
            logger.warn("No se encontró el concierto con ID {}", id);

            String message = messageSource.getMessage("msg.concert.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/concerts";
        }

        model.addAttribute("concert", concertOpt.get());
        model.addAttribute("allArtists", artistRepository.findAll());
        model.addAttribute("allStages", stageRepository.findAll());
        return "concert-form";
    }

    @PostMapping("/insert")
    public String insertConcert(@Valid @ModelAttribute("concert") Concert concert,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        logger.info("Intentando insertar nuevo concierto...");

        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario de concierto.");
            model.addAttribute("allArtists", artistRepository.findAll());
            model.addAttribute("allStages", stageRepository.findAll());
            return "concert-form";
        }

        concertRepository.save(concert);

        String message = messageSource.getMessage("msg.concert.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/concerts";
    }

    @PostMapping("/update")
    public String updateConcert(@Valid @ModelAttribute("concert") Concert concert,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        logger.info("Actualizando sucursal con ID {}", concert.getId());

        if (result.hasErrors()) {
            logger.warn("Errores de validación al actualizar el concierto.");
            model.addAttribute("allArtists", artistRepository.findAll());
            model.addAttribute("allStages", stageRepository.findAll());
            return "concert-form";
        }

        concertRepository.save(concert);
        logger.info("Concierto con ID {} actualizado con éxito.", concert.getId());

        String message = messageSource.getMessage("msg.concert.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/concerts";
    }

    @PostMapping("/delete")
    public String deleteConcert(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        logger.info("Eliminando concierto con ID {}", id);

        concertRepository.deleteById(id);
        logger.info("Concierto con ID {} eliminado correctamente", id);

        String message = messageSource.getMessage("msg.concert.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/concerts";
    }
}
