package edu.andrews.cptr252.aisensee.bugtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import java.util.UUID;

/**
 * Activity that hosts a BugDetailsFragment
 */
public class BugDetailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        // BugListFragment now launches BugDetailsActivity with a specific bug id.
        // Get the Intent sent to this activity from the BugListFragment.
        UUID bugId = (UUID)getIntent().getSerializableExtra(BugAdapter.EXTRA_BUG_ID);

        // Create a new instance of the BugDetailsFragment
        // with the Bug id as an argument.
        return BugDetailsFragment.newInstance(bugId);
    }
}
