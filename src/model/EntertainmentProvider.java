package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an entertainment provider (EP) in the
 * university events app.
 *
 * <p>
 * Entertainment providers can register, create events
 * and performances, cancel performances, and view their
 * own events. Each EP is verified through the
 * {@link external.VerificationService} upon registration.
 * </p>
 */
public class EntertainmentProvider extends User {

    /** The organisation name of the EP. */
    private String orgName;

    /**
     * The unique business registration number used to
     * verify the EP through the verification service.
     * 
     * Security: business registration number is
     * verified through external VerificationService
     * before account creation (in UserController)
     */
    private String businessRegistrationNumber;

    /** The EP representative's name. */
    private String name;

    /** A description of the EP / organisation. */
    private String description;

    /** The list of events created by this EP. */
    private final List<Event> events;

    /**
     * Constructs a new {@code EntertainmentProvider} with
     * the given details.
     *
     * @param email                      the EP's contact email address
     * @param password                   the EP's password
     * @param orgName                    the name of the organisation;
     *                                   must not be null or blank
     * @param businessRegistrationNumber the EP's unique
     *                                   business registration number; must not be
     *                                   null
     * @param name                       the EP representative's name
     * @param description                a description of the EP
     */
    public EntertainmentProvider(
            String email,
            String password,
            String orgName,
            String businessRegistrationNumber,
            String name,
            String description) {
        super(email, password);
        assert orgName != null && !orgName.isBlank()
                : "Org name must not be null or blank";
        assert businessRegistrationNumber != null
                : "Business number must not be null";
        this.orgName = orgName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.name = name;
        this.description = description;
        this.events = new ArrayList<>();
    }

    /**
     * Returns the organisation name.
     *
     * @return the EP's organisation name
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Sets the organisation name.
     *
     * @param orgName the new organisation name
     */
    public void setOrgName(String orgName) {
        assert orgName != null && !orgName.isBlank()
                : "Org name must not be null or blank";
        this.orgName = orgName;
    }

    /**
     * Returns the business registration number.
     *
     * @return the EP's business registration number
     */

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    /**
     * Sets the business registration number.
     *
     * @param businessRegistrationNumber the new number
     */
    public void setBusinessRegistrationNumber(
            String businessRegistrationNumber) {
        assert businessRegistrationNumber != null
                : "Business number must not be null";
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    /**
     * Returns the EP representative's name.
     *
     * @return the EP's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the EP representative's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the EP's description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the EP's description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns an unmodifiable view of the events created
     * by this EP.
     *
     * @return an unmodifiable list of events
     */
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Adds an event to this EP's list of events.
     *
     * @param event the event to add; must not be null
     */
    public void addEvent(Event event) {
        assert event != null : "Event must not be null";
        events.add(event);
    }

    /**
     * Returns a string representation of this EP.
     *
     * @return a string describing the EP
     */
    @Override
    public String toString() {
        return "EntertainmentProvider{orgName='" + orgName
                + "', email='" + getEmail() + "'}";
    }
}
