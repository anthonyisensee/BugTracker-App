package edu.andrews.cptr252.aisensee.bugtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Activity that hosts a BugDetailsFragment
 */
public class BugDetailsActivity extends FragmentActivity {

    /** ViewPager component that allows users to browse bugs by swiping */
    private ViewPager mViewPager;

    /** Array of bugs */
    private ArrayList<Bug> mBugs;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);

        // create the ViewPager
        mViewPager = new ViewPager(this);
        // Viewpager needs a resource id
        mViewPager.setId(R.id.viewPager);
        // Set the view for this activity to be the ViewPager
        // (Previously, it used the activity_fragment layout)
        setContentView(mViewPager);

        // get the list of bugs
        mBugs = BugList.getInstance(this).getBugs();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            // create a new BugDetailsFragment for bug at given position in list.
            @Override
            public Fragment getItem(int i) {
                Bug bug = mBugs.get(i);
                // create a new instance of the BugDetailsFragment
                // with the Bug id as an argument
                return BugDetailsFragment.newInstance(bug.getID());
            }

            @Override
            public int getCount() {
                return mBugs.size();
            }

        });

        // BugListFragment now launches BugDetailsActivity with a specific bug id.
        // Get the Intent sent to this activity from the BugListFragment.
        UUID bugId = (UUID) getIntent().getSerializableExtra(BugAdapter.EXTRA_BUG_ID);

        // Search through the list of bugs until we find the bug with the same id
        // as the one extracted from the intent.
        for (int i = 0; i < mBugs.size(); i++) {
            if (mBugs.get(i).getID().equals(bugId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        // If there is an App Bar (aka ActionBar), display the title of the current bug there.
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Bug bug = mBugs.get(i);
                if (bug.getTitle() != null) {
                    setTitle(bug.getTitle());
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}