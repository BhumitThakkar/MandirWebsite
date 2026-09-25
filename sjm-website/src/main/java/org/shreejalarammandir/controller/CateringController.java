package org.shreejalarammandir.controller;

import org.shreejalarammandir.dto.CateringForm;
import org.shreejalarammandir.service.CateringService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Live BT_PC name. This is the only admin caller of saveCateringAndApproveReservation.
 * Do not hook CateringService.save.
 */
@Controller
@RequestMapping("/admin")
public class CateringController {

    private final CateringService cateringService;

    public CateringController(CateringService cateringService) {
        this.cateringService = cateringService;
    }

    @PostMapping("/hall/{publicId}/catering")
    public String saveCateringAndApproveReservation(
            @PathVariable String publicId,
            @ModelAttribute("cateringForm") CateringForm form,
            RedirectAttributes redirectAttributes) {
        cateringService.saveCateringAndApproveReservation(publicId, form);
        redirectAttributes.addFlashAttribute("success", "Catering saved and reservation approved.");
        return "redirect:/admin/hall/" + publicId;
    }

    @PostMapping("/hall/{publicId}/catering/delete")
    public String deleteByHallReservationId(
            @PathVariable String publicId,
            RedirectAttributes redirectAttributes) {
        cateringService.deleteByHallReservationId(publicId);
        redirectAttributes.addFlashAttribute("success", "Catering removed.");
        return "redirect:/admin/hall/" + publicId;
    }
}
