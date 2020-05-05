package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import edu.andrews.cptr252.aisensee.bugtracker.Bug;

import edu.andrews.cptr252.aisensee.bugtracker.database.BugCursorWrapper;
import edu.andrews.cptr252.aisensee.bugtracker.database.BugDbHelper;
import edu.andrews.cptr252.aisensee.bugtracker.database.BugDbSchema.BugTable;


/**
 * Manage list of bugs. This is a singleton class.
 * Only one instance of this class may be created. Ever.
 */
public class BugList {

    /** Instance variable for BugList */
    private static BugList sOurInstance;

    /** SQLite database where bugs are stored */
    private SQLiteDatabase mDatabase;

    /** Reference to information about app environment */
    private Context mAppContext;    // reference to the app context allows this class to start activities and access project resources

    /** Tag for message log */
    private static final String TAG = "BugList";

    /** Name of JSON file containing list of bugs */
    private static final String FILENAME = "bugs.json";

    /** Reference to JSON serializer for a list of bugs */
    private BugJSONSerializer mSerializer;

    /**
     * Pack bug information into a ContentValues object.
     * @param bug to pack
     * @return resulting ContentValues object
     */
    // ContentValues is a data structure that stores key-value pairs, similar to HashMap or
    // Bundle. It is designed to work with the data SQLite uses.
    public static ContentValues getContentValues(Bug bug) {
        ContentValues values = new ContentValues();
        values.put(BugTable.Cols.UUID, bug.getID().toString());
        values.put(BugTable.Cols.TITLE, bug.getTitle());
        values.put(BugTable.Cols.DESCRIPTION, bug.getDescription());
        values.put(BugTable.Cols.DATE, bug.getDate().getTime());
        values.put(BugTable.Cols.SOLVED, bug.isSolved() ? 1 : 0);

        return values;
    }

    /**
     * Build a query for Bug DB
     * @param whereClause defines the where clause of a SQL query
     * @param whereArgs defines where arguments for a SQL query
     * @return Object defining a SQL query
     */
    private BugCursorWrapper queryBugs(String whereClause, String[] whereArgs) {
        // execute a sql query on the bug database. our queries will be simple. Only specify
        // where clause and its arguments. Query results stored in a Cursor object.
        Cursor cursor = mDatabase.query(
                BugTable.NAME,
                null,   // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new BugCursorWrapper(cursor);
    }

    /**
     * Return one and only instance of the bug list.
     * (If it does not exist, create it).
     *
     * @param c is the Application context
     * @return Reference to the bug list.
     */
    public static BugList getInstance(Context c) {
        if (sOurInstance == null) {
            sOurInstance = new BugList(c.getApplicationContext());
        }
        return sOurInstance;
    }

    private BugList(Context appContext) {
        // get context for application instead of activity's context.
        // using the activity's context prevents the activity from being reclaimed by the garbage
        // collector when it is no longer needed. The app's context exists for the lifetime of the app
        mAppContext = appContext.getApplicationContext();

        // Open database file or create it if it does not already exist.
        // If the database is older version, onUpgrade will be called.
        mDatabase = new BugDbHelper(mAppContext).getWritableDatabase();
    }

    /**
     * Add a bug to the list at given position.
     * @param bug is the bug to add.
     */
    public void addBug(Bug bug) {
        ContentValues values = getContentValues(bug);
        mDatabase.insert(BugTable.NAME, null, values);
    }

    /**
     * Return the Bug with a given id.
     * @param id Unique id for the bug
     * @return The bug object or null if not found
     */
    public Bug getBug(UUID id) {
        // queries our database for a bug with a given Id. Stores result in Cursor.
        // note that the " = ? " part protects against sql injection attack. "?" gets replaced
        // by the given string array containing the bug id (where arguments parameter)
        // with this approach, the where arguments are treated as a string and not executed if they contain
        // sql code
        BugCursorWrapper cursor = queryBugs(BugTable.Cols.UUID + " = ? ",
                new String[] {id.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getBug();
        } finally {
            cursor.close();
        }

    }

    /**
     * Return list of bugs.
     *
     * @return Array of Bug objects.
     */
    public ArrayList<Bug> getBugs() {

        // for code that needs an ArrayList, query the database and generate an ArrayList of bugs.
        // this array list is not automatically updated. it needs to be recreated in order to get
        // updated list of bugs.

        ArrayList<Bug> bugs = new ArrayList<>();
        BugCursorWrapper cursor = queryBugs(null, null);

        try {
            cursor.moveToFirst();
            // query results stored in a cursor object. Our query here returns all bugs in
            // database. We iterate through each result and add it to the newly created ArrayList.
            while (!cursor.isAfterLast()) {
                bugs.add(cursor.getBug());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        // finally, return completed ArrayList
        return bugs;
    }

    /**
     * Update information for a given bug.
     * @param bug contains the latest information for the bug.
     */
    public void updateBug(Bug bug) {
        String uuidString = bug.getID().toString();
        ContentValues values = getContentValues(bug);

        mDatabase.update(BugTable.NAME, values,
                BugTable.Cols.UUID + " = ? ",
                new String[] { uuidString } );
    }

    /**
     * Deletes a given bug from the list of bugs.
     * @param bug is the bug to delete.
     */
    public void deleteBug(Bug bug) {
        String uuidString = bug.getID().toString();
        mDatabase.delete(BugTable.NAME,
                BugTable.Cols.UUID + " = ? ",
                new String[] { uuidString} );
    }

}