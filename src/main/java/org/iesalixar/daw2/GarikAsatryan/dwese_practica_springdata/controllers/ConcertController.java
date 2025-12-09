package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Concert;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.dto.ConcertDTO;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ConcertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/concerts")
public class ConcertController {
    private static final Logger logger = LoggerFactory.getLogger(ConcertController.class);

    @Autowired
    private ConcertRepository concertRepository;

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
        model.addAttribute("agent", new Concert());
        return "concert-form";
    }
}
