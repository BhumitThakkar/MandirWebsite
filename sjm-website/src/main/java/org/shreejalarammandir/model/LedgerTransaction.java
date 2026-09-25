package org.shreejalarammandir.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ledger_transactions", schema = "sjm")
public class LedgerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hall_reservation_id")
    private Long hallReservationId;

    @Column(name = "pujari_seva_id")
    private Long pujariSevaId;

    @Column(nullable = false)
    private boolean voided;

    @Column(name = "amount_cents")
    private Long amountCents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHallReservationId() {
        return hallReservationId;
    }

    public void setHallReservationId(Long hallReservationId) {
        this.hallReservationId = hallReservationId;
    }

    public Long getPujariSevaId() {
        return pujariSevaId;
    }

    public void setPujariSevaId(Long pujariSevaId) {
        this.pujariSevaId = pujariSevaId;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }
}
