import controller.MenuController;
import view.TextUserInterface;
import view.View;

/**
 * Entry point for the University of Hindeburgh Events App.
 *
 * <p>Initialises the {@link TextUserInterface} and the
 * {@link MenuController}, then starts the main menu loop.
 * Pre-registered students and admin staff are loaded
 * automatically during initialisation.</p>
 *
 * <p><strong>Usage:</strong> run this class directly.
 * The application presents a text-based menu in the
 * terminal.</p>
 */
public class Main {

    /**
     * Application entry point.
     *
     * <p>Creates the view and menu controller, then
     * starts the interactive menu loop.</p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialise the text-based user interface
        View view = new TextUserInterface();

        // Create the menu controller, which in turn
        // creates all sub-controllers and loads
        // pre-registered users
        MenuController menuController =
            new MenuController(view);

        // Display welcome banner
        view.displayInfo(
            "===================================");
        view.displayInfo(
            " University of Hindeburgh Events App");
        view.displayInfo(
            "===================================");
        view.displayInfo("");

        // Start the main application loop
        menuController.mainMenu();
    }
}
