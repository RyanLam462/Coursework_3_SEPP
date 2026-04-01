package model;

/**
 * Holds the preferences for a {@link Student} user.
 *
 * <p>Preferences allow students to indicate which types
 * of event they are interested in. Each event type has
 * an independent boolean flag that can be toggled on
 * or off.</p>
 */
public class StudentPreferences {

    /** Whether the student prefers music events. */
    public boolean preferMusicEvents;

    /** Whether the student prefers theatre events. */
    public boolean preferTheaterEvents;

    /** Whether the student prefers dance events. */
    public boolean preferDanceEvents;

    /** Whether the student prefers movie events. */
    public boolean preferMovieEvents;

    /** Whether the student prefers sports events. */
    public boolean preferSportsEvents;

    /**
     * Constructs default student preferences with all
     * event types set to {@code false} (no preference).
     */
    public StudentPreferences() {
        this.preferMusicEvents = false;
        this.preferTheaterEvents = false;
        this.preferDanceEvents = false;
        this.preferMovieEvents = false;
        this.preferSportsEvents = false;
    }

    /**
     * Updates preferences from a raw string input.
     *
     * <p>The raw string should be a comma-separated list
     * of event types the student prefers. Valid tokens
     * (case-insensitive) are: {@code Music}, {@code Theatre},
     * {@code Dance}, {@code Movie}, {@code Sports}.
     * All flags are reset to {@code false} before parsing,
     * so only the listed types will be set to {@code true}.</p>
     *
     * <p>An empty or blank string clears all preferences.</p>
     *
     * @param studentRawStringPreferences the comma-separated
     *        preference string
     * @return {@code true} if all tokens were recognised,
     *         {@code false} if any token was invalid
     */
    public boolean updatePreferences(
            String studentRawStringPreferences) {
        // Reset all preferences
        preferMusicEvents = false;
        preferTheaterEvents = false;
        preferDanceEvents = false;
        preferMovieEvents = false;
        preferSportsEvents = false;

        if (studentRawStringPreferences == null
                || studentRawStringPreferences.isBlank()) {
            return true;  // clearing all is valid
        }

        boolean allValid = true;
        String[] tokens =
            studentRawStringPreferences.split(",");

        for (String token : tokens) {
            String trimmed = token.trim().toLowerCase();
            switch (trimmed) {
                case "music" ->
                    preferMusicEvents = true;
                case "theatre" ->
                    preferTheaterEvents = true;
                case "dance" ->
                    preferDanceEvents = true;
                case "movie" ->
                    preferMovieEvents = true;
                case "sports" ->
                    preferSportsEvents = true;
                default -> allValid = false;
            }
        }

        return allValid;
    }

    /**
     * Returns a string summarising these preferences.
     *
     * @return a descriptive string of the preferences
     */
    @Override
    public String toString() {
        return "StudentPreferences{"
            + "music=" + preferMusicEvents
            + ", theatre=" + preferTheaterEvents
            + ", dance=" + preferDanceEvents
            + ", movie=" + preferMovieEvents
            + ", sports=" + preferSportsEvents
            + '}';
    }
}
