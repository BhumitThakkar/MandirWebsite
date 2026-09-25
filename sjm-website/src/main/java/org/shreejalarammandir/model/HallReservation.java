package org.shreejalarammandir.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "hall_reservations", schema = "sjm")
public class HallReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, length = 64)
    private String publicId;

    @Column(name = "event_title", nullable = false)
    private String eventTitle;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private ReservationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BasementOccupancy basement;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(nullable = false)
    private boolean nonprofit;

    @Column(name = "payment_validated")
    private Boolean paymentValidated;

    @Column(name = "sjm_pujari_seva", nullable = false)
    private boolean sjmPujariSeva;

    @Column(name = "sjm_catering_seva", nullable = false)
    private boolean sjmCateringSeva;

    @Column(name = "admin_notes", length = 2048)
    private String adminNotes;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "google_calendar_event_id", length = 1024)
    private String googleCalendarEventId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "hallReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HallReservationServiceLine> serviceLines = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

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

    public Boolean getPaymentValidated() {
        return paymentValidated;
    }

    public void setPaymentValidated(Boolean paymentValidated) {
        this.paymentValidated = paymentValidated;
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

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<HallReservationServiceLine> getServiceLines() {
        return serviceLines;
    }

    public void setServiceLines(List<HallReservationServiceLine> serviceLines) {
        this.serviceLines = serviceLines;
    }

    public void replaceServiceLines(List<HallReservationServiceLine> lines) {
        this.serviceLines.clear();
        if (lines == null) {
            return;
        }
        for (HallReservationServiceLine line : lines) {
            line.setHallReservation(this);
            this.serviceLines.add(line);
        }
    }
}
