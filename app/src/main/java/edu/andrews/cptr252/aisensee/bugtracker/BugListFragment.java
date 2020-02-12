package edu.andrews.cptr252.aisensee.bugtracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    public BugListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        getActivity().setTitle(R.string.bug_list_label);
        mBugs = BugList.getInstance(getActivity()).getBugs();

        // for now, list bugs in log
        for (Bug bug: mBugs){
            Log.d(TAG, bug.getTitle());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bug_list, container, false);

        return v;
    }

}
