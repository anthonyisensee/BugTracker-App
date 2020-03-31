package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Fragment to display list of Bugs.
 */
public class BugListFragment extends Fragment {
    /** Tag for message log */
    private static final String TAG = "BugListFragment";

    /** Reference to list of bugs in display */
    private ArrayList<Bug> mBugs;

    /** RecyclerView that displays list of bugs */
    private RecyclerView mRecyclerView;

    /** Adapter that generates/reuses views to display bugs */
    private BugAdapter mBugAdapter;

    public BugListFragment() {
        // Required empty public constructor
    }

    /** Create a new bug, add it to the list and launch bug editor. */
    private void addBug(){
        // create new bug
        Bug bug = new Bug();
        // add bug to the list
        BugList.getInstance(getActivity()).addBug(mBugs.size(), bug);   // adds the bug to the final position in the list

        // create an intent to send to BugDetailsActivity
        // add the bug Id as an extra so BugDetailsFragment can edit it.
        Intent intent = new Intent(getActivity(), BugDetailsActivity.class);
        intent.putExtra(BugAdapter.EXTRA_BUG_ID, bug.getID());

        // launch BugDetailsActivity which will launch BugDetailsFragment
        startActivityForResult(intent, 0);
    }

    /**
     * Inflates the options menu, provided there is a menu bar present.
     * @param menu The menu you've created and wish to display
     * @param inflater The inflater that will inflate the menu
     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_bug_list, menu);
    }

    /**
     * Runs the correct function when an item is selected from the menu.
     * @param item the menu item that has been selected.
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_bug:
                // new bug icon clicked
                addBug();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        getActivity().setTitle(R.string.bug_list_label);
        mBugs = BugList.getInstance(getActivity()).getBugs();

        // lets onCreate know that we have an options menu
        setHasOptionsMenu(true);

        // use our custom bug adapter for generating views for each bug
        mBugAdapter = new BugAdapter(mBugs, getActivity());     // getActivity added in relation to BugSwiper?

        // for now, list bugs in log
        for (Bug bug: mBugs){
            Log.d(TAG, bug.getTitle());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bug_list, container, false);

        mRecyclerView = v.findViewById(R.id.bug_list_recyclerView);
        // RecyclerView will use our BugAdapter to create views for bugs
        mRecyclerView.setAdapter((mBugAdapter));
        // Use a linear layout when displaying bugs
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create and attach our new touch helper for bug swipes
        BugSwiper bugSwiper = new BugSwiper(mBugAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(bugSwiper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return v;
    }

    /**
     * Bug list fragment was paused (user was most likely editing a bug).
     * Notify the adapter that the data set (Bug list) may have changed.
     * The adapter should update the views.
     */
    @Override
    public void onResume(){
        super.onResume();   // first execute parent's onResume method.
        mBugAdapter.notifyDataSetChanged();
    }

}
