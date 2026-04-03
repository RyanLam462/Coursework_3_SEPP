package controller;

import model.EntertainmentProvider;
import model.Performance;
import model.User;
import view.View;

import java.util.Arrays;
import java.util.List;

import external.PaymentSystem;
import external.MockPaymentSystem;

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

    private final UserController userController;
    private final EventPerformanceController eventPerformanceController;
    private final BookingController bookingController;

    /**
     * Constructs a new {@code MenuController}, wiring up
     * all sub-controllers with a shared performance list.
     *
     * @param view the view for user interaction
     */
    public MenuController(View view) {
        super(view);

        List<Performance> sharedPerformances = new java.util.ArrayList<>();
        PaymentSystem sharedPaymentSystem = new MockPaymentSystem();

        this.userController = new UserController(view);
        this.eventPerformanceController = new EventPerformanceController(view, sharedPerformances, sharedPaymentSystem);
        this.bookingController = new BookingController(view, sharedPerformances, sharedPaymentSystem);

        userController.addPreregisteredUsers();

        // Load predefined events and performances for testing
        EntertainmentProvider mockEP = null;
        for (User u : userController.getUsers()) {
            if (u instanceof EntertainmentProvider) {
                mockEP = (EntertainmentProvider) u;
                break;
            }
        }
        if (mockEP != null) {
            eventPerformanceController.loadPreregisteredData(mockEP);
        }
    }

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
            syncCurrentUser(userController.getCurrentUser());

            if (checkCurrentUserIsGuest()) {
                running = handleGuestMainMenu();
            } else if (checkCurrentUserIsStudent()) {
                running = handleStudentMainMenu();
            } else if (checkCurrentUserIsEntertainmentProvider()) {
                running = handleEntertainmentProviderMainMenu();
            } else if (checkCurrentUserIsAdmin()) {
                running = handleAdminStaffMainMenu();
            } else {
                view.displayError("Unknown user type. Logging out.");
                userController.logout();
                syncCurrentUser(null);
            }
        }

        view.displayInfo("Thank you for using the Events App. Goodbye!");
    }

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
        view.displayInfo("=== Guest Menu ===");
        List<String> options = Arrays.asList(
                GuestMenuOptions.LOGIN.name(),
                GuestMenuOptions.REGISTER_EP.name(),
                "EXIT");

        int choice = selectFromMenu(options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                userController.login();
                syncCurrentUser(userController.getCurrentUser());
            }
            case 2 -> {
                userController.registerEntertainmentProvider();
                syncCurrentUser(userController.getCurrentUser());
            }
            case 3 -> {
                // Exit
                return false;
            }
            default -> view.displayError("Invalid choice. Please try again.");
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
     * @return {@code true} to continue the loop, {@code false} to exit
     */
    private boolean handleStudentMainMenu() {
        view.displayInfo("=== Student Menu ===");
        List<String> options = Arrays.asList(
                StudentMenuOptions.LOGOUT.name(),
                StudentMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                StudentMenuOptions.VIEW_PERFORMANCE.name(),
                StudentMenuOptions.EDIT_PREFERENCES.name(),
                StudentMenuOptions.BOOK_EVENT.name(),
                StudentMenuOptions.CANCEL_BOOKING.name());

        int choice = selectFromMenu(options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                eventPerformanceController.searchForPerformances();
            }
            case 3 -> {
                eventPerformanceController.viewPerformance();
            }
            case 4 -> {
                userController.editPreferences();
            }
            case 5 -> {
                bookingController.bookPerformance();
            }
            case 6 -> {
                bookingController.cancelBooking();
            }
            default -> view.displayError("Invalid choice. Please try again.");
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
     * @return {@code true} to continue the loop, {@code false} to exit
     */
    private boolean handleEntertainmentProviderMainMenu() {
        view.displayInfo("=== Entertainment Provider Menu ===");
        List<String> options = Arrays.asList(
                EPMenuOptions.LOGOUT.name(),
                EPMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                EPMenuOptions.VIEW_PERFORMANCE.name(),
                EPMenuOptions.CREATE_EVENT.name(),
                EPMenuOptions.CANCEL_PERFORMANCE.name());

        int choice = selectFromMenu(options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                eventPerformanceController.searchForPerformances();
            }
            case 3 -> {
                eventPerformanceController.viewPerformance();
            }
            case 4 -> {
                eventPerformanceController.createEvent();
            }
            case 5 -> {
                eventPerformanceController.cancelPerformance();
            }
            default -> view.displayError("Invalid choice. Please try again.");
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
        view.displayInfo("=== Admin Staff Menu ===");
        List<String> options = Arrays.asList(
                AdminMenuOptions.LOGOUT.name(),
                AdminMenuOptions.SEARCH_FOR_PERFORMANCES.name(),
                AdminMenuOptions.VIEW_PERFORMANCE.name());

        int choice = selectFromMenu(options, "Choose an option: ");

        switch (choice) {
            case 1 -> {
                userController.logout();
                syncCurrentUser(null);
            }
            case 2 -> {
                eventPerformanceController.searchForPerformances();
            }
            case 3 -> {
                eventPerformanceController.viewPerformance();
            }
            default -> view.displayError("Invalid choice. Please try again.");
        }
        return true;
    }
}
