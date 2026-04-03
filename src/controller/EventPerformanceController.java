package controller;

import model.Booking;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;
import model.Performance;
import model.PerformanceStatus;
import model.Student;
import view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import external.PaymentSystem;

/**
 * Controller responsible for event and performance-related
 * operations in the events app.
 *
 * <p>
 * Handles creating events with performances, searching
 * for performances, viewing individual performance details,
 * cancelling performances (EP only). Interacts with
 * performances (admin only). Maintains the global
 * collections of events and performances.
 * </p>
 */
public class EventPerformanceController extends Controller {

    /**
     * getEventByID() and getEventByTitle() were removed from
     * EventPerformanceController as no implemented use case requires looking up
     * events by ID or title on the controller. Events are accessed through their
     * Performance objects (via getEvent()) or through the
     * EntertainmentProvider's event list. If a future use case required direct
     * event lookup (e.g. an admin searching events), these methods would be
     * re-added.
     */

    private long nextEventID;
    private long nextPerformanceID;
    private final List<Performance> performances;
    private final List<Event> events;
    private final PaymentSystem paymentSystem;

    /**
     * Date-time format used for parsing user input.
     * Example: 2026-04-15T19:30
     */
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Constructs a new {@code EventPerformanceController}.
     *
     * @param view          the view for user interaction
     * @param performances  the shared performance list
     * @param paymentSystem the shared payment system
     */

    public EventPerformanceController(View view, List<Performance> performances, PaymentSystem paymentSystem) {
        super(view);
        this.performances = performances;
        this.paymentSystem = paymentSystem;
        this.events = new ArrayList<>();
        this.nextEventID = 1;
        this.nextPerformanceID = 1;
    }

    // Load preregistered data for testing
    public void loadPreregisteredData(EntertainmentProvider ep) {
        Event e1 = new Event(nextEventID++, "Comedy Night", EventType.Theatre, true, ep);
        addEvent(e1);
        ep.addEvent(e1);

        Performance p1 = e1.createPerformance(nextPerformanceID++, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(2), java.util.List.of("Comedian A"), "Main Hall", 100, false,
                false, 50, 10.0);
        addPerformance(p1);

        Event e2 = new Event(nextEventID++, "Music Fest", EventType.Music, true, ep);
        addEvent(e2);
        ep.addEvent(e2);

        Performance p2 = e2.createPerformance(nextPerformanceID++, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(4), java.util.List.of("Band X", "DJ Y"), "Outdoor Arena", 500,
                true, true, 200, 25.0);
        addPerformance(p2);
    }

    private void addEvent(Event e) {
        assert e != null : "Event must not be null";
        events.add(e);
    }

    private void addPerformance(Performance p) {
        assert p != null : "Performance must not be null";
        performances.add(p);
    }

    private Performance getPerformanceByID(long performanceID) {
        for (Performance p : performances) {
            if (p.getID() == performanceID) {
                return p;
            }
        }
        return null;
    }

    /**
     * Handles the create event use case.
     *
     * <p>
     * Only an {@link EntertainmentProvider} may create
     * events. The EP is prompted for the event title,
     * type, whether it is ticketed, and then for one or
     * more performance details (venue, dates, ticket
     * price, total tickets).
     * </p>
     *
     * <p>
     * <strong>Validation:</strong> checks that the
     * current user is an EP, that all inputs are valid,
     * that end dates are after start dates, and that
     * numeric values are non-negative.
     * </p>
     */
    public Event createEvent() {
        if (!checkCurrentUserIsEntertainmentProvider()) {
            view.displayError("Only entertainment providers can create events.");
            return null;
        }

        EntertainmentProvider ep = (EntertainmentProvider) currentUser;

        String title = view.getInput("Enter event title: ");
        if (title == null || title.isBlank()) {
            view.displayError("Event title must not be empty.");
            return null;
        }

        // Check duplicate title for this EP
        for (Event existing : ep.getEvents()) {
            if (existing.getEventTitle().equalsIgnoreCase(title)) {
                view.displayError("You already have an event with this title.");
                return null;
            }
        }

        // Event type
        view.displayInfo("Event types: 1 = Music, 2 = Theatre, 3 = Dance, 4 = Movie, 5 = Sports");
        String typeInput = view.getInput("Enter event type (1-5): ");
        EventType type;
        try {
            int typeChoice = Integer.parseInt(typeInput);
            type = switch (typeChoice) {
                case 1 -> EventType.Music;
                case 2 -> EventType.Theatre;
                case 3 -> EventType.Dance;
                case 4 -> EventType.Movie;
                case 5 -> EventType.Sports;
                default -> null;
            };
            if (type == null) {
                view.displayError("Invalid event type choice.");
                return null;
            }
        } catch (NumberFormatException e) {
            view.displayError("Invalid input for event type.");
            return null;
        }

        String ticketedInput = view.getInput("Is the event ticketed? (yes/no): ");
        boolean isTicketed;
        if ("yes".equalsIgnoreCase(ticketedInput)) {
            isTicketed = true;
        } else if ("no".equalsIgnoreCase(ticketedInput)) {
            isTicketed = false;
        } else {
            view.displayError("Invalid input. Enter 'yes' or 'no'.");
            return null;
        }

        // Create the event object
        long eventID = nextEventID++;
        Event event = new Event(eventID, title, type, isTicketed, ep);
        addEvent(event);
        ep.addEvent(event);

        // Add performances
        boolean addingPerformances = true;
        while (addingPerformances) {
            Performance perf = promptForPerformance(event, isTicketed);
            if (perf != null) {
                addPerformance(perf);
            }

            String more = view.getInput("Add another performance? (yes/no): ");
            addingPerformances = "yes".equalsIgnoreCase(more);
        }

        view.displaySuccess("Event '" + title + "' created successfully with ID " + eventID + ".");
        return event;
    }

    /**
     * Prompts the user for details of a single performance
     * and constructs the {@link Performance} object.
     *
     * @param event      the parent event
     * @param isTicketed whether the event is ticketed
     * @return the created {@link Performance}, or null if input was invalid
     */
    private Performance promptForPerformance(Event event, boolean isTicketed) {

        String venueAddress = view.getInput("Enter venue address: ");
        if (venueAddress == null || venueAddress.isBlank()) {
            view.displayError("Venue address must not be empty.");
            return null;
        }

        String capacityInput = view.getInput("Enter venue capacity: ");
        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(capacityInput);
            if (venueCapacity < 0) {
                view.displayError("Venue capacity cannot be negative.");
                return null;
            }
        } catch (NumberFormatException e) {
            view.displayError("Invalid input for venue capacity.");
            return null;
        }

        String outdoorsInput = view.getInput("Is the venue outdoors? (yes/no): ");
        boolean venueIsOutdoors;
        if ("yes".equalsIgnoreCase(outdoorsInput)) {
            venueIsOutdoors = true;
        } else if ("no".equalsIgnoreCase(outdoorsInput)) {
            venueIsOutdoors = false;
        } else {
            view.displayError("Invalid input. Enter 'yes' or 'no'.");
            return null;
        }

        String smokingInput = view.getInput("Does the venue allow smoking? (yes/no): ");
        boolean venueAllowsSmoking;
        if ("yes".equalsIgnoreCase(smokingInput)) {
            venueAllowsSmoking = true;
        } else if ("no".equalsIgnoreCase(smokingInput)) {
            venueAllowsSmoking = false;
        } else {
            view.displayError("Invalid input. Enter 'yes' or 'no'.");
            return null;
        }

        String performersInput = view.getInput("Enter performer names (comma-separated): ");
        Collection<String> performerNames = new ArrayList<>();
        if (performersInput != null && !performersInput.isBlank()) {
            String[] names = performersInput.split(",");
            for (String name : names) {
                String trimmed = name.trim();
                if (!trimmed.isEmpty()) {
                    performerNames.add(trimmed);
                }
            }
        }

        String startInput = view.getInput("Enter start date/time (yyyy-MM-ddTHH:mm): ");
        LocalDateTime startDT;
        try {
            startDT = LocalDateTime.parse(startInput, DT_FORMAT);
        } catch (DateTimeParseException e) {
            view.displayError("Invalid date/time format. Use yyyy-MM-ddTHH:mm.");
            return null;
        }

        String endInput = view.getInput("Enter end date/time (yyyy-MM-ddTHH:mm): ");
        LocalDateTime endDT;
        try {
            endDT = LocalDateTime.parse(endInput, DT_FORMAT);
        } catch (DateTimeParseException e) {
            view.displayError("Invalid date/time format. Use yyyy-MM-ddTHH:mm.");
            return null;
        }
        if (!endDT.isAfter(startDT)) {
            view.displayError("End date/time must be after start.");
            return null;
        }

        if (startDT.isBefore(LocalDateTime.now())) {
            view.displayError("Start date/time must be in the future.");
            return null;
        }

        if (event.hasPerformanceAtSameTimes(startDT, endDT)) {
            view.displayError("This event already has a performance at overlapping times.");
            return null;
        }

        double ticketPrice = 0;
        int numTickets = 0;

        if (isTicketed) {
            String priceInput = view.getInput("Enter ticket price (GBP): ");
            try {
                ticketPrice = Double.parseDouble(priceInput);
                if (ticketPrice < 0) {
                    view.displayError("Ticket price cannot be negative.");
                    return null;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid input for ticket price.");
                return null;
            }

            String ticketsInput = view.getInput("Enter total number of tickets: ");
            try {
                numTickets = Integer.parseInt(ticketsInput);
                if (numTickets <= 0) {
                    view.displayError("Number of tickets must be positive.");
                    return null;
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid input for ticket count.");
                return null;
            }
        }

        long perfID = nextPerformanceID++;
        Performance performance = event.createPerformance(perfID, startDT, endDT, performerNames, venueAddress,
                venueCapacity, venueIsOutdoors, venueAllowsSmoking, numTickets, ticketPrice);

        view.displayInfo("Performance " + perfID + " added at " + venueAddress + ".");
        return performance;
    }

    /**
     * Handles the search for performances use case.
     *
     * <p>
     * Displays all active performances in the system.
     * Any user (guest, student, EP, admin) can search.
     * </p>
     */
    public void searchForPerformances() {
        if (performances.isEmpty()) {
            view.displayInfo("No performances found in the system.");
            return;
        }

        view.displayInfo("=== Available Performances ===");
        int count = 0;
        for (Performance p : performances) {
            if (p.getStatus() == PerformanceStatus.ACTIVE) {
                view.displayInfo(p.toString());
                count++;
            }
        }

        if (count == 0) {
            view.displayInfo("No active performances found.");
        } else {
            view.displayInfo("Total active performances: " + count);
        }
    }

    /**
     * Handles the view performance use case.
     *
     * <p>
     * Prompts the user for a performance ID and
     * displays its full details. Any user can view
     * a performance.
     * </p>
     */
    public void viewPerformance() {
        String idInput = view.getInput("Enter performance ID to view: ");
        long performanceID;
        try {
            performanceID = Long.parseLong(idInput);
        } catch (NumberFormatException e) {
            view.displayError("Invalid performance ID.");
            return;
        }

        Performance p = getPerformanceByID(performanceID);
        if (p == null) {
            view.displayError("Performance with given number does not exist.");
            return;
        }

        // Display full performance details
        Event event = p.getEvent();
        view.displayInfo("=== Performance Details ===");
        view.displayInfo("Performance ID: " + p.getID());
        view.displayInfo("Event: " + event.getEventTitle() + " (" + event.getType() + ")");
        view.displayInfo("Organiser: " + event.getOrganiser().getOrgName());
        view.displayInfo("Venue: " + p.getVenueAddress());
        view.displayInfo("Start: " + p.getStartDateTime());
        view.displayInfo("End: " + p.getEndDateTime());
        view.displayInfo("Ticketed: " + event.getIsTicketed());

        if (event.getIsTicketed()) {
            view.displayInfo("Ticket Price: £" + String.format("%.2f", p.getTicketPrice()));
            view.displayInfo("Tickets Available: " + (p.getNumTicketsTotal() - p.getNumTicketsSold()) + "/"
                    + p.getNumTicketsTotal());
        }

        view.displayInfo("Status: " + p.getStatus());
    }

    /**
     * Handles the cancel performance use case.
     *
     * <p>
     * Only the {@link EntertainmentProvider} who
     * created the performance may cancel it. The EP is
     * prompted for the performance ID and a cancellation
     * message for affected students. All active bookings
     * are refunded through the payment system and marked
     * as cancelled by provider.
     * </p>
     *
     * <p>
     * <strong>Validation:</strong> checks that the
     * performance exists, belongs to the current EP, and
     * has not already happened.
     * </p>
     *
     */
    public void cancelPerformance() {
        // Guard: must be an EP
        if (!checkCurrentUserIsEntertainmentProvider()) {
            view.displayError("Only entertainment providers can cancel performances.");
            return;
        }

        EntertainmentProvider ep = (EntertainmentProvider) currentUser;
        Performance performance = null;
        boolean sameEP = false;
        boolean hasNotHappenedYet = false;

        while (performance == null || !sameEP || !hasNotHappenedYet) {
            String idInput = view.getInput("Enter ID of performance to cancel: ");
            try {
                long performanceID = Long.parseLong(idInput);
                performance = getPerformanceByID(performanceID);
                if (performance == null) {
                    view.displayError("Performance with given number does not exist.");
                } else {
                    sameEP = performance.checkCreatedByEP(ep.getEmail());
                    if (!sameEP) {
                        view.displayError("The performance with given number does not belong to you.");
                    } else {
                        hasNotHappenedYet = performance.checkHasNotHappenedYet();
                        if (!hasNotHappenedYet) {
                            view.displayError("Performance can't be cancelled as it has already happened.");
                        }
                    }
                }
            } catch (NumberFormatException e) {
                view.displayError("Invalid performance ID.");
                performance = null; // reset to ensure clean loop check
            }
        }

        // Get organiser message for affected students
        // We also use isBlank() to check for empty strings instead of using null
        String organiserMessage = "";
        while (organiserMessage.isBlank()) {
            organiserMessage = view.getInput("Provide a cancellation message for affected students: ");
            if (organiserMessage.isBlank()) {
                view.displayError("Please provide a non-empty message for the students.");
            }
        }

        // Process refunds for all active bookings
        List<Booking> activeBookings = performance.getActiveBookings();

        if (!activeBookings.isEmpty()) {
            Event event = performance.getEvent();
            String eventTitle = event.getEventTitle();
            String epEmail = ep.getEmail();

            for (Booking booking : activeBookings) {
                // Gather student details for refund
                Student student = booking.getStudent();
                String studentEmail = student.getEmail();
                int studentPhone = student.getPhoneNumber();
                int numTickets = booking.getNumTickets();
                double transactionAmount = booking.getTransactionAmount();

                // Process refund through payment system
                boolean refundSuccess = paymentSystem.processRefund(numTickets, eventTitle,
                        studentEmail, studentPhone, epEmail, transactionAmount, organiserMessage);

                if (!refundSuccess) {
                    view.displayError("There was an issue with a refund. The performance cannot be cancelled.");
                    return;
                }

                booking.cancelByProvider();
            }
        }
        performance.cancel();
        performance.setStatus(PerformanceStatus.CANCELLED);
        view.displaySuccess("Cancellation Successful!");
    }

}

/**
 * The cancel performance sequence diagram prescribes that booking details for
 * refunds are returned as a concatenated string via
 * getBookingDetailsForRefund(),
 * which the controller then parses. We deviated from this by accessing Booking
 * and
 * Student objects directly through performance.getActiveBookings(). This avoids
 * string parsing fragility and maintains proper encapsulation — the controller
 * works with typed domain objects rather than raw strings, which is more
 * reliable and consistent with OO design principles (Information Expert, high
 * cohesion). To update the diagram, we would replace the
 * getBookingDetailsForRefund()
 * string return with direct navigation from Performance to its Booking objects,
 * each providing typed accessors for student details and transaction amounts.
 */
