package edu.andrews.cptr252.aisensee.bugtracker;

import java.util.UUID;
import java.util.Date;

/**
 * Manage information for a specified bug.
 */
public class Bug {
    /** Unique Id for the Bug */
    private UUID mID;
    /** Title of the bug */
    private String mTitle;
    /** Description of the bug */
    private String mDescription;
    /** Date when bug logged. */
    private Date mDate;
    /** Has the bug been solved. */
    private boolean mSolved;

    /**
     * Create and initialize a new Bug
     */
    public Bug() {  // also known as a constructor, it automatically adds data to a class upon generation.
        // Generates a unique identifier for the bug.
        mID = UUID.randomUUID();
        mDate = new Date();
    }

    /** Return unique id for Bug.
     * @return Bug Id
     */
    public UUID getID() {
        return mID;
    }

    /**
     * Return the title for the bug.
     * @return Title of Bug
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * Provide a new title for the bug.
     * @param title New title.
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    // Getters and setters for private fields
    public String getDescription() { return mDescription; }

    // Sets the bugs description.
    public void setDescription(String description) { mDescription = description; }
    public boolean isSolved() { return mSolved; }
    public void setSolved(boolean solved) { mSolved = solved; }
    public Date getDate() { return mDate; }
    public void setDate(Date date) { mDate = date; }

}
