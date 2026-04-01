package controller;

import model.Performance;
import model.User;
import view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Controller responsible for the main application menu loop.
 *
 * <p>
 * The {@code MenuController} presents role-specific menus
 * to the user (guest, student, entertainment provider, or
 * admin staff) and routes the chosen option to the
 * appropriate specialised controller. It is the central
 * orchestrator of the application flow.
 * </p>
 *
 * <p>
 * <strong>Class diagram note:</strong> the
 * {@code MenuController} is associated 1-1 with
 * {@link UserController},
 * {@link EventPerformanceController}, and
 * {@link BookingController}, and 1-* with
 * {@link Performance} (it initialises the shared
 * performance collection passed to the other
 * controllers).
 * </p>
 */
public class MenuController extends Controller {

    /** The user controller for login/logout/register. */
    private final UserController userController;

    /** The event/performance controller. */
    private final EventPerformanceController eventPerformanceController;

    /** The booking controller. */
    private final BookingController bookingController;

    /**
     * Constructs a new {@code MenuController}, wiring up
     * all sub-controllers with a shared performance list.
     *
     * @param view the view for user interaction
     */
    public MenuController(View view) {
        super(view);

        // Shared performance collection passed to
        // both EventPerformanceController and
        // BookingController
        List<Performance> sharedPerformances = new java.util.ArrayList<>();

        this.userController = new UserController(view);
        this.eventPerformanceController = new EventPerformanceController(
                view, sharedPerformances);
        this.bookingController = new BookingController(
                view, sharedPerformances);

        // Load pre-registered users
        userController.addPreregisteredUsers();
    }

    // ==========================================================
    // Current-user synchronisation
    // ==========================================================

    /**
     * Synchronises the {@code currentUser} field across
     * all sub-controllers so that every controller has
     * access to the same logged-in user reference.
     *
     * <p>
     * Called after every action that may change the
     * current user (login, logout, register).
     * </p>
     *
     * @param user the current user, or {@code null}
     */
    private void syncCurrentUser(User user) {
        this.currentUser = user;
        userController.setCurrentUser(user);
        eventPerformanceController.setCurrentUser(user);
        bookingController.setCurrentUser(user);
    }

    // ==========================================================
    // Main menu loop
    // ==========================================================

    /**
     * Starts the main menu loop.
     *
     * <p>
     * Repeatedly presents the appropriate menu to the
     * current user and dispatches the chosen option to
     * the relevant controller. The loop continues until
     * the user explicitly exits.
     * </p>
     */
    public void mainMenu() {
        boolean running = true;

        while (running) {
            // Sync user state before displaying menu
            syncCurrentUser(
                    userController.getCurrentUser());

            if (checkCurrentUserIsGuest()) {
                running = handleGuestMainMenu();
            } else if (checkCurrentUserIsStudent()) {
                running = handleStudentMainMenu();
            } else if (checkCurrentUserIsEntertainmentProvider()) {
                running = handleEntertainmentProviderMainMenu();
            } else if (checkCurrentUserIsAdmin()) {
                running = handleAdminStaffMainMenu();
            } else {
                view.displayError(
                        "Unknown user type. Logging out.");
                userController.logout();
                syncCurrentUser(null);
            }
        }

        view.displayInfo(
                "Thank you for using the Events App. "
                        + "Goodbye!");
    }

    // ==========================================================
    // Role-specific menu handlers
    // ==========================================================

    /**
     * Handles the guest main menu.
     *
     * <p>
     * Guests can log in, register as an EP, or exit.
     * </p>
     *
     * @return {@code true} to continue the loop,
     *         {@code false} to exit
     */
    private boolean handleGuestMainMenu() {
        view.displayInfo(
                "\n=== Guest Menu ===");
        List<String> options = Arrays.asList(
                GuestMenuOptions.LOGIN.name(),
                GuestMenuOptions.REGISTER_EP.name(),
                "EXIT");

        int choice = selectFromMenu(
                options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                // Login
                userController.login();
                syncCurrentUser(
                        userController.getCurrentUser());
            }
            case 2 -> {
                // Register EP
                userController
                        .registerEntertainmentProvider();
                syncCurrentUser(
                        userController.getCurrentUser());
            }
            case 3 -> {
                // Exit
                return false;
            }
            default -> view.displayError(
                    "Invalid choice. Please try again.");
        }
        return true;
    }

    /**
     * Handles the student main menu.
     *
     * <p>
     * Students can search, view, book, cancel
     * performances, edit preferences, or log out.
     * </p>
     *
     * @return {@code true} to continue the loop,
     *         {@code false} to exit
     */
    private boolean handleStudentMainMenu() {
        view.displayInfo(
                "\n=== Student Menu ===");
        List<String> options = Arrays.asList(
                StudentMenuOptions.LOGOUT.name(),
                StudentMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                StudentMenuOptions.VIEW_PERFORMANCE.name(),
                StudentMenuOptions.EDIT_PREFERENCES.name(),
                StudentMenuOptions.BOOK_EVENT.name(),
                StudentMenuOptions.CANCEL_BOOKING.name());

        int choice = selectFromMenu(
                options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                // Logout
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                // Search for performances
                eventPerformanceController
                        .searchForPerformances();
            }
            case 3 -> {
                // View performance
                eventPerformanceController
                        .viewPerformance();
            }
            case 4 -> {
                // Edit preferences
                userController.editPreferences();
            }
            case 5 -> {
                // Book performance
                bookingController.bookPerformance();
            }
            case 6 -> {
                // Cancel booking
                bookingController.cancelBooking();
            }
            default -> view.displayError(
                    "Invalid choice. Please try again.");
        }
        return true;
    }

    /**
     * Handles the entertainment provider main menu.
     *
     * <p>
     * EPs can search, view, create events, cancel
     * performances, or log out.
     * </p>
     *
     * @return {@code true} to continue the loop,
     *         {@code false} to exit
     */
    private boolean handleEntertainmentProviderMainMenu() {
        view.displayInfo(
                "\n=== Entertainment Provider Menu ===");
        List<String> options = Arrays.asList(
                EPMenuOptions.LOGOUT.name(),
                EPMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                EPMenuOptions.VIEW_PERFORMANCE.name(),
                EPMenuOptions.CREATE_EVENT.name(),
                EPMenuOptions.CANCEL_PERFORMANCE.name());

        int choice = selectFromMenu(
                options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                // Logout
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                // Search for performances
                eventPerformanceController
                        .searchForPerformances();
            }
            case 3 -> {
                // View performance
                eventPerformanceController
                        .viewPerformance();
            }
            case 4 -> {
                // Create event
                eventPerformanceController.createEvent();
            }
            case 5 -> {
                // Cancel performance
                eventPerformanceController
                        .cancelPerformance(
                                bookingController);
            }
            default -> view.displayError(
                    "Invalid choice. Please try again.");
        }
        return true;
    }

    /**
     * Handles the admin staff main menu.
     *
     * <p>
     * Admins can search, view performances,
     * or log out.
     * </p>
     *
     * @return {@code true} to continue the loop,
     *         {@code false} to exit
     */
    private boolean handleAdminStaffMainMenu() {
        view.displayInfo(
                "\n=== Admin Staff Menu ===");
        List<String> options = Arrays.asList(
                AdminMenuOptions.LOGOUT.name(),
                AdminMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                AdminMenuOptions.VIEW_PERFORMANCE.name());

        int choice = selectFromMenu(
                options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                // Logout
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                // Search for performances
                eventPerformanceController
                        .searchForPerformances();
            }
            case 3 -> {
                // View performance
                eventPerformanceController
                        .viewPerformance();
            }
            default -> view.displayError(
                    "Invalid choice. Please try again.");
        }
        return true;
    }

    // ==========================================================
    // Accessors for testing
    // ==========================================================

    /**
     * Returns the user controller.
     *
     * @return the {@link UserController}
     */
    public UserController getUserController() {
        return userController;
    }

    /**
     * Returns the event/performance controller.
     *
     * @return the {@link EventPerformanceController}
     */
    public EventPerformanceController getEventPerformanceController() {
        return eventPerformanceController;
    }

    /**
     * Returns the booking controller.
     *
     * @return the {@link BookingController}
     */
    public BookingController getBookingController() {
        return bookingController;
    }
}
