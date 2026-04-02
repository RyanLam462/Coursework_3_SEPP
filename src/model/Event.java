package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents an event in the university events app.
 *
 * <p>
 * An event is created by an {@link EntertainmentProvider}
 * and may have one or more {@link Performance} instances.
 * Events can be ticketed (requiring booking and payment)
 * or non-ticketed (free entry).
 * </p>
 */
public class Event {

    private final long eventID;
    private String title;
    private EventType type;

    /**
     * Whether the event is ticketed. If {@code true},
     * students must book and pay; if {@code false}, the
     * event is free.
     */
    private boolean isTicketed;

    private final EntertainmentProvider organiser;
    private final List<Performance> performances;

    /**
     * Constructs a new {@code Event}.
     *
     * @param eventID    the unique event identifier
     * @param title      the event title; must not be null or blank
     * @param type       the type of event; must not be null
     * @param isTicketed whether the event requires tickets
     * @param organiser  the EP organising this event; must not be null
     */
    public Event(long eventID, String title, EventType type, boolean isTicketed, EntertainmentProvider organiser) {
        assert eventID >= 0 : "Event ID must be non-negative";
        assert title != null && !title.isBlank() : "Title must not be null or blank";
        assert type != null : "EventType must not be null";
        assert organiser != null : "Organiser must not be null";
        this.eventID = eventID;
        this.title = title;
        this.type = type;
        this.isTicketed = isTicketed;
        this.organiser = organiser;
        this.performances = new ArrayList<>();
    }

    public long getEventID() {
        return eventID;
    }

    public String getEventTitle() {
        return title;
    }

    public void setEventTitle(String title) {
        assert title != null && !title.isBlank() : "Title must not be null or blank";
        this.title = title;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        assert type != null : "EventType must not be null";
        this.type = type;
    }

    public boolean getIsTicketed() {
        return isTicketed;
    }

    public void setIsTicketed(boolean isTicketed) {
        this.isTicketed = isTicketed;
    }

    public EntertainmentProvider getOrganiser() {
        return organiser;
    }

    /**
     * Creates a new {@link Performance}, adds it to this
     * event, and returns it.
     *
     * @param performanceID      the unique performance ID
     * @param startDateTime      start date/time
     * @param endDateTime        end date/time
     * @param performerNames     names of the performers
     * @param venueAddress       the venue address
     * @param venueCapacity      the venue capacity
     * @param venueIsOutdoors    whether the venue is
     *                           outdoors
     * @param venueAllowsSmoking whether the venue allows
     *                           smoking
     * @param numTickets         total tickets available
     * @param ticketPrice        price per ticket in GBP
     * @return the newly created {@link Performance}
     */
    public Performance createPerformance(
            long performanceID,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Collection<String> performerNames,
            String venueAddress,
            int venueCapacity,
            boolean venueIsOutdoors,
            boolean venueAllowsSmoking,
            int numTickets,
            double ticketPrice) {
        Performance p = new Performance(
                performanceID, this, startDateTime,
                endDateTime, performerNames, venueAddress,
                venueCapacity, venueIsOutdoors,
                venueAllowsSmoking, numTickets, ticketPrice);
        performances.add(p);
        return p;
    }

    public Performance getPerformanceByID(long performanceID) {
        for (Performance p : performances) {
            if (p.getID() == performanceID) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns information about performances that start
     * on the given date.
     *
     * @param searchDateTime the date/time to match
     *                       (performances starting on the same date)
     * @return a collection of performance info strings
     */
    public Collection<String> getInfoOfPerformancesOnDate(LocalDateTime searchDateTime) {
        List<String> infos = new ArrayList<>();
        for (Performance p : performances) {
            if (p.getStartDateTime().toLocalDate().equals(searchDateTime.toLocalDate())) {
                infos.add(p.toString());
            }
        }
        return infos;
    }

    public String getOrganiserName() {
        return organiser.getOrgName();
    }

    public String getOrganiserEmail() {
        return organiser.getEmail();
    }

    /**
     * Checks whether this event already has a performance
     * whose time range overlaps with the given range.
     *
     * @param startDateTime the start of the range to check
     * @param endDateTime   the end of the range to check
     * @return {@code true} if an overlapping performance
     *         exists
     */
    public boolean hasPerformanceAtSameTimes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        for (Performance p : performances) {
            // Overlap: existing.start < new.end
            // && existing.end > new.start
            if (p.getStartDateTime().isBefore(endDateTime) && p.getEndDateTime().isAfter(startDateTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an unmodifiable view of the performances
     * for this event.
     *
     * @return an unmodifiable list of performances
     */
    public List<Performance> getPerformances() {
        return Collections.unmodifiableList(performances);
    }

    public void addPerformance(Performance performance) {
        assert performance != null : "Performance must not be null";
        performances.add(performance);
    }

    /**
     * Returns a string representation of this event.
     *
     * @return a descriptive string
     */
    @Override
    public String toString() {
        return "Event{id=" + eventID
                + ", title='" + title
                + "', type=" + type
                + ", ticketed=" + isTicketed + '}';
    }
}
