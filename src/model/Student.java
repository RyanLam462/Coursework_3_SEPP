package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a student user in the university events app.
 *
 * <p>
 * Students can search for performances, book tickets,
 * cancel bookings, and edit their
 * personal preferences. Students are pre-registered in the
 * system and log in with their email and password.
 * </p>
 */
public class Student extends User {
    private String name;
    private int phoneNumber;
    private StudentPreferences preferences;
    private final List<Booking> bookings;

    /**
     * Constructs a new {@code Student} with the given details.
     *
     * @param email       the student's email address
     * @param password    the student's password
     * @param name        the student's full name
     * @param phoneNumber the student's phone number
     */
    public Student(String email, String password, String name, int phoneNumber) {
        super(email, password);
        assert name != null && !name.isBlank() : "Student name must not be null or blank";
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferences = new StudentPreferences();
        this.bookings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        assert name != null && !name.isBlank() : "Name must not be null or blank";
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StudentPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(StudentPreferences preferences) {
        assert preferences != null : "Preferences must not be null";
        this.preferences = preferences;
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

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
        return "Student{name='" + name + "', email='" + getEmail() + "'}";
    }
}
