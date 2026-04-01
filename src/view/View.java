package view;

/**
 * Interface defining the contract for all user interface
 * implementations in the events app.
 *
 * <p>The {@code View} provides methods for displaying
 * information to the user and collecting input. This
 * abstraction enables the system to support different
 * UI implementations (e.g., text-based, graphical) while
 * keeping the controllers decoupled from specific UI
 * technology.</p>
 */
public interface View {

    /**
     * Prompts the user with the given message and returns
     * their text input.
     *
     * @param prompt the prompt message to display
     * @return the user's input string
     */
    String getInput(String prompt);

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    void displayError(String message);

    /**
     * Displays a success message to the user.
     *
     * @param message the success message to display
     */
    void displaySuccess(String message);

    /**
     * Displays general informational output to the user.
     *
     * @param message the informational message to display
     */
    void displayInfo(String message);

    /**
     * Displays a booking record to the user.
     *
     * @param bookingRecord the formatted booking record
     *                      string
     */
    void displayBookingRecord(String bookingRecord);

    /**
     * Displays a list of performance info strings to the
     * user.
     *
     * @param listOfPerformanceInfo the collection of
     *        performance info strings to display
     */
    void displayListofPerformances(
        java.util.Collection<String> listOfPerformanceInfo);

    /**
     * Displays the details of a specific performance to
     * the user.
     *
     * @param performanceInfo the formatted performance
     *                        info string
     */
    void displaySpecificPerformance(
        String performanceInfo);
}
