package view;

/**
 * Interface defining the contract for all user interface
 * implementations in the events app.
 *
 * <p>
 * The {@code View} provides methods for displaying
 * information to the user and collecting input. This
 * abstraction enables the system to support different
 * UI implementations (e.g., text-based, graphical) while
 * keeping the controllers decoupled from specific UI
 * technology.
 * </p>
 */
public interface View {

    String getInput(String prompt);

    void displaySuccess(String successMessage);

    void displayError(String errorMessage);

    void displayInfo(String message);

    void displayListofPerformances(java.util.Collection<String> listOfPerformanceInfo);

    void displaySpecificPerformance(String performanceInfo);

    void displayBookingRecord(String bookingRecord);
}
