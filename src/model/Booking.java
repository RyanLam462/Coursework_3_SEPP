package model;

import java.time.LocalDateTime;

/**
 * Represents a booking made by a {@link Student} for a
 * particular {@link Performance} in the events app.
 *
 * <p>A booking records the number of tickets purchased,
 * the transaction amount, and the current status of the
 * booking. Bookings may be cancelled by the student or
 * the entertainment provider, or may fail due to payment
 * issues.</p>
 */
public class Booking {

    /** The unique booking number. */
    private final long bookingNumber;

    /** The student who made this booking. */
    private final Student student;

    /** The performance that was booked. */
    private final Performance performance;

    /** The number of tickets purchased. */
    private final int numTickets;

    /**
     * The total transaction amount in GBP at the time
     * of booking.
     */
    private final double transactionAmount;

    /**
     * The date and time the booking was created.
     */
    private final LocalDateTime bookingDateTime;

    /** The current status of this booking. */
    private BookingStatus status;

    /**
     * Constructs a new {@code Booking}.
     *
     * @param bookingNumber     the unique booking number
     * @param student           the student making the
     *                          booking; must not be null
     * @param performance       the performance being booked;
     *                          must not be null
     * @param numTickets        the number of tickets;
     *                          must be positive
     * @param transactionAmount the total cost in GBP;
     *                          must be non-negative
     */
    public Booking(long bookingNumber, Student student,
                   Performance performance, int numTickets,
                   double transactionAmount) {
        assert bookingNumber >= 0
            : "Booking number must be non-negative";
        assert student != null
            : "Student must not be null";
        assert performance != null
            : "Performance must not be null";
        assert numTickets > 0
            : "Number of tickets must be positive";
        assert transactionAmount >= 0
            : "Transaction amount must be non-negative";

        this.bookingNumber = bookingNumber;
        this.student = student;
        this.performance = performance;
        this.numTickets = numTickets;
        this.transactionAmount = transactionAmount;
        this.bookingDateTime = LocalDateTime.now();
        this.status = BookingStatus.ACTIVE;
    }

    // =====================================================
    // Getters
    // =====================================================

    /**
     * Returns the booking number.
     *
     * @return the unique booking number
     */
    public long getBookingNumber() {
        return bookingNumber;
    }

    /**
     * Returns the student who made this booking.
     *
     * @return the {@link Student}
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the booked performance.
     *
     * @return the {@link Performance}
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Returns the number of tickets in this booking.
     *
     * @return the number of tickets purchased
     */
    public int getNumTickets() {
        return numTickets;
    }

    /**
     * Returns the total transaction amount.
     *
     * @return the amount in GBP
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Returns the date and time the booking was created.
     *
     * @return the booking creation {@link LocalDateTime}
     */
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    /**
     * Returns the current booking status.
     *
     * @return the {@link BookingStatus}
     */
    public BookingStatus getStatus() {
        return status;
    }

    // =====================================================
    // Status transitions
    // =====================================================

    /**
     * Sets the booking status.
     *
     * @param status the new status; must not be null
     */
    public void setStatus(BookingStatus status) {
        assert status != null
            : "Status must not be null";
        this.status = status;
    }

    /**
     * Cancels this booking by the student, setting the
     * status to {@link BookingStatus#CANCELLEDBYSTUDENT}.
     */
    public void cancelByStudent() {
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    /**
     * Cancels this booking by the entertainment provider,
     * setting the status to
     * {@link BookingStatus#CANCELLEDBYPROVIDER}.
     */
    public void cancelByProvider() {
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    /**
     * Marks this booking as having a failed payment,
     * setting the status to
     * {@link BookingStatus#PAYMENTFAILED}.
     */
    public void cancelPaymentFailed() {
        this.status = BookingStatus.PAYMENTFAILED;
    }

    // =====================================================
    // Convenience methods
    // =====================================================

    /**
     * Returns the student's email from the associated
     * student.
     *
     * @return the student's email address
     */
    public String getStudentEmail() {
        return student.getEmail();
    }

    /**
     * Returns the student's phone number from the
     * associated student.
     *
     * @return the student's phone number
     */
    public int getStudentPhoneNumber() {
        return student.getPhoneNumber();
    }

    /**
     * Checks whether this booking was made by the student
     * with the given email address.
     *
     * @param email the email to check
     * @return {@code true} if the booking's student has
     *         the given email
     */
    public boolean checkBookedByStudent(String email) {
        if (email == null) {
            return false;
        }
        return student.getEmail()
            .equalsIgnoreCase(email);
    }

    /**
     * Returns a string summarising the student's details
     * for this booking.
     *
     * @return a formatted string with the student's name,
     *         email, and phone number
     */
    public String getStudentDetails() {
        return student.getName()
            + " (" + student.getEmail()
            + ", phone: " + student.getPhoneNumber()
            + ")";
    }

    /**
     * Generates a human-readable booking record string
     * containing all relevant details.
     *
     * <p>The record includes the student's name, contact
     * info, the event and performance details, the number
     * of tickets, the transaction amount, and the booking
     * date/time.</p>
     *
     * @return a formatted booking record string
     */
    public String generateBookingRecord() {
        Event event = performance.getEvent();
        return "=== Booking Record ==="
            + "\nBooking Number: " + bookingNumber
            + "\nStudent: " + student.getName()
            + "\nEmail: " + student.getEmail()
            + "\nPhone: " + student.getPhoneNumber()
            + "\nEvent: " + event.getEventTitle()
            + " (" + event.getType() + ")"
            + "\nPerformance ID: " + performance.getID()
            + "\nVenue: " + performance.getVenueAddress()
            + "\nDate: " + performance.getStartDateTime()
            + " - " + performance.getEndDateTime()
            + "\nTickets: " + numTickets
            + "\nTotal Paid: £"
            + String.format("%.2f", transactionAmount)
            + "\nStatus: " + status
            + "\nBooked at: " + bookingDateTime
            + "\n========================";
    }

    /**
     * Returns a string representation of this booking.
     *
     * @return a descriptive string
     */
    @Override
    public String toString() {
        return "Booking{number=" + bookingNumber
            + ", student=" + student.getEmail()
            + ", performance=" + performance.getID()
            + ", tickets=" + numTickets
            + ", status=" + status + '}';
    }
}
