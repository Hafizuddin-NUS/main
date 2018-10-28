package seedu.address.session;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.password.Password;
import seedu.address.model.prioritylevel.PriorityLevel;
import seedu.address.model.prioritylevel.PriorityLevelEnum;

/**
 * Public API for Session, which manages the user login into the application.
 */
public interface Session {
    //================================ LOGIN/LOGOUT =================================================================
    boolean isLoggedIn();

    /**
     * Attempts to log into the session using a user input NRIC and Password.
     * @throws CommandException if the login parameters are incorrect.
     */
    void loginToSession(Nric loginWithThisNric, Password loginWithThisPassword) throws CommandException;

    void logOutSession();


    //================================================ GETTING LOGGED IN DETAILS ====================================
    /**
     * Returns the {@code Nric} of the logged in person.
     * @throws CommandException if the app's not logged in.
     */
    Nric getLoggedInSessionNric() throws CommandException;

    /**
     * Returns the {@code Person} object whose NRIC matches the one that's currently logged in.
     */
    Person getLoggedInPersonDetails() throws CommandException;

    /**
     * Returns the {@code Priority} object whose NRIC matches the one that's currently logged in.
     */
    PriorityLevel getLoggedInPriorityLevel() throws CommandException;

    //================================== PRIORITY LEVEL CONCERNS ==================================================
    /**
     * Returns true if current session has at least the required priority level for the operation.
     * @throws CommandException if user's not logged in.
     */
    boolean hasSufficientPriorityLevelForThisSession(PriorityLevelEnum minimumPriorityLevel) throws CommandException;
}
