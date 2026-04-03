package controller;

import external.MockVerificationService;
import external.VerificationService;
import model.AdminStaff;
import model.EntertainmentProvider;
import model.Student;
import model.StudentPreferences;
import model.User;
import view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for user-related operations in
 * the events app.
 *
 * <p>
 * Handles logging in, logging out, registering new
 * entertainment providers, editing student preferences,
 * and maintaining the collection of all registered users.
 * Pre-registered students and admin staff are added during
 * initialisation.
 * </p>
 */
public class UserController extends Controller {

        public static final String PREREGISTERED_USERS_FILE_PATH = "data/preregistered_users.csv";
        public static final String PREREGISTERED_ADMIN_FILE_PATH = "data/preregistered_admins.csv";
        private final List<User> users;
        private final VerificationService verificationService;

        /**
         * Constructs a new {@code UserController}.
         *
         * @param view the view for user interaction
         */
        public UserController(View view) {
                super(view);
                this.users = new ArrayList<>();
                this.verificationService = new MockVerificationService();
        }

        public void addUser(User user) {
                assert user != null : "User must not be null";
                users.add(user);
        }

        /**
         * Performs basic email format validation.
         *
         * @param email the email to validate
         * @return {@code true} if the email contains '@' and a domain portion
         */
        private boolean isValidEmailFormat(String email) {
                if (email == null || email.isBlank()) {
                        return false;
                }
                int atIndex = email.indexOf('@');
                // Must have @ not at start/end, with a dot
                // in the domain portion
                return atIndex > 0 && atIndex < email.length() - 1 && email.indexOf('.', atIndex) > atIndex + 1;
        }

        /**
         * Loads hardcoded pre-registered students and admin
         * staff into the system.
         *
         * <p>
         * In a real deployment these would be read from
         * the files at {@link #PREREGISTERED_USERS_FILE_PATH}
         * and {@link #PREREGISTERED_ADMIN_FILE_PATH}. For
         * testing purposes, representative accounts are hardcoded here.
         * </p>
         */
        public void addPreregisteredUsers() {
                // Pre-registered students
                users.add(new Student("student1@ed.ac.uk", "password1", "Alice Smith", 447123456));
                users.add(new Student("student2@ed.ac.uk", "password2", "Bob Jones", 447654321));
                users.add(new Student("student3@ed.ac.uk", "password3", "Charlie Brown", 447111222));

                // Pre-registered admin staff
                users.add(new AdminStaff("admin1@ed.ac.uk", "adminPass1", "Admin Alice"));
                users.add(new AdminStaff("admin2@ed.ac.uk", "adminPass2", "Admin Bob"));

                // Pre-registered EP
                EntertainmentProvider ep1 = new EntertainmentProvider("ep1@ed.ac.uk", "password123", "Mock Org",
                                "1234567890", "Mock Person", "Mock Description");
                users.add(ep1);
        }

        public List<User> getUsers() {
                return users;
        }

        /**
         * Handles the login use case.
         *
         * <p>
         * Prompts the user for their email and password,
         * then searches the registered users for a match.
         * If a match is found, sets the current user
         * accordingly.
         * </p>
         *
         * <p>
         * <strong>Validation:</strong> rejects blank email
         * or password, and reports an error if no matching
         * account is found or if the password is incorrect.
         * </p>
         */
        public void login() {
                // Guard: must not already be logged in
                if (!checkCurrentUserIsGuest()) {
                        view.displayError("You are already logged in. Please log out first.");
                        return;
                }

                String email = view.getInput("Enter email: ");
                if (email == null || email.isBlank()) {
                        view.displayError("Email must not be empty.");
                        return;
                }

                if (!isValidEmailFormat(email)) {
                        view.displayError("Invalid email format.");
                        return;
                }

                String password = view.getInput("Enter password: ");
                if (password == null || password.isBlank()) {
                        view.displayError("Password must not be empty.");
                        return;
                }

                // Security: case-insensitive email match prevents
                // user enumeration via case variation
                for (User user : users) {
                        if (user.getEmail().equalsIgnoreCase(email)) {
                                if (user.checkPassword(password)) {
                                        currentUser = user;
                                        view.displaySuccess("Login successful. Welcome, " + email + "!");
                                        return;
                                } else {
                                        view.displayError("Incorrect password.");
                                        return;
                                }
                        }
                }

                view.displayError("No account found with email: " + email);
        }

        /**
         * Handles the logout use case.
         *
         * <p>
         * Clears the currently logged-in user, returning
         * the system to guest mode.
         * </p>
         */
        public void logout() {
                if (checkCurrentUserIsGuest()) {
                        view.displayError("No user is currently logged in.");
                        return;
                }
                String email = currentUser.getEmail();
                currentUser = null;
                view.displaySuccess("Logged out successfully. Goodbye, " + email + "!");
        }

        /**
         * Handles the register entertainment provider use case.
         *
         * <p>
         * Prompts the guest user for their organisation
         * name, business registration number, email, and
         * password. The business number is verified through
         * the {@link VerificationService}. If verification
         * succeeds and no duplicate account exists, the new
         * EP is registered and logged in.
         * </p>
         *
         * <p>
         * <strong>Validation:</strong> checks for blank
         * inputs, duplicate email/org/business number
         * combinations, and verification service results.
         * </p>
         */
        public void registerEntertainmentProvider() {
                // Guard: must be a guest
                if (!checkCurrentUserIsGuest()) {
                        view.displayError("You must log out before registering a new account.");
                        return;
                }

                String email = view.getInput("Enter email: ");
                if (email == null || email.isBlank()) {
                        view.displayError("Email must not be empty.");
                        return;
                }

                if (!isValidEmailFormat(email)) {
                        view.displayError("Invalid email format.");
                        return;
                }

                String password = view.getInput("Enter password: ");
                if (password == null || password.isBlank()) {
                        view.displayError("Password must not be empty.");
                        return;
                }

                String orgName = view.getInput("Enter organisation name: ");
                if (orgName == null || orgName.isBlank()) {
                        view.displayError("Organisation name must not be empty.");
                        return;
                }

                String businessNumber = view.getInput("Enter business registration number: ");
                if (businessNumber == null || businessNumber.isBlank()) {
                        view.displayError("Business registration number must not be empty.");
                        return;
                }

                // Check for duplicate EP account
                if (epAccountAlreadyExists(email, orgName, businessNumber)) {
                        view.displayError(
                                        "An entertainment provider with the same email, organisation name, or business number already exists.");
                        return;
                }

                // Verify through external verification service
                boolean verified = verificationService.verifyEntertainmentProvider(businessNumber);
                if (!verified) {
                        view.displayError("Verification failed. The business registration number is not valid.");
                        return;
                }

                // Create and register the new EP
                String epName = view.getInput("Enter contact person name: ");
                if (epName == null || epName.isBlank()) {
                        epName = "";
                }

                String description = view.getInput("Enter organisation description: ");
                if (description == null || description.isBlank()) {
                        description = "";
                }

                EntertainmentProvider newEP = new EntertainmentProvider(email, password, orgName, businessNumber,
                                epName,
                                description);
                users.add(newEP);

                // Automatically log in the new EP
                currentUser = newEP;
                view.displaySuccess("Entertainment provider registered successfully. Welcome, " + orgName + "!");
        }

        /**
         * Checks whether an EP account with the same email,
         * organisation name, or business number already exists.
         *
         * @param email          the email to check
         * @param orgName        the organisation name to check
         * @param businessNumber the business number to check
         * @return {@code true} if a duplicate account exists
         */
        private boolean epAccountAlreadyExists(String email, String orgName, String businessNumber) {
                for (User user : users) {
                        if (user instanceof EntertainmentProvider ep) {
                                if (ep.getEmail().equalsIgnoreCase(email)
                                                || ep.getOrgName().equalsIgnoreCase(orgName)
                                                || ep.getBusinessRegistrationNumber().equals(businessNumber)) {
                                        return true;
                                }
                        }
                        // Also check email against non-EP users
                        if (!(user instanceof EntertainmentProvider) && user.getEmail().equalsIgnoreCase(email)) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * Handles the edit preferences use case for students.
         *
         * <p>
         * Prompts the student to enter their preferred
         * event types as a comma-separated list.
         * </p>
         */
        public void editPreferences() {
                if (!checkCurrentUserIsStudent()) {
                        view.displayError("Only students can edit preferences.");
                        return;
                }

                Student student = (Student) currentUser;
                StudentPreferences prefs = student.getPreferences();

                view.displayInfo("Current preferences: " + prefs.toString());
                view.displayInfo("Enter preferred event types as a comma-separated list.");
                view.displayInfo("Valid types: Music, Theatre, Dance, Movie, Sports");
                view.displayInfo("Leave blank to clear all preferences.");

                String rawInput = view.getInput("Enter preferences: ");

                boolean success = prefs.updatePreferences(rawInput);

                if (!success) {
                        view.displayError(
                                        "Some preference tokens were not recognised. Valid tokens: Music, Theatre, Dance, Movie, Sports.");
                } else {
                        view.displaySuccess("Preferences updated successfully.");
                }
                view.displayInfo("Current preferences: " + prefs);
        }

        /**
         * Returns the entertainment provider that owns the
         * event with the given event number.
         *
         * <p>
         * Searches through all registered users to find
         * the EP whose event list contains an event with the
         * matching ID.
         * </p>
         *
         * @param eventNumber the event ID to search for
         * @return the owning {@link EntertainmentProvider},
         *         or {@code null} if not found
         */
        public EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
                for (User user : users) {
                        if (user instanceof EntertainmentProvider ep) {
                                for (model.Event event : ep.getEvents()) {
                                        if (event.getEventID() == eventNumber) {
                                                return ep;
                                        }
                                }
                        }
                }
                return null;
        }
}
