package org.shreejalarammandir.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.shreejalarammandir.model.PujariSevaStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PujariSevaAdminForm {

    @NotNull
    private LocalDate sevaDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotBlank
    private String selectedSevas;

    private String venue;

    @NotNull
    private PujariSevaStatus status;

    private String adminNotes;

    private String googleCalendarEventId;

    public LocalDate getSevaDate() {
        return sevaDate;
    }

    public void setSevaDate(LocalDate sevaDate) {
        this.sevaDate = sevaDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSelectedSevas() {
        return selectedSevas;
    }

    public void setSelectedSevas(String selectedSevas) {
        this.selectedSevas = selectedSevas;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public PujariSevaStatus getStatus() {
        return status;
    }

    public void setStatus(PujariSevaStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public String getGoogleCalendarEventId() {
        return googleCalendarEventId;
    }

    public void setGoogleCalendarEventId(String googleCalendarEventId) {
        this.googleCalendarEventId = googleCalendarEventId;
    }
}
