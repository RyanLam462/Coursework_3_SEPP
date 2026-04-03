package controller;

import external.PaymentSystem;
import model.Booking;
import model.BookingStatus;
import model.Event;
import model.Performance;
import model.Student;
import view.View;
import model.PerformanceStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for booking-related operations in the events app.
 *
 * <p>
 * Handles booking performances, cancelling bookings,
 * Interacts with the {@link PaymentSystem} for payment processing and refunds.
 * Maintains the global collection of all bookings.
 * </p>
 */

public class BookingController extends Controller {
    /**
     * findBookingsByEventID() was removed from BookingController
     * as it is not used by any of the implemented use cases.
     * The cancel performance use case retrieves bookings directly
     * from the Performance object via getActiveBookings().
     */

    private long nextBookingNumber;
    private final List<Booking> bookings;
    private final List<Performance> performances;
    private final PaymentSystem paymentSystem;

    /**
     * Constructs a new {@code BookingController}.
     *
     * @param view          the view for user interaction
     * @param performances  the shared performance list
     * @param paymentSystem the shared payment system
     */
    public BookingController(View view, List<Performance> performances, PaymentSystem paymentSystem) {
        super(view);
        this.performances = performances;
        this.paymentSystem = paymentSystem;
        this.bookings = new ArrayList<>();
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

    private boolean checkIfBookingPossible(Performance performance, int numTickets) {
        boolean isTicketed = performance.checkIfEventIsTicketed();
        if (!isTicketed) {
            view.displayError("The requested performance's event is not ticketed. There is no need to book it.");
            return false;
        }

        boolean enoughTicketsLeft = performance.checkTicketsLeft(numTickets);
        if (!enoughTicketsLeft) {
            view.displayError("Requested performance has no tickets left.");
            return false;
        }

        return true;
    }

    private void addBooking(Booking b) {
        assert b != null : "Booking must not be null";
        bookings.add(b);
    }

    private Booking getBookingByNumber(long bookingNumber) {
        for (Booking b : bookings) {
            if (b.getBookingNumber() == bookingNumber) {
                return b;
            }
        }
        return null;
    }

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

        while (performance == null || (!possible && isTicketed)) {
            String idInput = view.getInput("Enter performance ID to book (or 'exit' to cancel): ");
            if ("exit".equalsIgnoreCase(idInput)) return;
            
            String ticketsInput = view.getInput("Enter number of tickets (or 'exit' to cancel): ");
            if ("exit".equalsIgnoreCase(ticketsInput)) return;

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
                view.displayError("Invalid input format.");
                performance = null;
            }

            if (performance != null && performance.getStatus() == PerformanceStatus.CANCELLED) {
                view.displayError("Performance is not active.");
                performance = null;
            }
        }

        // Guard: if loop exited because the event was non-ticketed, do not proceed with
        // booking
        if (!possible) {
            return;
        }

        Student student = (Student) getCurrentUser();
        Event event = performance.getEvent();
        double ticketPrice = performance.getFinalTicketPrice();
        double transactionAmount = ticketPrice * numTickets;
        long bookingNum = nextBookingNumber++;
        Booking booking = new Booking(bookingNum, student, performance, numTickets, transactionAmount);

        addBooking(booking);
        performance.addBooking(booking);
        student.addBooking(booking);

        String eventTitle = event.getEventTitle();
        String studentEmail = student.getEmail();
        int studentPhone = student.getPhoneNumber();
        String epEmail = event.getOrganiserEmail();

        boolean paymentSuccessful = paymentSystem.processPayment(numTickets, eventTitle, studentEmail, studentPhone,
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

    /**
     * Our book performace controller very slightly deviates fromt the sequence
     * diagram. For example, we dont use getFinalTicketPrice() to get the final
     * ticket price. Instead, we calculate it ourselves. This is because we need to
     * know the transaction amount to create the booking.
     */

    /**
     * Handles the cancel booking use case.
     *
     * <p>
     * Only a {@link Student} may cancel their own booking. The student is prompted
     * for the booking number. If the associated performance has not yet happened, a
     * refund is processed and the booking is marked as cancelled.
     * </p>
     *
     * <p>
     * <strong>Validation:</strong> checks that the
     * booking exists, belongs to the current student,
     * is active, and the performance hasn't happened.
     * </p>
     */
    public void cancelBooking() {
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
            view.displayError("Booking with given number does not exist.");
            return;
        }

        // Check the booking belongs to this student
        if (!booking.getStudentEmail().equalsIgnoreCase(student.getEmail())) {
            view.displayError("This booking does not belong to you.");
            return;
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            view.displayError("This booking is not active and cannot be cancelled.");
            return;
        }

        Performance performance = booking.getPerformance();
        if (!performance.checkHasNotHappenedYet()) {
            view.displayError("The performance has already happened. The booking cannot be cancelled.");
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
            view.displayError("There was an issue with the refund. The booking cannot be cancelled.");
            return;
        }

        booking.cancelByStudent();
        // Restore the ticket count
        performance.setNumTicketsSold(performance.getNumTicketsSold() - numTickets);
        view.displaySuccess("Booking cancelled successfully. Refund has been processed.");
    }
}