package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.entities.Stage;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.ConcertRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.repositories.StageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/stages")
public class StageController {

    private static final Logger logger = LoggerFactory.getLogger(StageController.class);

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listStages(Model model) {
        logger.info("Listando todos los escenarios...");

        // Sin paginación ni búsqueda, listado directo
        List<Stage> stages = stageRepository.findAll();

        model.addAttribute("stages", stages);
        return "stage";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo escenario...");
        model.addAttribute("stage", new Stage());
        return "stage-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        logger.info("Mostrando formulario de edición para el escenario con ID {}", id);
        Optional<Stage> stageOpt = stageRepository.findById(id);

        if (stageOpt.isEmpty()) {
            String message = messageSource.getMessage("msg.stage.flash.not-found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/stages";
        }

        model.addAttribute("stage", stageOpt.get());
        return "stage-form";
    }

    @PostMapping("/insert")
    public String insertStage(@Valid @ModelAttribute("stage") Stage stage,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "stage-form";
        }

        stageRepository.save(stage);

        String message = messageSource.getMessage("msg.stage.flash.created", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/stages";
    }

    @PostMapping("/update")
    public String updateStage(@Valid @ModelAttribute("stage") Stage stage,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "stage-form";
        }

        stageRepository.save(stage);

        String message = messageSource.getMessage("msg.stage.flash.updated", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/stages";
    }

    @PostMapping("/delete")
    public String deleteStage(@RequestParam("id") Long id,
                              RedirectAttributes redirectAttributes) {

        // Comprobamos si tiene conciertos asignados antes de borrar
        if (concertRepository.existsByStageId(id)) {
            String message = messageSource.getMessage("msg.stage.flash.has-concerts", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/stages";
        }

        stageRepository.deleteById(id);

        String message = messageSource.getMessage("msg.stage.flash.deleted", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/stages";
    }
}