package model;

/**
 * Represents an administrative staff member in the university events app.
 *
 * <p>
 * Admin staff can search for and view performances, Admin staff accounts are
 * pre-registered in the system.
 * </p>
 */
public class AdminStaff extends User {
    private String name;

    /**
     * Constructs a new {@code AdminStaff} with the given credentials.
     *
     * @param email    the admin's email address
     * @param password the admin's password
     * @param name     the admin's full name
     */
    public AdminStaff(String email, String password, String name) {
        super(email, password);
        assert name != null && !name.isBlank() : "Admin name must not be null or blank";
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        assert name != null && !name.isBlank() : "Name must not be null or blank";
        this.name = name;
    }

    @Override
    public String toString() {
        return "AdminStaff{name='" + name + "', email='" + getEmail() + "'}";
    }
}
