package edu.andrews.cptr252.aisensee.bugtracker;

import java.util.UUID;

/**
 * Manage information for a specified bug.
 */
public class Bug {
    /** Unique Id for the Bug */
    private UUID mID;
    /** Title of the bug */
    private String mTitle;

    /**
     * Create and initialize a new Bug
     */
    public Bug() {
        // Generates a unique identifier for the bug.
        mID = UUID.randomUUID();
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
}
