package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Artist;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.ArtistDTO;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ArtistRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ConcertRepository;
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
@RequestMapping("/artists")
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listArtists(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {
        logger.info("Listing artists. Page: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6;

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Artist> artistPage;
        if (keyword == null || keyword.isEmpty()) {
            // Si no hay palabra clave, traemos todos los artistas paginados
            artistPage = artistRepository.findAll(pageable);
        } else {
            // Si hay palabra clave, usamos el metodo de búsqueda personalizado (por nombre, género, país...)
            artistPage = artistRepository.searchArtists(keyword, pageable);
        }

        // Empaquetado de datos (DTO)
        // Usamos un DTO para enviar la lista de artistas y la info de paginación a la vista de forma limpia
        ArtistDTO artistDTO = new ArtistDTO(
                artistPage.getContent(),
                artistPage.getTotalPages(),
                page
        );

        logger.info("Se han cargado {} artistas.", artistDTO.getArtists().size());
        model.addAttribute("listArtists", artistDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
        return "artist";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo artista...");
        model.addAttribute("artist", new Artist());
        return "artist-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        logger.info("Mostrando formulario de edición para el artista con ID {}", id);
        Optional<Artist> artistOpt = artistRepository.findById(id);

        if (artistOpt.isEmpty()) {
            logger.warn("No se encontró el artista con ID {}", id);
            String message = messageSource.getMessage("msg.artist.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/artists";
        }

        model.addAttribute("artist", artistOpt.get());
        return "artist-form";
    }

    @PostMapping("/insert")
    public String insertStage(@Valid @ModelAttribute("artist") Artist artist,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        logger.info("Intentando insertar nuevo artista...");

        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario de artist.");
            return "artist-form";
        }

        artistRepository.save(artist);
        String message = messageSource.getMessage("msg.artist.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }

    @PostMapping("/update")
    public String updateStage(@Valid @ModelAttribute("artist") Artist artist,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        logger.info("Actualizando artista con ID {}", artist.getId());

        if (result.hasErrors()) {
            logger.warn("Errores de validación al actualizar el artista.");
            return "artist-form";
        }

        artistRepository.save(artist);
        logger.info("Artista con ID {} actualizado con éxito.", artist.getId());

        String message = messageSource.getMessage("msg.artist.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }

    @PostMapping("/delete")
    public String deleteStage(@RequestParam("id") Long id,
                              RedirectAttributes redirectAttributes) {
        logger.info("Intentando eliminar artista con ID {}", id);

        // Comprobamos si tiene conciertos asignados
        if (concertRepository.existsByArtistId(id)) {
            logger.warn("Intento de eliminar artista con ID {} fallido: Tiene conciertos asignados.", id);

            String message = messageSource.getMessage("msg.artist.flash.has-concerts", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/artists";
        }

        // Si no tiene conciertos, procedemos a borrar
        artistRepository.deleteById(id);
        logger.info("Artista con ID {} eliminado correctamente", id);

        String message = messageSource.getMessage("msg.artist.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }
}