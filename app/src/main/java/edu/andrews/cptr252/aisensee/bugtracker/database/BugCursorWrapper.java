package edu.andrews.cptr252.aisensee.bugtracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.andrews.cptr252.aisensee.bugtracker.Bug;
import edu.andrews.cptr252.aisensee.bugtracker.database.BugDbSchema.BugTable;


public class BugCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     * @param cursor The underlying cursor to wrap.
     */
    public BugCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Extracts bug information from a query.
     * @return Bug that resulted from the query.
     */
    public Bug getBug() {
        String uuidString = getString(getColumnIndex(BugTable.Cols.UUID));
        String title = getString(getColumnIndex(BugTable.Cols.TITLE));
        String description = getString(getColumnIndex(BugTable.Cols.DESCRIPTION));
        long date = getLong(getColumnIndex(BugTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(BugTable.Cols.SOLVED));

        Bug bug = new Bug(UUID.fromString(uuidString));
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setDate(new Date(date));
        bug.setSolved(isSolved != 0);   // if isSolved != 0, set to true. if = 0, set to false.
        return bug;
    }
}
