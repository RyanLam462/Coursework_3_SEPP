package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestPerformance {

    private Performance performance;
    private Event event;
    private EntertainmentProvider ep;

    @BeforeEach
    void setUp() {
        ep = new EntertainmentProvider(
                "ep@company.com", "password123",
                "Events Ltd", "1234567890",
                "John Smith", "We run events");

        event = new Event(1, "Rock Concert", EventType.Music,
                true, ep);

        performance = new Performance(1, event,
                LocalDateTime.of(2026, 6, 15, 19, 0),
                LocalDateTime.of(2026, 6, 15, 22, 0),
                List.of("Band A", "Band B"),
                "Main Hall, Edinburgh",
                500, false, false, 100, 25.0);
    }

    // ============ checkIfEventIsTicketed tests ============

    // Test that checkIfEventIsTicketed returns true when event is ticketed
    @Test
    void checkIfEventIsTicketedReturnsTrueWhenTicketed() {
        assertTrue(performance.checkIfEventIsTicketed(),
                "checkIfEventIsTicketed should return true when event is ticketed");
    }

    // Test that checkIfEventIsTicketed returns false when event is not ticketed
    @Test
    void checkIfEventIsTicketedReturnsFalseWhenNotTicketed() {
        Event nonTicketedEvent = new Event(2, "Free Show",
                EventType.Music, false, ep);
        Performance freePerformance = new Performance(2, nonTicketedEvent,
                LocalDateTime.of(2026, 6, 15, 19, 0),
                LocalDateTime.of(2026, 6, 15, 22, 0),
                List.of("Band A"), "Main Hall", 500,
                false, false, 100, 0.0);
        assertFalse(freePerformance.checkIfEventIsTicketed(),
                "checkIfEventIsTicketed should return false when event is not ticketed");
    }

// ============ checkTicketsLeft tests ============

    // Test that checkTicketsLeft returns true when requesting fewer than available
    @Test
    void checkTicketsLeftFewerThanAvailableReturnsTrue() {
        boolean result = performance.checkTicketsLeft(50);
        assertTrue(result,
                "checkTicketsLeft should return true when requesting fewer than available");
    }

    // Test that checkTicketsLeft returns true when requesting exactly the number available (boundary)
    @Test
    void checkTicketsLeftExactlyAvailableReturnsTrue() {
        boolean result = performance.checkTicketsLeft(100);
        assertTrue(result,
                "checkTicketsLeft should return true when requesting exactly the number available");
    }

    // Test that checkTicketsLeft returns false when requesting more than available
    @Test
    void checkTicketsLeftMoreThanAvailableReturnsFalse() {
        boolean result = performance.checkTicketsLeft(101);
        assertFalse(result,
                "checkTicketsLeft should return false when requesting more than available");
    }

    // Test that checkTicketsLeft returns true when requesting 0 tickets
    @Test
    void checkTicketsLeftZeroTicketsReturnsTrue() {
        boolean result = performance.checkTicketsLeft(0);
        assertTrue(result,
                "checkTicketsLeft should return true when requesting 0 tickets");
    }

    // Test that checkTicketsLeft returns true when requesting negative tickets
    @Test
    void checkTicketsLeftNegativeTicketsReturnsTrue() {
        boolean result = performance.checkTicketsLeft(-1);
        assertTrue(result,
                "checkTicketsLeft should return true when requesting negative tickets");
    }

    // Test that checkTicketsLeft accounts for already sold tickets
    @Test
    void checkTicketsLeftAccountsForSoldTickets() {
        performance.setNumTicketsSold(90);
        boolean result = performance.checkTicketsLeft(11);
        assertFalse(result,
                "checkTicketsLeft should return false when sold tickets reduce availability");
    }

    // Test that checkTicketsLeft boundary when some tickets sold
    @Test
    void checkTicketsLeftExactlyRemainingAfterSalesReturnsTrue() {
        performance.setNumTicketsSold(90);
        boolean result = performance.checkTicketsLeft(10);
        assertTrue(result,
                "checkTicketsLeft should return true when requesting exactly the remaining tickets");
    }

// ============ checkHasNotHappenedYet tests ============

    // Test that checkHasNotHappenedYet returns true when start time is in the future
    @Test
    void checkHasNotHappenedYetReturnsTrueWhenInFuture() {
        assertTrue(performance.checkHasNotHappenedYet(),
                "checkHasNotHappenedYet should return true when performance is in the future");
    }

    // Test that checkHasNotHappenedYet returns false when start time is in the past
    @Test
    void checkHasNotHappenedYetReturnsFalseWhenInPast() {
        Performance pastPerformance = new Performance(3, event,
                LocalDateTime.of(2020, 1, 1, 19, 0),
                LocalDateTime.of(2020, 1, 1, 22, 0),
                List.of("Band A"), "Main Hall", 500,
                false, false, 100, 25.0);
        assertFalse(pastPerformance.checkHasNotHappenedYet(),
                "checkHasNotHappenedYet should return false when performance is in the past");
    }

// ============ checkCreatedByEP tests ============

    // Test that checkCreatedByEP returns true when email matches
    @Test
    void checkCreatedByEPMatchingEmailReturnsTrue() {
        assertTrue(performance.checkCreatedByEP("ep@company.com"),
                "checkCreatedByEP should return true when email matches organiser");
    }

    // Test that checkCreatedByEP returns false when email does not match
    @Test
    void checkCreatedByEPNonMatchingEmailReturnsFalse() {
        assertFalse(performance.checkCreatedByEP("other@company.com"),
                "checkCreatedByEP should return false when email does not match organiser");
    }

    // Test that checkCreatedByEP returns false when email is null
    @Test
    void checkCreatedByEPNullEmailReturnsFalse() {
        assertFalse(performance.checkCreatedByEP(null),
                "checkCreatedByEP should return false when email is null");
    }

    // Test that checkCreatedByEP returns false when email is empty string
    @Test
    void checkCreatedByEPEmptyEmailReturnsFalse() {
        assertFalse(performance.checkCreatedByEP(""),
                "checkCreatedByEP should return false when email is empty string");
    }

// ============ cancel tests ============

    // Test that status is ACTIVE before calling cancel
    @Test
    void statusIsActiveByDefault() {
        assertEquals(PerformanceStatus.ACTIVE, performance.getStatus(),
                "Status should be ACTIVE by default");
    }

    // Test that cancel sets status to CANCELLED
    @Test
    void cancelSetsStatusToCancelled() {
        performance.cancel();
        assertEquals(PerformanceStatus.CANCELLED, performance.getStatus(),
                "Status should be CANCELLED after calling cancel");
    }

// ============ hasActiveBookings tests ============

    // Test that hasActiveBookings returns false when no bookings exist
    @Test
    void hasActiveBookingsReturnsFalseWhenNoBookings() {
        assertFalse(performance.hasActiveBookings(),
                "hasActiveBookings should return false when no bookings exist");
    }

    // Test that hasActiveBookings returns true when there is an active booking
    @Test
    void hasActiveBookingsReturnsTrueWithActiveBooking() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        performance.addBooking(booking);
        assertTrue(performance.hasActiveBookings(),
                "hasActiveBookings should return true when an active booking exists");
    }

    // Test that hasActiveBookings returns false when only cancelled bookings exist
    @Test
    void hasActiveBookingsReturnsFalseWhenOnlyCancelledBookings() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        booking.cancelByStudent();
        performance.addBooking(booking);
        assertFalse(performance.hasActiveBookings(),
                "hasActiveBookings should return false when only cancelled bookings exist");
    }

    // Test that hasActiveBookings returns true when mix of active and cancelled bookings
    @Test
    void hasActiveBookingsReturnsTrueWithMixedBookings() {
        Student student1 = new Student("s1@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Student student2 = new Student("s2@ed.ac.uk", "pass123",
                "Bob", 987654321);
        Booking booking1 = new Booking(1, student1, performance,
                2, 50.0);
        Booking booking2 = new Booking(2, student2, performance,
                1, 25.0);
        booking1.cancelByStudent();
        performance.addBooking(booking1);
        performance.addBooking(booking2);
        assertTrue(performance.hasActiveBookings(),
                "hasActiveBookings should return true when at least one active booking exists");
    }

// ============ getActiveBookings tests ============

    // Test that getActiveBookings returns empty list when no bookings exist
    @Test
    void getActiveBookingsEmptyWhenNoBookings() {
        assertEquals(0, performance.getActiveBookings().size(),
                "getActiveBookings should return empty list when no bookings exist");
    }

    // Test that getActiveBookings returns list with one element when one active booking
    @Test
    void getActiveBookingsReturnsOneWhenOneActiveBooking() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        performance.addBooking(booking);
        assertEquals(1, performance.getActiveBookings().size(),
                "getActiveBookings should return list of size 1 with one active booking");
    }

    // Test that getActiveBookings returns empty list when only cancelled bookings
    @Test
    void getActiveBookingsEmptyWhenOnlyCancelledBookings() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        booking.cancelByStudent();
        performance.addBooking(booking);
        assertEquals(0, performance.getActiveBookings().size(),
                "getActiveBookings should return empty list when only cancelled bookings exist");
    }

    // Test that getActiveBookings returns only active bookings when mixed
    @Test
    void getActiveBookingsReturnsOnlyActiveWhenMixed() {
        Student student1 = new Student("s1@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Student student2 = new Student("s2@ed.ac.uk", "pass123",
                "Bob", 987654321);
        Booking booking1 = new Booking(1, student1, performance,
                2, 50.0);
        Booking booking2 = new Booking(2, student2, performance,
                1, 25.0);
        booking1.cancelByStudent();
        performance.addBooking(booking1);
        performance.addBooking(booking2);
        assertEquals(1, performance.getActiveBookings().size(),
                "getActiveBookings should return only active bookings");
    }

// ============ addBooking tests ============

    // Test that addBooking adds one booking to the list
    @Test
    void addBookingAddsOneBooking() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        performance.addBooking(booking);
        assertEquals(1, performance.getBookings().size(),
                "getBookings should return list of size 1 after adding one booking");
    }

    // Test that addBooking adds two bookings to the list
    @Test
    void addBookingAddsTwoBookings() {
        Student student1 = new Student("s1@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Student student2 = new Student("s2@ed.ac.uk", "pass123",
                "Bob", 987654321);
        Booking booking1 = new Booking(1, student1, performance,
                2, 50.0);
        Booking booking2 = new Booking(2, student2, performance,
                1, 25.0);
        performance.addBooking(booking1);
        performance.addBooking(booking2);
        assertEquals(2, performance.getBookings().size(),
                "getBookings should return list of size 2 after adding two bookings");
    }

// ============ getBookingDetailsForRefund tests ============

    // Test that getBookingDetailsForRefund contains header info when no bookings
    @Test
    void getBookingDetailsForRefundContainsHeaderWhenNoBookings() {
        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("Booking Details for Refund"),
                "getBookingDetailsForRefund should contain header text");
    }

    // Test that getBookingDetailsForRefund contains active booking details
    @Test
    void getBookingDetailsForRefundContainsActiveBookingDetails() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        performance.addBooking(booking);
        String details = performance.getBookingDetailsForRefund();
        assertTrue(details.contains("Alice"),
                "getBookingDetailsForRefund should contain student name for active booking");
    }

    // Test that getBookingDetailsForRefund does not contain cancelled booking details
    @Test
    void getBookingDetailsForRefundExcludesCancelledBookings() {
        Student student = new Student("s@ed.ac.uk", "pass123",
                "Alice", 123456789);
        Booking booking = new Booking(1, student, performance,
                2, 50.0);
        booking.cancelByStudent();
        performance.addBooking(booking);
        String details = performance.getBookingDetailsForRefund();
        assertFalse(details.contains("Alice"),
                "getBookingDetailsForRefund should not contain cancelled booking details");
    }

// ============ getFinalTicketPrice tests ============

    // Test that getFinalTicketPrice returns the correct price
    @Test
    void getFinalTicketPriceReturnsCorrectPrice() {
        assertEquals(25.0, performance.getFinalTicketPrice(),
                "getFinalTicketPrice should return the ticket price");
    }

// ============ getOrganiserEmail tests ============

    // Test that getOrganiserEmail returns the EP's email
    @Test
    void getOrganiserEmailReturnsCorrectEmail() {
        assertEquals("ep@company.com", performance.getOrganiserEmail(),
                "getOrganiserEmail should return the EP's email");
    }

// ============ getEventTitle tests ============

    // Test that getEventTitle returns the correct event title
    @Test
    void getEventTitleReturnsCorrectTitle() {
        assertEquals("Rock Concert", performance.getEventTitle(),
                "getEventTitle should return the event's title");
    }

}