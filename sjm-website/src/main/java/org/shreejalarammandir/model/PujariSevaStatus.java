package org.shreejalarammandir.model;

public enum PujariSevaStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    public boolean isSoftCancelOnCalendar() {
        return this == REJECTED || this == CANCELLED;
    }
}
