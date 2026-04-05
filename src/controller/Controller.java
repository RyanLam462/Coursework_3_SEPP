package controller;

import model.AdminStaff;
import model.EntertainmentProvider;
import model.Student;
import model.User;
import view.View;

import java.util.Collection;

/**
 * Abstract base class for all controllers in the events app.
 *
 * <p>
 * Provides shared state such as the currently logged-in
 * user and the {@link View} for user interaction, along with
 * convenience methods for checking the current user's role
 * and for presenting menus.
 * </p>
 *
 * <p>
 * All sub-controllers share the same {@code View}
 * and {@code currentUser} reference, which are set via
 * the {@link MenuController}.
 * </p>
 */

public abstract class Controller {
    protected User currentUser;
    protected View view;

    protected Controller(View view) {
        assert view != null : "View must not be null";
        this.view = view;
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    protected boolean checkCurrentUserIsGuest() {
        return currentUser == null;
    }

    protected boolean checkCurrentUserIsAdmin() {
        return currentUser instanceof AdminStaff;
    }

    protected boolean checkCurrentUserIsStudent() {
        return currentUser instanceof Student;
    }

    protected boolean checkCurrentUserIsEntertainmentProvider() {
        return currentUser instanceof EntertainmentProvider;
    }

    /**
     * Presents a numbered menu from the given collection
     * of items and returns the 1-based index chosen by
     * the user.
     *
     * <p>
     * The user is shown a numbered list and prompted
     * with the given menuPrompt. If the input is
     * not a valid integer or is out of range, the method
     * returns -1.
     * </p>
     *
     * @param <T>        the type of the menu items
     * @param items      the collection of items to display
     * @param menuPrompt the prompt message shown to the user
     * @return the 1-based index of the chosen item, or -1 if the input is invalid
     */
    protected <T> int selectFromMenu(Collection<T> items, String menuPrompt) {
        if (items == null || items.isEmpty()) {
            view.displayError("No options available.");
            return -1;
        }

        // Display the numbered menu
        int index = 1;
        for (T item : items) {
            view.displayInfo(index + ". " + item);
            index++;
        }

        // Get user choice
        String input = view.getInput(menuPrompt);
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > items.size()) {
                view.displayError("Choice out of range. Please enter a number between 1 and " + items.size() + ".");
                return -1;
            }
            return choice;
        } catch (NumberFormatException e) {
            view.displayError("Invalid input. Please enter a number.");
            return -1;
        }
    }
}
