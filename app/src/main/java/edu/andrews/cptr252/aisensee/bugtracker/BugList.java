package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.Context;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import edu.andrews.cptr252.aisensee.bugtracker.Bug;


/**
 * Manage list of bugs. This is a singleton class.
 * Only one instance of this class may be created. Ever.
 */
public class BugList {
    /**
     * Instance variable for BugList
     */
    private static BugList sOurInstance;

    /**
     * List of Bugs
     */
    private ArrayList<Bug> mBugs;
    /**
     * Reference to information about app environment
     */
    private Context mAppContext;

    private BugList(Context appContext) {
        mAppContext = appContext;
        mBugs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Bug bug = new Bug();
            bug.setTitle("Bug #" + i);
            // every other one is solved
            bug.setSolved(i % 2 == 0);
            mBugs.add(bug);
        }
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

    /**
     * Return list of bugs.
     *
     * @return Array of Bug objects.
     */
    public ArrayList<Bug> getBugs() {
        return mBugs;
    }

    /**
     * Return the Bug with a given id.
     * @param id Unique id for the bug
     * @return The bug object or null if not found
     */
    public Bug getBug(UUID id) {
        // Iterates through the bug list, looking for the bug with the same id that has
        // been provided as a parameter. When it is found, it returns bug.
        // if it cannot be found, returns null.
        for (Bug bug : mBugs) {
            if (bug.getID().equals(id))
                return bug;
        }
        return null;
    }

}