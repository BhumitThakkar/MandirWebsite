package org.shreejalarammandir.controller;

import org.shreejalarammandir.dto.PujariSevaForm;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.service.PujariSevaService;
import org.shreejalarammandir.web.ValidationMessageResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class PujariSevaController {

    private final PujariSevaService pujariSevaService;
    private final ValidationMessageResolver validationMessageResolver;

    public PujariSevaController(
            PujariSevaService pujariSevaService,
            ValidationMessageResolver validationMessageResolver) {
        this.pujariSevaService = pujariSevaService;
        this.validationMessageResolver = validationMessageResolver;
    }

    @GetMapping("/pujari-seva")
    public String form(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new PujariSevaForm());
        }
        return "pujari-seva";
    }

    @PostMapping("/pujari-seva")
    public String submit(
            @Valid @ModelAttribute("form") PujariSevaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pujari-seva";
        }
        PujariSeva saved = pujariSevaService.submit(form);
        redirectAttributes.addFlashAttribute("success",
                validationMessageResolver.resolveKey("Flash.pujariSeva.success"));
        redirectAttributes.addFlashAttribute("publicId", saved.getPublicId());
        return "redirect:/pujari-seva";
    }
}
