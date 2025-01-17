package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guitests.guihandles.StatusBarFooterHandle;
import seedu.address.model.AddressBook;

//@@ author shaoyi1997-reused
public class StatusBarFooterTest extends GuiUnitTest {

    private static final Path STUB_SAVE_LOCATION = Paths.get("Stub");
    private static final Path RELATIVE_PATH = Paths.get(".");

    private static final Clock originalClock = StatusBarFooter.getClock();
    private static final Clock injectedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    private StatusBarFooterHandle statusBarFooterHandle;
    private final AddressBook addressBook = new AddressBook();

    @BeforeAll
    public static void setUpBeforeClass() {
        // inject fixed clock
        StatusBarFooter.setClock(injectedClock);
    }

    @AfterAll
    public static void tearDownAfterClass() {
        // restore original clock
        StatusBarFooter.setClock(originalClock);
    }

    @BeforeEach
    public void setUp() {
        StatusBarFooter statusBarFooter = new StatusBarFooter(STUB_SAVE_LOCATION, addressBook);
        uiPartExtension.setUiPart(statusBarFooter);

        statusBarFooterHandle = new StatusBarFooterHandle(statusBarFooter.getRoot());
    }

    @Test
    public void display() {
        // initial state
        assertStatusBarContent(RELATIVE_PATH.resolve(STUB_SAVE_LOCATION).toString(), SYNC_STATUS_INITIAL);

        // after address book is updated
        guiRobot.interact(() -> addressBook.addEntity(ALICE));
        assertStatusBarContent(RELATIVE_PATH.resolve(STUB_SAVE_LOCATION).toString(),
                String.format(SYNC_STATUS_UPDATED, new Date(injectedClock.millis()).toString()));
    }

    /**
     * Asserts that the save location matches that of {@code expectedSaveLocation}, and the
     * sync status matches that of {@code expectedSyncStatus}.
     */
    private void assertStatusBarContent(String expectedSaveLocation, String expectedSyncStatus) {
        assertEquals(expectedSaveLocation, statusBarFooterHandle.getSaveLocation());
        assertEquals(expectedSyncStatus, statusBarFooterHandle.getSyncStatus());
        guiRobot.pauseForHuman();
    }

}
//@@ author
