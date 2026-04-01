package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single scheduled performance of an
 * {@link Event} in the university events app.
 *
 * <p>A performance has a specific date/time window, a
 * venue, a ticket price, and a limited number of tickets.
 * Students book individual performances rather than events
 * directly.</p>
 */
public class Performance {

    /** The unique identifier for this performance. */
    private final long performanceID;

    /** The event to which this performance belongs. */
    private final Event event;

    /** The date and time when the performance starts. */
    private LocalDateTime startDateTime;

    /** The date and time when the performance ends. */
    private LocalDateTime endDateTime;

    /** The names of the performers. */
    private Collection<String> performerNames;

    /** The venue address / location of the performance. */
    private String venueAddress;

    /** The capacity of the venue. */
    private int venueCapacity;

    /** Whether the venue is outdoors. */
    private boolean venueIsOutdoors;

    /** Whether the venue allows smoking. */
    private boolean venueAllowsSmoking;

    /** The total number of tickets available. */
    private int numTicketsTotal;

    /** The number of tickets sold so far. */
    private int numTicketsSold;

    /**
     * The price per ticket in GBP. A value of {@code 0}
     * indicates a free performance (non-ticketed events).
     */
    private double ticketPrice;

    /** The current status of this performance. */
    private PerformanceStatus status;

    /** Bookings associated with this performance. */
    private final List<Booking> bookings;

    /**
     * Constructs a new {@code Performance}.
     *
     * @param performanceID      the unique performance ID
     * @param event              the parent event; must not
     *                           be null
     * @param startDateTime      the start date/time; must
     *                           not be null
     * @param endDateTime        the end date/time; must not
     *                           be null and must be after
     *                           start
     * @param performerNames     names of the performers
     * @param venueAddress       the venue address; must not
     *                           be null or blank
     * @param venueCapacity      the capacity of the venue;
     *                           must be non-negative
     * @param venueIsOutdoors    whether the venue is outdoors
     * @param venueAllowsSmoking whether the venue allows
     *                           smoking
     * @param numTickets         total tickets available;
     *                           must be non-negative
     * @param ticketPrice        price per ticket in GBP;
     *                           must be non-negative
     */
    public Performance(long performanceID, Event event,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime,
                       Collection<String> performerNames,
                       String venueAddress,
                       int venueCapacity,
                       boolean venueIsOutdoors,
                       boolean venueAllowsSmoking,
                       int numTickets,
                       double ticketPrice) {
        assert performanceID >= 0
            : "Performance ID must be non-negative";
        assert event != null : "Event must not be null";
        assert startDateTime != null
            : "Start date/time must not be null";
        assert endDateTime != null
            : "End date/time must not be null";
        assert !endDateTime.isBefore(startDateTime)
            : "End must not be before start";
        assert venueAddress != null
            && !venueAddress.isBlank()
            : "Venue address must not be null or blank";
        assert venueCapacity >= 0
            : "Venue capacity must be non-negative";
        assert numTickets >= 0
            : "Total tickets must be non-negative";
        assert ticketPrice >= 0
            : "Ticket price must be non-negative";

        this.performanceID = performanceID;
        this.event = event;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames != null
            ? new ArrayList<>(performerNames)
            : new ArrayList<>();
        this.venueAddress = venueAddress;
        this.venueCapacity = venueCapacity;
        this.venueIsOutdoors = venueIsOutdoors;
        this.venueAllowsSmoking = venueAllowsSmoking;
        this.numTicketsTotal = numTickets;
        this.numTicketsSold = 0;
        this.ticketPrice = ticketPrice;
        this.status = PerformanceStatus.ACTIVE;
        this.bookings = new ArrayList<>();
    }

    // =====================================================
    // Getters
    // =====================================================

    /**
     * Returns the unique performance ID.
     *
     * @return the performance ID
     */
    public long getID() {
        return performanceID;
    }

    /**
     * Returns the event to which this performance belongs.
     *
     * @return the parent {@link Event}
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Returns the start date and time.
     *
     * @return the start {@link LocalDateTime}
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the end date and time.
     *
     * @return the end {@link LocalDateTime}
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Returns the performer names.
     *
     * @return a collection of performer name strings
     */
    public Collection<String> getPerformerNames() {
        return performerNames;
    }

    /**
     * Returns the venue address.
     *
     * @return the venue address string
     */
    public String getVenueAddress() {
        return venueAddress;
    }

    /**
     * Returns the venue capacity.
     *
     * @return the capacity of the venue
     */
    public int getVenueCapacity() {
        return venueCapacity;
    }

    /**
     * Returns whether the venue is outdoors.
     *
     * @return {@code true} if outdoors
     */
    public boolean getVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    /**
     * Returns whether the venue allows smoking.
     *
     * @return {@code true} if smoking is allowed
     */
    public boolean getVenueAllowsSmoking() {
        return venueAllowsSmoking;
    }

    /**
     * Returns the ticket price in GBP.
     *
     * @return the price per ticket
     */
    public double getTicketPrice() {
        return ticketPrice;
    }

    /**
     * Returns the total number of tickets available.
     *
     * @return the total ticket count
     */
    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    /**
     * Returns the number of tickets sold so far.
     *
     * @return the number of tickets sold
     */
    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    /**
     * Returns the current status of this performance.
     *
     * @return the {@link PerformanceStatus}
     */
    public PerformanceStatus getStatus() {
        return status;
    }


    // =====================================================
    // Setters
    // =====================================================

    /**
     * Sets the start date and time.
     *
     * @param startDateTime the new start date/time
     */
    public void setStartDateTime(
            LocalDateTime startDateTime) {
        assert startDateTime != null
            : "Start must not be null";
        this.startDateTime = startDateTime;
    }

    /**
     * Sets the end date and time.
     *
     * @param endDateTime the new end date/time
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        assert endDateTime != null
            : "End must not be null";
        this.endDateTime = endDateTime;
    }

    /**
     * Sets the performer names.
     *
     * @param performerNames the new performer names
     */
    public void setPerformerNames(
            Collection<String> performerNames) {
        this.performerNames = performerNames != null
            ? new ArrayList<>(performerNames)
            : new ArrayList<>();
    }

    /**
     * Sets the venue address.
     *
     * @param venueAddress the new venue address; must not
     *                     be null or blank
     */
    public void setVenueAddress(String venueAddress) {
        assert venueAddress != null
            && !venueAddress.isBlank()
            : "Venue address must not be null or blank";
        this.venueAddress = venueAddress;
    }

    /**
     * Sets the venue capacity.
     *
     * @param venueCapacity the new capacity; must be
     *                      non-negative
     */
    public void setVenueCapacity(int venueCapacity) {
        assert venueCapacity >= 0
            : "Capacity must be non-negative";
        this.venueCapacity = venueCapacity;
    }

    /**
     * Sets whether the venue is outdoors.
     *
     * @param venueIsOutdoors {@code true} if outdoors
     */
    public void setVenueIsOutdoors(
            boolean venueIsOutdoors) {
        this.venueIsOutdoors = venueIsOutdoors;
    }

    /**
     * Sets whether the venue allows smoking.
     *
     * @param venueAllowsSmoking {@code true} if smoking
     *                           is allowed
     */
    public void setVenueAllowsSmoking(
            boolean venueAllowsSmoking) {
        this.venueAllowsSmoking = venueAllowsSmoking;
    }

    /**
     * Sets the ticket price.
     *
     * @param ticketPrice the new price; must be
     *                    non-negative
     */
    public void setTicketPrice(double ticketPrice) {
        assert ticketPrice >= 0
            : "Price must be non-negative";
        this.ticketPrice = ticketPrice;
    }

    /**
     * Sets the total number of tickets available.
     *
     * @param numTicketsTotal the new total; must be
     *                        non-negative
     */
    public void setNumTicketsTotal(int numTicketsTotal) {
        assert numTicketsTotal >= 0
            : "Total tickets must be non-negative";
        this.numTicketsTotal = numTicketsTotal;
    }

    /**
     * Sets the number of tickets sold so far.
     *
     * @param numTicketsSold the new sold count; must be
     *                       non-negative and not exceed
     *                       the total
     */
    public void setNumTicketsSold(int numTicketsSold) {
        assert numTicketsSold >= 0
            : "Sold tickets must be non-negative";
        this.numTicketsSold = numTicketsSold;
    }

    /**
     * Sets the status of this performance.
     *
     * @param status the new status; must not be null
     */
    public void setStatus(PerformanceStatus status) {
        assert status != null
            : "Status must not be null";
        this.status = status;
    }


    // =====================================================
    // Business logic
    // =====================================================

    /**
     * Checks whether the event associated with this
     * performance is ticketed.
     *
     * @return {@code true} if the parent event is ticketed
     */
    public boolean checkIfEventIsTicketed() {
        return event.getIsTicketed();
    }

    /**
     * Checks whether there are enough tickets remaining
     * for the given request.
     *
     * @param numTicketsRequested the number of tickets
     *                            the student wants to buy
     * @return {@code true} if sufficient tickets remain
     */
    public boolean checkTicketsLeft(
            int numTicketsRequested) {
        return (numTicketsTotal - numTicketsSold)
            >= numTicketsRequested;
    }

    /**
     * Returns the ticket price.
     *
     * @return the price per ticket in GBP
     */
    public double getFinalTicketPrice() {
        return ticketPrice;
    }

    /**
     * Returns the organiser's email address.
     *
     * <p>Convenience method that delegates to the parent
     * event's organiser.</p>
     *
     * @return the organiser's email
     */
    public String getOrganiserEmail() {
        return event.getOrganiserEmail();
    }

    /**
     * Returns the event title.
     *
     * <p>Convenience method that delegates to the parent
     * event.</p>
     *
     * @return the event title string
     */
    public String getEventTitle() {
        return event.getEventTitle();
    }

    /**
     * Checks whether the performance has not yet occurred.
     *
     * @return {@code true} if the performance start time
     *         is in the future
     */
    public boolean checkHasNotHappenedYet() {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the given EP email matches the
     * organiser of this performance's event.
     *
     * <p>Used to verify that the EP attempting to cancel
     * a performance is the one who created it.</p>
     *
     * @param epEmail the email to check
     * @return {@code true} if the email matches the
     *         organiser's email
     */
    public boolean checkCreatedByEP(String epEmail) {
        if (epEmail == null) {
            return false;
        }
        return event.getOrganiserEmail().equals(epEmail);
    }

    /**
     * Cancels this performance by setting its status
     * to {@link PerformanceStatus#CANCELLED}.
     */
    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }


    /**
     * Checks whether this performance has any active
     * bookings.
     *
     * @return {@code true} if at least one booking has
     *         status {@link BookingStatus#ACTIVE}
     */
    public boolean hasActiveBookings() {
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a formatted string containing the booking
     * details needed for processing refunds during
     * performance cancellation.
     *
     * @return a string with refund-relevant booking info
     */
    public String getBookingDetailsForRefund() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Booking Details for Refund ===\n");
        sb.append("Performance ID: ")
            .append(performanceID).append("\n");
        sb.append("Event: ")
            .append(event.getEventTitle()).append("\n");
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                sb.append("  Booking #")
                    .append(b.getBookingNumber())
                    .append(": ")
                    .append(b.getStudentDetails())
                    .append(", Tickets: ")
                    .append(b.getNumTickets())
                    .append(", Amount: £")
                    .append(String.format(
                        "%.2f", b.getTransactionAmount()))
                    .append("\n");
            }
        }
        return sb.toString();
    }

    // =====================================================
    // Booking management
    // =====================================================

    /**
     * Returns an unmodifiable view of the bookings for
     * this performance.
     *
     * @return an unmodifiable list of bookings
     */
    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    /**
     * Adds a booking to this performance.
     *
     * @param booking the booking to add; must not be null
     */
    public void addBooking(Booking booking) {
        assert booking != null
            : "Booking must not be null";
        bookings.add(booking);
    }

    /**
     * Returns all active bookings for this performance.
     *
     * @return a list of bookings with status
     *         {@link BookingStatus#ACTIVE}
     */
    public List<Booking> getActiveBookings() {
        List<Booking> active = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                active.add(b);
            }
        }
        return active;
    }

    /**
     * Returns a string representation of this performance.
     *
     * @return a descriptive string
     */
    @Override
    public String toString() {
        return "Performance{id=" + performanceID
            + ", event='" + event.getEventTitle()
            + "', venue='" + venueAddress
            + "', start=" + startDateTime
            + ", end=" + endDateTime
            + ", price=" + ticketPrice
            + ", ticketsSold=" + numTicketsSold
            + "/" + numTicketsTotal
            + ", status=" + status + '}';
    }
}
