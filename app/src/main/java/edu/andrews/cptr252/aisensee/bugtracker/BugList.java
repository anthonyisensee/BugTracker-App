package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import edu.andrews.cptr252.aisensee.bugtracker.Bug;


/**
 * Manage list of bugs. This is a singleton class.
 * Only one instance of this class may be created. Ever.
 */
public class BugList {

    /** Instance variable for BugList */
    private static BugList sOurInstance;

    /** List of Bugs */
    private ArrayList<Bug> mBugs;

    /** Reference to information about app environment */
    private Context mAppContext;    // reference to the app context allows this class to start activities and access project resources

    /** Tag for message log */
    private static final String TAG = "BugList";

    /** Name of JSON file containing list of bugs */
    private static final String FILENAME = "bugs.json";

    /** Reference to JSON serializer for a list of bugs */
    private BugJSONSerializer mSerializer;

    /**
     * Write bug list to JSON file.
     * @return true if successful, false otherwise
     */
    public boolean saveBugs() {     // use our JSON serailizer object to write list of bugs to a local file.
        try {
            mSerializer.saveBugs(mBugs);
            Log.d(TAG, "Bugs saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving bugs: " + e);
            return false;
        }
    }

    private BugList(Context appContext) {
        mAppContext = appContext;
        // create our serializer to load and save bugs
        mSerializer = new BugJSONSerializer(mAppContext, FILENAME);

        try {
            // load bugs from JSON file
            mBugs = mSerializer.loadBugs();
        } catch (Exception e) {
            // Unable to load from file, so create empty bug list.
            // Either file does not exist (okay)
            // or file contains an error (not great)
            mBugs = new ArrayList<>();
            Log.e(TAG, "Error loading bugs: " + e);
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
     * Add a bug to the list at given position.
     * @param position is the index for the bug to add.
     * @param bug is the bug to add.
     */
    public void addBug(int position, Bug bug) {
        mBugs.add(position, bug);       // adds the new bug to the list.
        saveBugs();                     // updates the JSON file.
    }

    /**
     * Deletes a given bug from the list of bugs.
     * @param position is the index of bug to delete.
     */
    public void deleteBug(int position) {
        mBugs.remove(position);     // removes the bug at the indexed position
        saveBugs();                 // saves the updated bug list to the JSON file.
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