package model;

/**
 * Enumeration representing the different types of events
 * that can be created in the university events app.
 *
 * <p>Each event must be assigned exactly one {@code EventType}
 * when it is created by an {@link EntertainmentProvider}.</p>
 */
public enum EventType {
    /** A music concert or recital. */
    Music,
    /** A theatrical production or play. */
    Theatre,
    /** A dance show or ballet. */
    Dance,
    /** A movie screening. */
    Movie,
    /** A sporting event or match. */
    Sports
}
