package org.shreejalarammandir.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.shreejalarammandir.model.BasementOccupancy;
import org.shreejalarammandir.model.ReservationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HallReservationAdminForm {

    @NotBlank
    private String eventTitle;

    @NotNull
    private LocalDate reservationDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private ReservationStatus status;

    private BasementOccupancy basement;

    private Integer guestCount;

    private boolean nonprofit;

    private boolean sjmPujariSeva;

    private boolean sjmCateringSeva;

    private String pujariLineTitles;

    private String adminNotes;

    private String googleCalendarEventId;

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BasementOccupancy getBasement() {
        return basement;
    }

    public void setBasement(BasementOccupancy basement) {
        this.basement = basement;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public boolean isNonprofit() {
        return nonprofit;
    }

    public void setNonprofit(boolean nonprofit) {
        this.nonprofit = nonprofit;
    }

    public boolean isSjmPujariSeva() {
        return sjmPujariSeva;
    }

    public void setSjmPujariSeva(boolean sjmPujariSeva) {
        this.sjmPujariSeva = sjmPujariSeva;
    }

    public boolean isSjmCateringSeva() {
        return sjmCateringSeva;
    }

    public void setSjmCateringSeva(boolean sjmCateringSeva) {
        this.sjmCateringSeva = sjmCateringSeva;
    }

    public String getPujariLineTitles() {
        return pujariLineTitles;
    }

    public void setPujariLineTitles(String pujariLineTitles) {
        this.pujariLineTitles = pujariLineTitles;
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
