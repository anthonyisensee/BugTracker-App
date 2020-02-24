package edu.andrews.cptr252.aisensee.bugtracker;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.UUID;


/**
 * Show the details for a bug and allow editing
 */
public class BugDetailsFragment extends Fragment {

    /** Tag for logging fragment messages */
    public static final String TAG = "BugDetailsFragment";

    /** Reference to bug description field */
    private EditText mDescriptionField;

    /** Reference to bug date button */
    private Button mDateButton;

    /** Reference to bug solved check box */
    private CheckBox mSolvedCheckBox;


    /** Bug that is to be viewed/edited */
    private Bug mBug;

    /** Reference to title field for bug */
    private EditText mTitleField;


    public BugDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new BugDetailsFragment with a given Bug id as an argument.
     * @param bugId
     * @return A refrence to the new BugDetailsFragment
     */
    public static BugDetailsFragment newInstance(UUID bugId) {
        //Create a new argument bundle object
        // Add the Bug id as an argument
        Bundle args = new Bundle();
        args.putSerializable(BugAdapter.EXTRA_BUG_ID, bugId);
        // Create new instance of BugDetailsFragment
        BugDetailsFragment fragment = new BugDetailsFragment();
        // Pass the bundle (containing the bug id) to the fragment
        // The bundle will be unpacked in the fragment's onCreate(Bundle) method.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Extract bug id from Bundle
        super.onCreate(savedInstanceState);
        UUID bugId = (UUID) getArguments().getSerializable(BugAdapter.EXTRA_BUG_ID);

        // Get the bug with the id from the Bundle.
        // This will be the bug that the fragment displays.
        mBug = BugList.getInstance(getActivity()).getBug(bugId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bug_details, container, false);

        // get reference to EditText box for bug title
        mTitleField = v.findViewById(R.id.bug_title);
        mTitleField.setText(mBug.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // left intentionally blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mBug.setTitle(s.toString());
                Log.d(TAG, "Set description to "+s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // left intentionally blank
            }
        });

        // get reference to bug date button.
        mDateButton = v.findViewById(R.id.bug_date);
        mDateButton.setText(mBug.getDate().toString());
        // disable button for now. we will enable it at a later time
        mDateButton.setEnabled(false);

        // get reference to solved check box
        mSolvedCheckBox = v.findViewById(R.id.bug_solved);
        mSolvedCheckBox.setChecked(mBug.isSolved());
        // toggle bug solved status when check box is tapped
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the bug's solved property
                mBug.setSolved(isChecked);
                Log.d(TAG, "Set solved status to "+isChecked);
            }
        });

        // get a reference to EditText box for bug description
        mDescriptionField = v.findViewById(R.id.bug_description);
        mDescriptionField.setText(mBug.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // left blank intentionally
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user typed text, update the bug title
                mBug.setDescription(s.toString());
                // write the new title to the message log for debugging
                Log.d(TAG, "Description changed to " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // we left this blank on purpose. don't question it.
            }
        });
        return v;
    }

}
