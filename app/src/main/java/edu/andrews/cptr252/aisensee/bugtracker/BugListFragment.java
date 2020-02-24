package edu.andrews.cptr252.aisensee.bugtracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
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

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        getActivity().setTitle(R.string.bug_list_label);
        mBugs = BugList.getInstance(getActivity()).getBugs();

        // use our custom bug adapter for generating views for each bug
        mBugAdapter = new BugAdapter(mBugs);

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
