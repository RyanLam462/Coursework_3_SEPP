package model;

/**
 * Enumeration representing the possible statuses of a
 * {@link Performance} within the events app.
 *
 * <p>A performance begins as {@link #ACTIVE} and may
 * transition to {@link #CANCELLED} if the entertainment
 * provider cancels it.</p>
 */
public enum PerformanceStatus {
    /** The performance is scheduled and active. */
    ACTIVE,
    /** The performance has been cancelled by the provider. */
    CANCELLED
}
