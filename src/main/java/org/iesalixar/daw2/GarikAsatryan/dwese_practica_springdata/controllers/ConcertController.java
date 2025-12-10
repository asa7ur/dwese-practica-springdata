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

        logger.info("Listando conciertos. Pág: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        // Configuración de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Concert> concertPage;

        if (keyword == null || keyword.isEmpty()) {
            concertPage = concertRepository.findAll(pageable);
        } else {
            // Busca por nombre de artista o nombre de escenario
            concertPage = concertRepository.searchConcerts(keyword, pageable);
        }

        // Empaquetado DTO
        ConcertDTO concertDTO = new ConcertDTO(
                concertPage.getContent(),
                concertPage.getTotalPages(),
                page
        );

        model.addAttribute("listConcerts", concertDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");

        return "concert";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo concierto...");
        model.addAttribute("concert", new Concert());
        // Cargar listas para los desplegables
        model.addAttribute("allArtists", artistRepository.findAll());
        model.addAttribute("allStages", stageRepository.findAll());
        return "concert-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        Optional<Concert> concertOpt = concertRepository.findById(id);

        if (concertOpt.isEmpty()) {
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

        // Comprobamos si no hay errores previos en fechas y escenario
        if (!result.hasFieldErrors("startTime") &&
                !result.hasFieldErrors("endTime") &&
                !result.hasFieldErrors("stage")) {

            boolean isOverlapping = concertRepository.existsOverlappingConcert(
                    concert.getStage().getId(),
                    concert.getStartTime(),
                    concert.getEndTime(),
                    null // ID es null porque es una inserción nueva
            );

            if (isOverlapping) {
                // Añadimos el error al campo "stage" o globalmente
                result.rejectValue("stage", "msg.concert.stage.busy",
                        "El escenario ya tiene un concierto programado en ese horario.");
            }
        }

        if (result.hasErrors()) {
            // Si hay error, recargamos las listas para que el formulario se vea bien
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

        if (!result.hasFieldErrors("startTime") &&
                !result.hasFieldErrors("endTime") &&
                !result.hasFieldErrors("stage")) {

            boolean isOverlapping = concertRepository.existsOverlappingConcert(
                    concert.getStage().getId(),
                    concert.getStartTime(),
                    concert.getEndTime(),
                    concert.getId() // Pasamos el ID actual para excluirlo de la búsqueda
            );

            if (isOverlapping) {
                result.rejectValue("stage", "msg.concert.stage.busy",
                        "El escenario ya tiene un concierto programado en ese horario.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("allArtists", artistRepository.findAll());
            model.addAttribute("allStages", stageRepository.findAll());
            return "concert-form";
        }

        concertRepository.save(concert);

        String message = messageSource.getMessage("msg.concert.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/concerts";
    }

    @PostMapping("/delete")
    public String deleteConcert(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes) {

        concertRepository.deleteById(id);

        String message = messageSource.getMessage("msg.concert.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/concerts";
    }
}