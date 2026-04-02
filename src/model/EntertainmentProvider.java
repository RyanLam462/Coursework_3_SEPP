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

    private String orgName;
    private String businessRegistrationNumber;
    private String name;
    private String description;

    /** The list of events created by this EP. */
    private final List<Event> events;

    /**
     * Constructs a new {@code EntertainmentProvider} with
     * the given details.
     *
     * @param email                      the EP's contact email address
     * @param password                   the EP's password
     * @param orgName                    the name of the organisation; must not be
     *                                   null or blank
     * @param businessRegistrationNumber the EP's unique business registration
     *                                   number; must not be null
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
        assert orgName != null && !orgName.isBlank() : "Org name must not be null or blank";
        assert businessRegistrationNumber != null : "Business number must not be null";
        this.orgName = orgName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.name = name;
        this.description = description;
        this.events = new ArrayList<>();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        assert orgName != null && !orgName.isBlank() : "Org name must not be null or blank";
        this.orgName = orgName;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        assert businessRegistrationNumber != null : "Business number must not be null";
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
        return "EntertainmentProvider{orgName='" + orgName + "', email='" + getEmail() + "'}";
    }
}
