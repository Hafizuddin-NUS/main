package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.prioritylevel.PriorityLevel;
import seedu.address.model.prioritylevel.PriorityLevelEnum;

/**
 * Stores the NRIC of the user who's logged in to the applicaion.
 * Also manages the logging in and out of the current session.
 */
public class SessionManager {
    public static final String NOT_LOGGED_IN = "This operation requires the user to be logged in!";

    private static Nric loggedInNric = null;

    /**
     * Stores the {@code Nric} of the successfully logged in person into the session.
     * PRE-CONDITION: Nric must be valid.
     * @param logInWithThisNric
     */
    public static void loginToSession(Nric logInWithThisNric) {
        requireNonNull(logInWithThisNric);
        loggedInNric = logInWithThisNric;
    }


    public static void logOutSession() {
        loggedInNric = null;
    }

    /**
     * Returns the {@code Nric} of the logged in person.
     * @throws CommandException if the app's not logged in.
     */
    public static Nric getLoggedInSessionNric() throws CommandException {
        if (!isLoggedIn()) {
            throw new CommandException(NOT_LOGGED_IN);
        }
        return loggedInNric;
    }

    /**
     * Reutrns true if user is logged in to the application.
     */
    public static boolean isLoggedIn() {
        if (loggedInNric == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns the {@code Person} of the person whose NRIC matches the one that's currently logged in.
     */
    public static Person getLoggedInPersonDetails(Model model) {
        List<Person> allPersonsList = model.getAddressBook().getPersonList();
        for (Person currPerson : allPersonsList) {
            if (currPerson.getNric() == loggedInNric) {
                return currPerson;
            }
        }
        return null;
    }

    /**
     * Returns true if current session has at least the required priority level for the operation.
     * @throws CommandException if user's not logged in.
     */
    public static boolean hasSufficientPriorityLevelForThisSession(Model model, PriorityLevelEnum minimumPriorityLevel)
            throws CommandException {
        if (!isLoggedIn()) {
            throw new CommandException(NOT_LOGGED_IN);
        }
        Person personToCheck = getLoggedInPersonDetails(model);
        return PriorityLevel.isPriorityLevelAtLeastOf(personToCheck, minimumPriorityLevel);
    }
}
