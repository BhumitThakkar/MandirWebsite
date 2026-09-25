package org.shreejalarammandir.controller;

import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.dto.SubmitReservationResult;
import org.shreejalarammandir.service.HallReservationService;
import org.shreejalarammandir.service.calendar.CalendarSyncOutcome;
import org.shreejalarammandir.web.HallReservationMessageKeys;
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
public class HallReservationController {

    private final HallReservationService hallReservationService;
    private final ValidationMessageResolver validationMessageResolver;

    public HallReservationController(
            HallReservationService hallReservationService,
            ValidationMessageResolver validationMessageResolver) {
        this.hallReservationService = hallReservationService;
        this.validationMessageResolver = validationMessageResolver;
    }

    @GetMapping({"/", "/hall"})
    public String hallForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new HallReservationForm());
        }
        return "hall";
    }

    @PostMapping("/hall")
    public String submit(
            @Valid @ModelAttribute("form") HallReservationForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "hall";
        }
        SubmitReservationResult result = hallReservationService.submitReservation(form);
        redirectAttributes.addFlashAttribute("success", flashFor(result));
        redirectAttributes.addFlashAttribute("publicId", result.getReservation().getPublicId());
        return "redirect:/hall";
    }

    String flashFor(SubmitReservationResult result) {
        boolean paymentApproved = Boolean.TRUE.equals(result.getReservation().getPaymentValidated());
        CalendarSyncOutcome calendar = result.getCalendarOutcome();
        if (calendar != null && calendar.isSuccess()) {
            return validationMessageResolver.resolveKey(paymentApproved
                    ? HallReservationMessageKeys.SUCCESS_APPROVED_WITH_CALENDAR
                    : HallReservationMessageKeys.SUCCESS_PENDING_WITH_CALENDAR);
        }
        if (calendar != null && calendar.isFailed()) {
            return validationMessageResolver.resolveKey(paymentApproved
                    ? HallReservationMessageKeys.SUCCESS_APPROVED_CALENDAR_FAILED
                    : HallReservationMessageKeys.SUCCESS_PENDING_CALENDAR_FAILED);
        }
        return validationMessageResolver.resolveKey(paymentApproved
                ? HallReservationMessageKeys.SUCCESS_APPROVED
                : HallReservationMessageKeys.SUCCESS_PENDING);
    }
}
