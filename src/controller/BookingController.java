package controller;

import external.MockPaymentSystem;
import external.PaymentSystem;
import model.Booking;
import model.BookingStatus;
import model.Event;
import model.Performance;
import model.Student;
import view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller responsible for booking-related operations in
 * the events app.
 *
 * <p>
 * Handles booking performances, cancelling bookings,
 * Interacts with the {@link PaymentSystem} for payment processing and refunds.
 * Maintains the global collection of all bookings.
 * </p>
 */
public class BookingController extends Controller {

    private long nextBookingNumber;
    private final List<Performance> performances;
    private final PaymentSystem paymentSystem;

    /**
     * Constructs a new {@code BookingController}.
     *
     * @param view         the view for user interaction
     * @param performances the shared performance list
     */
    public BookingController(View view, List<Performance> performances) {
        super(view);
        assert performances != null : "Performances list must not be null";
        this.performances = performances;
        this.paymentSystem = new MockPaymentSystem();
        this.nextBookingNumber = 1;
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
     * Checks whether a booking is possible for the given
     * performance and number of tickets.
     *
     * <p>
     * Validates that the event is ticketed and that
     * enough tickets remain.
     * </p>
     *
     * @param performance the performance to check
     * @param numTickets  the number of tickets
     *                    requested
     * @return {@code true} if booking is possible
     */
    private boolean checkIfBookingPossible(Performance performance, int numTickets) {

        // Check if the event is ticketed
        boolean isTicketed = performance.checkIfEventIsTicketed();
        if (!isTicketed) {
            view.displayError(
                    "The requested performance's event is " + "not ticketed. There is no need " + "to book it.");
            return false;
        }

        // Check ticket availability
        boolean enoughTicketsLeft = performance.checkTicketsLeft(numTickets);
        if (!enoughTicketsLeft) {
            view.displayError("Requested performance has no " + "tickets left.");
            return false;
        }

        return true;
    }

    /**
     * Adds a booking to the internal collection.
     *
     * @param b the booking to add; must not be null
     */
    private void addBooking(Booking b) {
        assert b != null : "Booking must not be null";
        // Booking is stored on the performance and
        // student objects directly
    }

    /**
     * Finds all bookings for a given event ID by
     * searching across all performances of that event.
     *
     * @param eventID the event ID to search for
     * @return a collection of matching bookings
     */
    private Collection<Booking> findBookingsByEventID(long eventID) {
        List<Booking> result = new ArrayList<>();
        for (Performance p : performances) {
            if (p.getEvent().getEventID() == eventID) {
                result.addAll(p.getBookings());
            }
        }
        return result;
    }

    /**
     * Finds a booking by its unique booking number,
     * searching across all performances.
     *
     * @param bookingNumber the booking number to find
     * @return the matching {@link Booking}, or null
     */
    private Booking getBookingByNumber(long bookingNumber) {
        for (Performance p : performances) {
            for (Booking b : p.getBookings()) {
                if (b.getBookingNumber() == bookingNumber) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * Returns the payment system used by this controller.
     *
     * <p>
     * Exposed so that the
     * {@link EventPerformanceController} can use it for
     * refunds during performance cancellation.
     * </p>
     *
     * @return the {@link PaymentSystem}
     */

    public PaymentSystem getPaymentSystem() {
        return paymentSystem;
    }

    // ==========================================================
    // Use case: Book Performance
    // ==========================================================

    /**
     * Handles the book performance use case.
     *
     * <p>
     * Only a {@link Student} may book a performance.
     * The student is prompted for a performance ID and
     * the number of tickets. The system validates
     * availability, processes payment, creates a booking,
     * and displays a booking record.
     * </p>
     *
     * <p>
     * <strong>Validation:</strong> checks that the
     * performance exists, is active, is ticketed, has
     * enough tickets, the student is logged in, and
     * payment succeeds.
     * </p>
     */

    public void bookPerformance() {
        if (!checkCurrentUserIsStudent()) {
            view.displayError("Only students can book performances.");
            return;
        }

        Performance performance = null;
        boolean possible = false;
        boolean isTicketed = true;
        int numTickets = 0;

        // Exact match to the Sequence Diagram loop condition:
        // loop [while performance == null or (possible==false and isTicketed==true)]
        while (performance == null || (!possible && isTicketed)) {
            String idInput = view.getInput("Enter performance ID to book: ");
            String ticketsInput = view.getInput("Enter number of tickets: ");

            try {
                long performanceID = Long.parseLong(idInput);
                numTickets = Integer.parseInt(ticketsInput);

                if (numTickets <= 0) {
                    view.displayError("Number of tickets must be positive.");
                    continue;
                }

                performance = getPerformanceByID(performanceID);

                if (performance == null) {
                    view.displayError("Performance with given number does not exist.");
                } else {
                    possible = checkIfBookingPossible(performance, numTickets);
                    isTicketed = performance.checkIfEventIsTicketed();
                }

            } catch (NumberFormatException e) {
                view.displayError(
                        "Invalid input format.");
                performance = null;
            }
        }

        // Guard: if loop exited because the event was
        // non-ticketed, do not proceed with booking
        if (!possible) {
            return;
        }

        // --- Start of Post-Loop Sequence Diagram Logic ---
        Student student = (Student) getCurrentUser();
        Event event = performance.getEvent();
        double ticketPrice = performance.getFinalTicketPrice();
        double transactionAmount = ticketPrice * numTickets;

        long bookingNum = nextBookingNumber++;
        Booking booking = new Booking(
                bookingNum, student, performance,
                numTickets, transactionAmount);

        addBooking(booking);
        performance.addBooking(booking);
        student.addBooking(booking);

        String eventTitle = event.getEventTitle();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = event.getOrganiserEmail();

        boolean paymentSuccessful = paymentSystem.processPayment(
                numTickets, eventTitle,
                studentEmail, studentPhone,
                epEmail, transactionAmount);

        if (paymentSuccessful) {
            performance.setNumTicketsSold(performance.getNumTicketsSold() + numTickets);
            view.displaySuccess("Booking Successful!");

            String bookingRecord = booking.generateBookingRecord();
            view.displayBookingRecord(bookingRecord);
        } else {
            view.displayError("There was an issue with payment.");
            booking.cancelPaymentFailed();
        }
    }

    // ==========================================================
    // Use case: Cancel Booking
    // ==========================================================

    /**
     * Handles the cancel booking use case.
     *
     * <p>
     * Only a {@link Student} may cancel their own booking. The student is prompted
     * for the booking number.
     * If the associated performance has not yet happened, a refund is processed and
     * the booking is marked as cancelled.
     * </p>
     *
     * <p>
     * <strong>Validation:</strong> checks that the
     * booking exists, belongs to the current student,
     * is active, and the performance hasn't happened.
     * </p>
     */
    public void cancelBooking() {
        // Guard: must be a student
        if (!checkCurrentUserIsStudent()) {
            view.displayError("Only students can cancel bookings.");
            return;
        }

        Student student = (Student) currentUser;

        String numInput = view.getInput("Enter booking number to cancel: ");
        long bookingNumber;
        try {
            bookingNumber = Long.parseLong(numInput);
        } catch (NumberFormatException e) {
            view.displayError("Invalid booking number.");
            return;
        }

        Booking booking = getBookingByNumber(bookingNumber);
        if (booking == null) {
            view.displayError("Booking with given number " + "does not exist.");
            return;
        }

        // Check the booking belongs to this student
        if (!booking.getStudentEmail().equalsIgnoreCase(student.getEmail())) {
            view.displayError("This booking does not belong to you.");
            return;
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            view.displayError("This booking is not active and " + "cannot be cancelled.");
            return;
        }

        Performance performance = booking.getPerformance();
        if (!performance.checkHasNotHappenedYet()) {
            view.displayError("The performance has already happened. " + "The booking cannot be cancelled.");
            return;
        }

        // Process refund
        Event event = performance.getEvent();
        String eventTitle = event.getEventTitle();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = event.getOrganiserEmail();
        int numTickets = booking.getNumTickets();
        double transactionAmount = booking.getTransactionAmount();

        boolean refundSuccess = paymentSystem.processRefund(numTickets, eventTitle, studentEmail, studentPhone, epEmail,
                transactionAmount, "");

        if (!refundSuccess) {
            view.displayError("There was an issue with the refund. " + "The booking cannot be cancelled.");
            return;
        }

        booking.cancelByStudent();

        // Restore the ticket count
        performance.setNumTicketsSold(performance.getNumTicketsSold() - numTickets);

        view.displaySuccess("Booking cancelled successfully. " + "Refund has been processed.");
    }
}
