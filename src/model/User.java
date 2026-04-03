package model;

/**
 * Abstract base class representing a user of the university
 * events app.
 *
 * <p>
 * All user types ({@link Student}, {@link AdminStaff},
 * {@link EntertainmentProvider}) extend this class and
 * inherit common authentication fields such as email
 * and password.
 * </p>
 */
public abstract class User {
    private String email;
    private String password;

    /**
     * Constructs a new {@code User} with the given credentials.
     *
     * @param email    the user's email address; must not be null or empty
     * @param password the user's password; must not be null or empty
     */
    protected User(String email, String password) {
        assert email != null && !email.isBlank() : "User email must not be null or blank";
        assert password != null && !password.isBlank() : "User password must not be null or blank";
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        assert email != null && !email.isBlank() : "Email must not be null or blank";
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        assert password != null && !password.isBlank() : "Password must not be null or blank";
        this.password = password;
    }

    /**
     * Checks whether the given password matches this user's
     * stored password.
     *
     * @param passwordAttempt the password to check
     * @return {@code true} if the passwords match, {@code false} otherwise
     */
    public boolean checkPassword(String passwordAttempt) {
        if (passwordAttempt == null) {
            return false;
        }
        return this.password.equals(passwordAttempt);
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a descriptive string including the user's email
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{email='" + email + "'}";
    }
}
