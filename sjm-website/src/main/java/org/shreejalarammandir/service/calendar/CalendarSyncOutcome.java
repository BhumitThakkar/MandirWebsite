package org.shreejalarammandir.service.calendar;

public final class CalendarSyncOutcome {

    public enum State {
        NOT_ATTEMPTED,
        SUCCESS,
        FAILED
    }

    private final State state;
    private final String eventId;

    private CalendarSyncOutcome(State state, String eventId) {
        this.state = state;
        this.eventId = eventId;
    }

    public static CalendarSyncOutcome notAttempted() {
        return new CalendarSyncOutcome(State.NOT_ATTEMPTED, null);
    }

    public static CalendarSyncOutcome success(String eventId) {
        return new CalendarSyncOutcome(State.SUCCESS, eventId);
    }

    public static CalendarSyncOutcome failed() {
        return new CalendarSyncOutcome(State.FAILED, null);
    }

    public State getState() {
        return state;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isNotAttempted() {
        return state == State.NOT_ATTEMPTED;
    }

    public boolean isSuccess() {
        return state == State.SUCCESS;
    }

    public boolean isFailed() {
        return state == State.FAILED;
    }
}
