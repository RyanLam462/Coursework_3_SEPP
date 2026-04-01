package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a student user in the university events app.
 *
 * <p>Students can search for performances, book tickets,
 * cancel bookings, and edit their
 * personal preferences. Students are pre-registered in the
 * system and log in with their email and password.</p>
 */
public class Student extends User {

    /** The student's full name. */
    private String name;

    /** The student's phone number. */
    private int phoneNumber;

    /** The student's customisable preferences. */
    private StudentPreferences preferences;

    /** All bookings this student has made. */
    private final List<Booking> bookings;

    /**
     * Constructs a new {@code Student} with the given details.
     *
     * @param email       the student's email address
     * @param password    the student's password
     * @param name        the student's full name
     * @param phoneNumber the student's phone number
     */
    public Student(String email, String password,
                   String name, int phoneNumber) {
        super(email, password);
        assert name != null && !name.isBlank()
            : "Student name must not be null or blank";
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = new StudentPreferences();
        this.bookings = new ArrayList<>();
    }

    /**
     * Returns the student's full name.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's full name.
     *
     * @param name the new name; must not be null or blank
     */
    public void setName(String name) {
        assert name != null && !name.isBlank()
            : "Name must not be null or blank";
        this.name = name;
    }

    /**
     * Returns the student's phone number.
     *
     * @return the phone number
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the student's phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the student's preferences.
     *
     * @return the {@link StudentPreferences} object
     */
    public StudentPreferences getPreferences() {
        return preferences;
    }

    /**
     * Replaces the student's preferences.
     *
     * @param preferences the new preferences; must not
     *                    be null
     */
    public void setPreferences(StudentPreferences preferences) {
        assert preferences != null
            : "Preferences must not be null";
        this.preferences = preferences;
    }

    /**
     * Returns an unmodifiable view of this student's bookings.
     *
     * @return an unmodifiable list of bookings
     */
    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    /**
     * Adds a booking to this student's booking history.
     *
     * @param booking the booking to add; must not be null
     */
    public void addBooking(Booking booking) {
        assert booking != null : "Booking must not be null";
        bookings.add(booking);
    }

    /**
     * Returns a string representation of the student.
     *
     * @return a string describing this student
     */
    @Override
    public String toString() {
        return "Student{name='" + name
            + "', email='" + getEmail() + "'}";
    }
}
