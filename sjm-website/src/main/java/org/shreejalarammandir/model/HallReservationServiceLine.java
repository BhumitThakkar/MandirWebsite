package org.shreejalarammandir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "hall_reservation_service_lines", schema = "sjm")
public class HallReservationServiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hall_reservation_id")
    private HallReservation hallReservation;

    @Column(nullable = false)
    private String title;

    public HallReservationServiceLine() {
    }

    public HallReservationServiceLine(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HallReservation getHallReservation() {
        return hallReservation;
    }

    public void setHallReservation(HallReservation hallReservation) {
        this.hallReservation = hallReservation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
