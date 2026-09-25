package org.shreejalarammandir.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.shreejalarammandir.model.BasementOccupancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HallReservationForm {

    @NotBlank
    private String eventTitle;

    @NotNull
    private LocalDate reservationDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private BasementOccupancy basement = BasementOccupancy.FULL;

    private Integer guestCount;

    private boolean nonprofit;

    private boolean sjmPujariSeva;

    private boolean sjmCateringSeva;

    private boolean paymentValidated;

    private String pujariLineTitles;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

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

    public boolean isPaymentValidated() {
        return paymentValidated;
    }

    public void setPaymentValidated(boolean paymentValidated) {
        this.paymentValidated = paymentValidated;
    }

    public String getPujariLineTitles() {
        return pujariLineTitles;
    }

    public void setPujariLineTitles(String pujariLineTitles) {
        this.pujariLineTitles = pujariLineTitles;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getGoogleCalendarEventId() {
        return googleCalendarEventId;
    }

    public void setGoogleCalendarEventId(String googleCalendarEventId) {
        this.googleCalendarEventId = googleCalendarEventId;
    }
}
