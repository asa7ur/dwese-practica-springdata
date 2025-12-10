package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Artist;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.ArtistDTO;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ArtistRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ConcertRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.services.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;
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
    private FileStorageService fileStorageService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listArtists(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        logger.info("Listando artistas. Pág: {}, Keyword: {}, Sort: {}, Dir: {}", page, keyword, sortBy, direction);

        int pageSize = 6; // Tamaño de página ajustado al diseño de grid

        // Configuración de la ordenación dinámica
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Artist> artistPage;

        if (keyword == null || keyword.isEmpty()) {
            artistPage = artistRepository.findAll(pageable);
        } else {
            artistPage = artistRepository.searchArtists(keyword, pageable);
        }

        // Empaquetado en DTO para la vista
        ArtistDTO artistDTO = new ArtistDTO(
                artistPage.getContent(),
                artistPage.getTotalPages(),
                page
        );

        model.addAttribute("listArtists", artistDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        // Helper para invertir la dirección en los encabezados si quisieras implementarlo
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
        Optional<Artist> artistOpt = artistRepository.findById(id);

        if (artistOpt.isEmpty()) {
            String message = messageSource.getMessage("msg.artist.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/artists";
        }

        model.addAttribute("artist", artistOpt.get());
        return "artist-form";
    }

    @PostMapping("/insert")
    public String insertArtist(@Valid @ModelAttribute("artist") Artist artist,
                               BindingResult result,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "artist-form";
        }

        // 3. Guardar imagen si no está vacía
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                artist.setImage(fileName);
            }
        }

        artistRepository.save(artist);

        String message = messageSource.getMessage("msg.artist.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }

    @PostMapping("/update")
    public String updateArtist(@Valid @ModelAttribute("artist") Artist artist,
                               BindingResult result,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "artist-form";
        }

        // Lógica para mantener la imagen anterior si no suben una nueva
        // Recuperamos el artista original de la BD para saber qué imagen tenía
        Artist existingArtist = artistRepository.findById(artist.getId()).orElse(null);

        if (!imageFile.isEmpty()) {
            // Si suben nueva imagen, borramos la vieja y guardamos la nueva
            if (existingArtist != null && existingArtist.getImage() != null) {
                fileStorageService.deleteFile(existingArtist.getImage());
            }
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                artist.setImage(fileName);
            }
        } else {
            // Si no suben nada, mantenemos la imagen que ya tenía
            if (existingArtist != null) {
                artist.setImage(existingArtist.getImage());
            }
        }

        artistRepository.save(artist);

        String message = messageSource.getMessage("msg.artist.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }

    @PostMapping("/delete")
    public String deleteArtist(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {

        // Validación de integridad referencial (no borrar si tiene conciertos)
        if (concertRepository.existsByArtistId(id)) {
            String message = messageSource.getMessage("msg.artist.flash.has-concerts", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/artists";
        }

        artistRepository.deleteById(id);

        String message = messageSource.getMessage("msg.artist.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/artists";
    }
}