package model;

import java.time.LocalDateTime;

/**
 * Represents a booking made by a {@link Student} for a
 * particular {@link Performance} in the events app.
 *
 * <p>
 * A booking records the number of tickets purchased,
 * the transaction amount, and the current status of the
 * booking. Bookings may be cancelled by the student or
 * the entertainment provider, or may fail due to payment
 * issues.
 * </p>
 */
public class Booking {
    private final long bookingNumber;
    private final Student student;
    private final Performance performance;
    private final int numTickets;
    private final double transactionAmount;
    private final LocalDateTime bookingDateTime;
    private BookingStatus status;

    /**
     * Constructs a new {@code Booking}.
     *
     * @param bookingNumber     the unique booking number
     * @param student           the student making the booking; must not be null
     * @param performance       the performance being booked; must not be null
     * @param numTickets        the number of tickets; must be positive
     * @param transactionAmount the total cost in GBP; must be non-negative
     */
    public Booking(long bookingNumber, Student student, Performance performance, int numTickets,
            double transactionAmount) {
        assert bookingNumber >= 0 : "Booking number must be non-negative";
        assert student != null : "Student must not be null";
        assert performance != null : "Performance must not be null";
        assert numTickets > 0 : "Number of tickets must be positive";
        assert transactionAmount >= 0 : "Transaction amount must be non-negative";

        this.bookingNumber = bookingNumber;
        this.student = student;
        this.performance = performance;
        this.numTickets = numTickets;
        this.transactionAmount = transactionAmount;
        this.bookingDateTime = LocalDateTime.now();
        this.status = BookingStatus.ACTIVE;
    }

    public long getBookingNumber() {
        return bookingNumber;
    }

    public Student getStudent() {
        return student;
    }

    public Performance getPerformance() {
        return performance;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        assert status != null : "Status must not be null";
        this.status = status;
    }

    public void cancelByStudent() {
        this.status = BookingStatus.CANCELLEDBYSTUDENT;
    }

    public void cancelByProvider() {
        this.status = BookingStatus.CANCELLEDBYPROVIDER;
    }

    public void cancelPaymentFailed() {
        this.status = BookingStatus.PAYMENTFAILED;
    }

    public String getStudentEmail() {
        return student.getEmail();
    }

    public int getStudentPhoneNumber() {
        return student.getPhoneNumber();
    }

    public boolean checkBookedByStudent(String email) {
        if (email == null) {
            return false;
        }
        return student.getEmail().equalsIgnoreCase(email);
    }

    /**
     * Returns a string summarising the student's details for this booking.
     *
     * @return a formatted string with the student's name, email, and phone number
     */
    public String getStudentDetails() {
        return student.getName() + " (" + student.getEmail() + ", phone: " + student.getPhoneNumber() + ")";
    }

    /**
     * Generates a human-readable booking record string
     * containing all relevant details.
     *
     * <p>
     * The record includes the student's name, contact
     * info, the event and performance details, the number
     * of tickets, the transaction amount, and the booking
     * date/time.
     * </p>
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
                + "\nDate: " + performance.getStartDateTime().format(Performance.DATE_TIME_FORMATTER)
                + " - " + performance.getEndDateTime().format(Performance.DATE_TIME_FORMATTER)
                + "\nTickets: " + numTickets
                + "\nTotal Paid: £"
                + String.format("%.2f", transactionAmount)
                + "\nStatus: " + status
                + "\nBooked at: " + bookingDateTime.format(Performance.DATE_TIME_FORMATTER)
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
