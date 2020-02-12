package edu.andrews.cptr252.aisensee.bugtracker;

import androidx.fragment.app.Fragment;

/**
 * Activity for displaying our fine list of bugs.
 */
public class BugListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BugListFragment();
    }

}
