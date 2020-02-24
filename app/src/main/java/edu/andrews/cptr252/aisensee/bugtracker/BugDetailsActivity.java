package edu.andrews.cptr252.aisensee.bugtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class BugDetailsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BugDetailsFragment();
    }
}
