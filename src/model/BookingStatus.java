package model;

/**
 * Enumeration representing the possible statuses of a
 * {@link Booking} within the events app.
 *
 * <p>A booking starts as {@link #ACTIVE} and may move to
 * one of the cancelled or failed states depending on
 * what triggered the status change.</p>
 */
public enum BookingStatus {
    /** The booking is confirmed and active. */
    ACTIVE,
    /** The booking was cancelled by the student. */
    CANCELLEDBYSTUDENT,
    /** The booking was cancelled by the provider. */
    CANCELLEDBYPROVIDER,
    /** The payment for this booking failed. */
    PAYMENTFAILED
}
