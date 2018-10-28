package seedu.address.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.DESC_REQUEST1;
import static seedu.address.logic.commands.CommandTestUtil.DESC_REQUEST2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPROVAL_REQUEST3;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showLeaveAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalLeave.getTypicalLeaveList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.EditLeaveCommand.EditLeaveDescriptor;
import seedu.address.model.LeaveList;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.leave.Leave;
import seedu.address.model.prioritylevel.PriorityLevelEnum;
import seedu.address.testutil.EditLeaveDescriptorBuilder;
import seedu.address.testutil.LeaveBuilder;
import systemtests.SessionHelper;

//@@author Hafizuddin-NUS
/**
 * Contains integration tests (interaction with the Model) and unit tests for EditLeaveCommand.
 */
public class EditLeaveCommandTest {

    private static final Leave REQUEST1EDIT = new LeaveBuilder().withNric("S1234567A")
            .withDate("01/02/2018").withApproval("APPROVED").withPriorityLevel(1).build();

    private Model model = new ModelManager(getTypicalLeaveList(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        SessionHelper.forceLoginWithPriorityLevelOf(PriorityLevelEnum.IT_UNIT.getPriorityLevelCode());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {

        Leave editedLeave = REQUEST1EDIT;

        EditLeaveDescriptor descriptor = new EditLeaveDescriptorBuilder(editedLeave).build();
        EditLeaveCommand editLeaveCommand = new EditLeaveCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditLeaveCommand.MESSAGE_EDIT_LEAVE_SUCCESS, editedLeave);

        Model expectedModel = new ModelManager(new LeaveList(model.getLeaveList()), new UserPrefs());
        expectedModel.updateLeave(model.getFilteredLeaveList().get(0), editedLeave);
        expectedModel.commitLeaveList();

        assertCommandSuccess(editLeaveCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditLeaveCommand editLeaveCommand = new EditLeaveCommand(INDEX_FIRST_PERSON, new EditLeaveDescriptor());
        Leave editedLeave = model.getFilteredLeaveList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditLeaveCommand.MESSAGE_EDIT_LEAVE_SUCCESS, editedLeave);

        Model expectedModel = new ModelManager(new LeaveList(model.getLeaveList()), new UserPrefs());
        expectedModel.commitLeaveList();

        assertCommandSuccess(editLeaveCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showLeaveAtIndex(model, INDEX_FIRST_PERSON);

        Leave leaveInFilteredList = model.getFilteredLeaveList().get(INDEX_FIRST_PERSON.getZeroBased());
        Leave editedLeave = new LeaveBuilder(leaveInFilteredList).withApproval(VALID_APPROVAL_REQUEST3).build();
        EditLeaveCommand editLeaveCommand = new EditLeaveCommand(INDEX_FIRST_PERSON,
                new EditLeaveDescriptorBuilder().withApproval(VALID_APPROVAL_REQUEST3).build());

        String expectedMessage = String.format(EditLeaveCommand.MESSAGE_EDIT_LEAVE_SUCCESS, editedLeave);

        Model expectedModel = new ModelManager(new LeaveList(model.getLeaveList()), new UserPrefs());
        expectedModel.updateLeave(model.getFilteredLeaveList().get(0), editedLeave);
        expectedModel.commitLeaveList();

        assertCommandSuccess(editLeaveCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidLeaveIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLeaveList().size() + 1);
        EditLeaveDescriptor descriptor = new EditLeaveDescriptorBuilder().withApproval(VALID_APPROVAL_REQUEST3).build();
        EditLeaveCommand editLeaveCommand = new EditLeaveCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editLeaveCommand, model, commandHistory, Messages.MESSAGE_INVALID_LEAVE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidLeaveIndexFilteredList_failure() {
        showLeaveAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of leave list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getLeaveList().getRequestList().size());

        EditLeaveCommand editLeaveCommand = new EditLeaveCommand(outOfBoundIndex,
                new EditLeaveDescriptorBuilder().withApproval(VALID_APPROVAL_REQUEST3).build());

        assertCommandFailure(editLeaveCommand, model, commandHistory, Messages.MESSAGE_INVALID_LEAVE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditLeaveCommand standardCommand = new EditLeaveCommand(INDEX_FIRST_PERSON, DESC_REQUEST1);

        // same values -> returns true
        EditLeaveDescriptor copyDescriptor = new EditLeaveDescriptor(DESC_REQUEST1);
        EditLeaveCommand commandWithSameValues = new EditLeaveCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditLeaveCommand(INDEX_SECOND_PERSON, DESC_REQUEST1)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditLeaveCommand(INDEX_FIRST_PERSON, DESC_REQUEST2)));
    }
}
