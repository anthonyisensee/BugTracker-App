package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Adapter responsible for getting the view for a bug.
 */
public class BugAdapter extends RecyclerView.Adapter<BugAdapter.ViewHolder> {

    public static final String TAG = "BugAdapter";

    /** Used to store reference to list of bugs. */
    private ArrayList<Bug> mBugs;

    /**
     * Constructor for BugAdapter. initialize adapter with given list of bugs.
     * @param bugs list of bugs to display.
     */
    public BugAdapter(ArrayList<Bug> bugs) {
        mBugs = bugs;
    }

    /**
     * Class to hold references to widgets on a given view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
                                    implements View.OnClickListener {
        /** Textview  that displays bug title */
        public TextView bugTitleTextView;
        /** TextView that displays date bug was created */
        public TextView bugDateTextView;
        /** CheckBox that displays whether the bug was solved or not */
        public CheckBox bugSolvedCheckBox;

        /** Create a new view holder for a given view item in the bug list */
        public ViewHolder(View itemView) {
            super(itemView);

            // Store references to the widgets on the view item
            bugTitleTextView = itemView.findViewById(R.id.bug_list_item_titleTextView);
            bugDateTextView = itemView.findViewById(R.id.bug_list_item_dateTextView);
            bugSolvedCheckBox = itemView.findViewById(R.id.bug_list_item_solvedCheckBox);

            itemView.setOnClickListener(this);

        }
        /**
         * OnClick listener for bug in the bug list.
         * Triggered when user clicks on a bug in the list
         * @param view for bug that was clicked
         */
        @Override
        public void onClick(View view) {
            // Get index of bug that was clicked.
            int position = getAdapterPosition();
            // For now, just display the bug title.
            // In the future, open the selected bug.
            if (position != RecyclerView.NO_POSITION) {
                Bug bug = mBugs.get(position);
                Log.d(TAG, bug.getTitle() + " was clicked");
            }
        }
    }// end ViewHolder

    /**
     * Create a new view to display a bug.
     * Return the ViewHolder that stores references to the widgets on the new view.
     */
    @Override
    public BugAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the layout inflater from parent that contains the bug view item.
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout to display a bug in the list
        View bugView = inflater.inflate(R.layout.list_item_bug, parent, false);

        // Create a view holder to store references to the widgets on the new view.
        ViewHolder viewHolder = new ViewHolder(bugView);

        return viewHolder;
    }

    /**
     * Display given bug in the view referenced by the given ViewHolder.
     * @param viewHolder Contains references to widgets used to display bug.
     * @param position Index of the bug in the list.
     */
    @Override
    public void onBindViewHolder(BugAdapter.ViewHolder viewHolder, int position) {
        // Get bug for given index in bug list
        Bug bug = mBugs.get(position);

        // Get references to widgets stored in the ViewHolder
        TextView bugTitleTextView = viewHolder.bugTitleTextView;
        TextView bugDateTextView = viewHolder.bugDateTextView;
        CheckBox bugSolvedCheckBox = viewHolder.bugSolvedCheckBox;

        // Updated widgets on view with bug details
        bugTitleTextView.setText(bug.getTitle());
        bugDateTextView.setText(bug.getDate().toString());
        bugSolvedCheckBox.setChecked(bug.isSolved());
    }

    /**
     * Get number of bugs in bug list.
     */
    @Override
    public int getItemCount() {
        return mBugs.size();
    }
}
