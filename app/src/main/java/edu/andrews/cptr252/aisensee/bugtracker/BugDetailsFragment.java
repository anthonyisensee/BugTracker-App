package edu.andrews.cptr252.aisensee.bugtracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Date;
import java.util.UUID;


/**
 * Show the details for a bug and allow editing
 */
public class BugDetailsFragment extends Fragment {

    /** Tag used to identify the DatePickerDialog in the FragmentManger */
    private static final String DIALOG_DATE = "date";

    /** Result code used for communication with DatePickerFragment */
    private static final int REQUEST_DATE = 0;

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

    /**
     * Update the date displayed on the date button
     */
    public void updateDate() {
        mDateButton.setText(mBug.getDate().toString());
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
        // set the date text on the bug date button
        updateDate();
        // create a listener to handle date button clicks
        mDateButton.setOnClickListener(new View.OnClickListener() {
            // Launch the date picker dialog when the user clicks the date button
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                // Create a new instance of the Date picker fragment
                // Give it the current bug date
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBug.getDate());
                // let DatePickerFragment communicate directly with BugDetailsFragment
                dialog.setTargetFragment(BugDetailsFragment.this, REQUEST_DATE);
                // display the DatePickerFragment
                dialog.show(fm, DIALOG_DATE);
            }
        });

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

    /**
     * Save the bug list to a JSON file when app is paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        BugList.getInstance(getActivity()).saveBugs();
    }

    /**
     * Process the result sent back to BugDetailsFragment from fragment that it is communicating
     * with (e.g. DatePickerFragment).
     * @param requestCode is the type of result passed back to BugDetailsFragment.
     *                    If requestCode = REQUEST_DATE, then a new date is in the intent.
     * @param dialogResultCode is RESULT_OK if user clicked OK in the dialog.
     * @param data is the Intent sent back to BugDetailsFragment.
     *            It Should contain a new date if user clicked ok in DatePickerFragment.
     */
    @Override
    public void onActivityResult(int requestCode, int dialogResultCode, Intent data) {
        if(dialogResultCode != Activity.RESULT_OK) {
            // user did not click ok in the dialog.
            // ignore intent contents
            return;
        }

        if(requestCode == REQUEST_DATE) {
            // a date is sent back in the intent. Extract the date
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // update the bug with the new date
            mBug.setDate(date);
            // update the bug date button text with the new date
            updateDate();
        }
    }
}