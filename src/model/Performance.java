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
 * <p>
 * A performance has a specific date/time window, a
 * venue, a ticket price, and a limited number of tickets.
 * Students book individual performances rather than events
 * directly.
 * </p>
 */
public class Performance {
    private final long performanceID;

    // The event to which this performance belongs.
    private final Event event;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Collection<String> performerNames;
    private String venueAddress;
    private int venueCapacity;
    private boolean venueIsOutdoors;
    private boolean venueAllowsSmoking;
    private int numTicketsTotal;
    private int numTicketsSold;
    private double ticketPrice;
    private PerformanceStatus status;

    // Bookings associated with this performance.
    private final List<Booking> bookings;

    /**
     * Constructs a new {@code Performance}.
     *
     * @param performanceID      the unique performance ID
     * @param event              the parent event; must not be null
     * @param startDateTime      the start date/time; must not be null
     * @param endDateTime        the end date/time; must not be null and must be
     *                           after start
     * @param performerNames     names of the performers
     * @param venueAddress       the venue address; must not be null or blank
     * @param venueCapacity      the capacity of the venue; must be non-negative
     * @param venueIsOutdoors    whether the venue is outdoors
     * @param venueAllowsSmoking whether the venue allows smoking
     * @param numTickets         total tickets available; must be non-negative
     * @param ticketPrice        price per ticket in GBP; must be non-negative
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
        assert performanceID >= 0 : "Performance ID must be non-negative";
        assert event != null : "Event must not be null";
        assert startDateTime != null : "Start date/time must not be null";
        assert endDateTime != null : "End date/time must not be null";
        assert !endDateTime.isBefore(startDateTime) : "End must not be before start";
        assert venueAddress != null && !venueAddress.isBlank() : "Venue address must not be null or blank";
        assert venueCapacity >= 0 : "Venue capacity must be non-negative";
        assert numTickets >= 0 : "Total tickets must be non-negative";
        assert ticketPrice >= 0 : "Ticket price must be non-negative";

        this.performanceID = performanceID;
        this.event = event;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames != null ? new ArrayList<>(performerNames) : new ArrayList<>();
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

    public long getID() {
        return performanceID;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Collection<String> getPerformerNames() {
        return performerNames;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public int getVenueCapacity() {
        return venueCapacity;
    }

    public boolean getVenueIsOutdoors() {
        return venueIsOutdoors;
    }

    public boolean getVenueAllowsSmoking() {
        return venueAllowsSmoking;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public int getNumTicketsTotal() {
        return numTicketsTotal;
    }

    public int getNumTicketsSold() {
        return numTicketsSold;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        assert startDateTime != null : "Start must not be null";
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        assert endDateTime != null : "End must not be null";
        this.endDateTime = endDateTime;
    }

    public void setPerformerNames(Collection<String> performerNames) {
        this.performerNames = performerNames != null ? new ArrayList<>(performerNames) : new ArrayList<>();
    }

    public void setVenueAddress(String venueAddress) {
        assert venueAddress != null && !venueAddress.isBlank() : "Venue address must not be null or blank";
        this.venueAddress = venueAddress;
    }

    public void setVenueCapacity(int venueCapacity) {
        assert venueCapacity >= 0 : "Capacity must be non-negative";
        this.venueCapacity = venueCapacity;
    }

    public void setVenueIsOutdoors(boolean venueIsOutdoors) {
        this.venueIsOutdoors = venueIsOutdoors;
    }

    public void setVenueAllowsSmoking(boolean venueAllowsSmoking) {
        this.venueAllowsSmoking = venueAllowsSmoking;
    }

    public void setTicketPrice(double ticketPrice) {
        assert ticketPrice >= 0 : "Price must be non-negative";
        this.ticketPrice = ticketPrice;
    }

    public void setNumTicketsTotal(int numTicketsTotal) {
        assert numTicketsTotal >= 0 : "Total tickets must be non-negative";
        this.numTicketsTotal = numTicketsTotal;
    }

    public void setNumTicketsSold(int numTicketsSold) {
        assert numTicketsSold >= 0 : "Sold tickets must be non-negative";
        this.numTicketsSold = numTicketsSold;
    }

    public void setStatus(PerformanceStatus status) {
        assert status != null : "Status must not be null";
        this.status = status;
    }

    public boolean checkIfEventIsTicketed() {
        return event.getIsTicketed();
    }

    public boolean checkTicketsLeft(int numTicketsRequested) {
        return (numTicketsTotal - numTicketsSold) >= numTicketsRequested;
    }

    public double getFinalTicketPrice() {
        return ticketPrice;
    }

    public String getOrganiserEmail() {
        return event.getOrganiserEmail();
    }

    public String getEventTitle() {
        return event.getEventTitle();
    }

    // Used by cancelPerformance()
    public boolean checkHasNotHappenedYet() {
        return startDateTime.isAfter(LocalDateTime.now());
    }

    // Used by cancelBooking() 
    public boolean checkIsAtLeast24HoursAway() {
        return startDateTime.isAfter(LocalDateTime.now().plusHours(24));
    }

    /**
     * Checks whether the given EP email matches the
     * organiser of this performance's event.
     *
     * <p>
     * Used to verify that the EP attempting to cancel
     * a performance is the one who created it.
     * </p>
     *
     * @param epEmail the email to check
     * @return {@code true} if the email matches the organiser's email
     */
    public boolean checkCreatedByEP(String epEmail) {
        if (epEmail == null) {
            return false;
        }
        return event.getOrganiserEmail().equals(epEmail);
    }

    public void cancel() {
        this.status = PerformanceStatus.CANCELLED;
    }

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
        sb.append("Performance ID: ").append(performanceID).append("\n");
        sb.append("Event: ").append(event.getEventTitle()).append("\n");
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.ACTIVE) {
                sb.append("  Booking #").append(b.getBookingNumber())
                        .append(": ")
                        .append(b.getStudentDetails())
                        .append(", Tickets: ")
                        .append(b.getNumTickets())
                        .append(", Amount: £")
                        .append(String.format("%.2f", b.getTransactionAmount()))
                        .append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns an unmodifiable view of the bookings for this performance.
     *
     * @return an unmodifiable list of bookings
     */
    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public void addBooking(Booking booking) {
        assert booking != null : "Booking must not be null";
        bookings.add(booking);
    }

    /**
     * Returns all active bookings for this performance.
     *
     * @return a list of bookings with status {@link BookingStatus#ACTIVE}
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
