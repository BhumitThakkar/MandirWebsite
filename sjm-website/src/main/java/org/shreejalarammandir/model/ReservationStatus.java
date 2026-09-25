package org.shreejalarammandir.model;

public enum ReservationStatus {
    PAYMENT_VALIDATION_PENDING,
    SJM_CATERING_PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    public boolean isSoftCancelOnCalendar() {
        return this == REJECTED || this == CANCELLED;
    }
}
