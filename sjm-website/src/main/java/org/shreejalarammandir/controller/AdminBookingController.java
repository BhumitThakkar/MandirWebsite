package org.shreejalarammandir.controller;

import org.shreejalarammandir.dto.CateringForm;
import org.shreejalarammandir.dto.HallReservationAdminForm;
import org.shreejalarammandir.dto.PujariSevaAdminForm;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.service.HallReservationService;
import org.shreejalarammandir.service.PujariSevaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminBookingController {

    private final HallReservationService hallReservationService;
    private final PujariSevaService pujariSevaService;

    public AdminBookingController(
            HallReservationService hallReservationService,
            PujariSevaService pujariSevaService) {
        this.hallReservationService = hallReservationService;
        this.pujariSevaService = pujariSevaService;
    }

    @GetMapping("/hall/{publicId}")
    public String hall(@PathVariable String publicId, Model model) {
        HallReservation hall = hallReservationService.requireByPublicId(publicId);
        model.addAttribute("hall", hall);
        if (!model.containsAttribute("adminForm")) {
            HallReservationAdminForm form = new HallReservationAdminForm();
            form.setEventTitle(hall.getEventTitle());
            form.setReservationDate(hall.getReservationDate());
            form.setStartTime(hall.getStartTime());
            form.setEndTime(hall.getEndTime());
            form.setStatus(hall.getStatus());
            form.setBasement(hall.getBasement());
            form.setGuestCount(hall.getGuestCount());
            form.setNonprofit(hall.isNonprofit());
            form.setSjmPujariSeva(hall.isSjmPujariSeva());
            form.setSjmCateringSeva(hall.isSjmCateringSeva());
            form.setAdminNotes(hall.getAdminNotes());
            model.addAttribute("adminForm", form);
        }
        if (!model.containsAttribute("cateringForm")) {
            model.addAttribute("cateringForm", new CateringForm());
        }
        return "admin-hall";
    }

    @PostMapping("/hall/{publicId}")
    public String updateHall(
            @PathVariable String publicId,
            @ModelAttribute("adminForm") HallReservationAdminForm form,
            RedirectAttributes redirectAttributes) {
        hallReservationService.adminUpdate(publicId, form);
        redirectAttributes.addFlashAttribute("success", "Hall reservation updated.");
        return "redirect:/admin/hall/" + publicId;
    }

    @PostMapping("/hall/{publicId}/delete")
    public String deleteHall(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        hallReservationService.delete(publicId);
        redirectAttributes.addFlashAttribute("success", "Hall reservation deleted.");
        return "redirect:/hall";
    }

    @GetMapping("/pujari-seva/{publicId}")
    public String pujari(@PathVariable String publicId, Model model) {
        PujariSeva seva = pujariSevaService.requireByPublicId(publicId);
        model.addAttribute("seva", seva);
        if (!model.containsAttribute("adminForm")) {
            PujariSevaAdminForm form = new PujariSevaAdminForm();
            form.setSevaDate(seva.getSevaDate());
            form.setStartTime(seva.getStartTime());
            form.setEndTime(seva.getEndTime());
            form.setSelectedSevas(seva.getSelectedSevas());
            form.setVenue(seva.getVenue());
            form.setStatus(seva.getStatus());
            form.setAdminNotes(seva.getAdminNotes());
            model.addAttribute("adminForm", form);
        }
        return "admin-pujari-seva";
    }

    @PostMapping("/pujari-seva/{publicId}")
    public String updatePujari(
            @PathVariable String publicId,
            @ModelAttribute("adminForm") PujariSevaAdminForm form,
            RedirectAttributes redirectAttributes) {
        pujariSevaService.adminUpdate(publicId, form);
        redirectAttributes.addFlashAttribute("success", "Pujari seva updated.");
        return "redirect:/admin/pujari-seva/" + publicId;
    }

    @PostMapping("/pujari-seva/{publicId}/delete")
    public String deletePujari(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        PujariSeva seva = pujariSevaService.requireByPublicId(publicId);
        pujariSevaService.deleteById(seva.getId());
        redirectAttributes.addFlashAttribute("success", "Pujari seva deleted.");
        return "redirect:/pujari-seva";
    }
}
