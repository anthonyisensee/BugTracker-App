package edu.andrews.cptr252.aisensee.bugtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// this import allows us to reference BugTable directly in this file
import edu.andrews.cptr252.aisensee.bugtracker.database.BugDbSchema.BugTable;

public class BugDbHelper extends SQLiteOpenHelper {

    /**
     * Current database version. This can be incremented as database structure changes,
     * for instance, if we were to add new features that require new/different data to be stored.
     */
    private static final int VERSION = 1;

    /** Database filename */
    private static final String DATABASE_NAME = "bugDb.db";
    /** Constructor */
    public BugDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * If database does not exist, create it.
     * @param db will contain the newly created database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute SQL command to create a new bug database
        db.execSQL("create table " + BugTable.NAME + "(" +
                " _id integer primary key autoincrement," +
                BugTable.Cols.UUID + ", " +
                BugTable.Cols.TITLE + ", " +
                BugTable.Cols.DESCRIPTION + ", " +
                BugTable.Cols.DATE + ", " +
                BugTable.Cols.SOLVED +
                ")");
    }

    /**
     * Previous database is older and needs to be upgraded to current version.
     * @param db is the database that needs to be upgraded
     * @param oldVersion is the version number of the database
     * @param newVersion is the version the database should be upgraded to.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // As features are added to the app, it may be necessary to add/delete columns to the
        // database or make other structural changes.

        // When user gets an app update, their database will need to be changed to the newer schema.
        // This is where you would add code to update their database.
    }
}
