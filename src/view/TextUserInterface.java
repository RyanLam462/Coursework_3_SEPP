package view;

import java.util.Scanner;

/**
 * A text-based (console) implementation of the {@link View}
 * interface for the university events app.
 *
 * <p>All user interaction happens through {@code System.in}
 * and {@code System.out}. Error messages are prefixed with
 * "[ERROR]" and success messages with "[SUCCESS]" for
 * visual clarity in the terminal.</p>
 */
public class TextUserInterface implements View {

    /** ANSI colour code for red (errors). */
    private static final String ANSI_RED = "\u001B[31m";

    /** ANSI colour code for green (success). */
    private static final String ANSI_GREEN = "\u001B[32m";

    /** ANSI colour code for yellow (info). */
    private static final String ANSI_YELLOW = "\u001B[33m";

    /** ANSI reset code. */
    private static final String ANSI_RESET = "\u001B[0m";

    /** Scanner for reading user input from stdin. */
    private final Scanner scanner;

    /**
     * Constructs a new {@code TextUserInterface} reading
     * from {@code System.in}.
     */
    public TextUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the prompt to stdout and waits for the
     * user to enter a line of text.</p>
     */
    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the message in red with an "[ERROR]"
     * prefix.</p>
     */
    @Override
    public void displayError(String message) {
        System.out.println(
            ANSI_RED + "[ERROR] " + message + ANSI_RESET);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the message in green with a "[SUCCESS]"
     * prefix.</p>
     */
    @Override
    public void displaySuccess(String message) {
        System.out.println(
            ANSI_GREEN + "[SUCCESS] " + message
            + ANSI_RESET);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the message in yellow with an "[INFO]"
     * prefix.</p>
     */
    @Override
    public void displayInfo(String message) {
        System.out.println(
            ANSI_YELLOW + "[INFO] " + message
            + ANSI_RESET);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the booking record string as-is to
     * stdout.</p>
     */
    @Override
    public void displayBookingRecord(
            String bookingRecord) {
        System.out.println(bookingRecord);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints each performance info string on its own
     * line.</p>
     */
    @Override
    public void displayListofPerformances(
            java.util.Collection<String>
                listOfPerformanceInfo) {
        if (listOfPerformanceInfo == null
                || listOfPerformanceInfo.isEmpty()) {
            System.out.println(
                ANSI_YELLOW + "[INFO] "
                + "No performances to display."
                + ANSI_RESET);
            return;
        }
        for (String info : listOfPerformanceInfo) {
            System.out.println(info);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Prints the performance info string as-is to
     * stdout.</p>
     */
    @Override
    public void displaySpecificPerformance(
            String performanceInfo) {
        System.out.println(performanceInfo);
    }
}
