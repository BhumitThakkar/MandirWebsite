package org.shreejalarammandir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "catering", schema = "sjm")
public class Catering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "hall_reservation_id", unique = true)
    private HallReservation hallReservation;

    @Column(name = "meal_summary", length = 1024)
    private String mealSummary;

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

    public String getMealSummary() {
        return mealSummary;
    }

    public void setMealSummary(String mealSummary) {
        this.mealSummary = mealSummary;
    }
}
