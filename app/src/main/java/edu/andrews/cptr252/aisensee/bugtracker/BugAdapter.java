package edu.andrews.cptr252.aisensee.bugtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Adapter responsible for getting the view for a bug.
 */
public class BugAdapter extends RecyclerView.Adapter<BugAdapter.ViewHolder> {

    /** key used to pass the id of the bug */
    public static final String EXTRA_BUG_ID = "edu.andrews.cptr252.aisensee.bugtracker.bug_id";

    public static final String TAG = "BugAdapter";

    /** Used to store reference to list of bugs. */
    private ArrayList<Bug> mBugs;

    /** Activity hosting the list fragment */
    private Activity mActivity;

    /**
     * Constructor for BugAdapter. initialize adapter with given list of bugs.
     * @param bugs list of bugs to display.
     */
    public BugAdapter(ArrayList<Bug> bugs, Activity activity) {
        mBugs = bugs;
        mActivity = activity;
    }

    /** Return reference to activity hosting the bug list fragment */
    public Context getActivity() {
        return mActivity;
    }

    /**
     * Create snackbar with ability to undo bug deletion.
     */
    private void showUndoSnackbar(final Bug bug, final int position) {

        // get root view for activity hosting bug list fragment
        View view = mActivity.findViewById(android.R.id.content);

        // build message stating which bug was deleted
        String bugDeletedText = mActivity.getString(R.string.bug_deleted_msg, bug.getTitle());

        // create the snackbar
        Snackbar snackbar = Snackbar.make(view, bugDeletedText, Snackbar.LENGTH_LONG);

        // adds the undo option to the snackbar
        snackbar.setAction(R.string.undo_option, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo is selected, restore the deleted item
                restoreBug(bug, position);
            }
        });

        // if user does not undo
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    // officially delete bug from list
                    BugList.getInstance(mActivity).deleteBug(bug);
                }
            }
        });

        // text for undo string will be yellow
        snackbar.setActionTextColor(Color.YELLOW);

        // and finally, display the snackbar
        snackbar.show();

    }

    /**
     * Remove bug from list
     * @param position index of bug to remove
     */
    public void deleteBug(int position) {

        // save deleted bug so we can undo delete if needed.
        final Bug bug = mBugs.get(position);

        // delete bug from bug array used by adapter
        // will be deleted from database once undo snackbar is dismissed
        mBugs.remove(position);

        // update list of bugs in recyclerview
        notifyItemRemoved(position);

        // display snackbar so user may undo their delete
        showUndoSnackbar(bug, position);
    }

    /**
     * Force adapter to load new bug list and regenerate views.
     */
    public void refreshBugListDisplay() {
        // get instance of bug list
        mBugs = BugList.getInstance(mActivity).getBugs();
        // update views
        notifyDataSetChanged();
    }

    /**
     * Put deleted bug back into list
     * @param bug to restore
     * @param position in list where bug will go
     */
    public void restoreBug(Bug bug, int position) {

        // refresh the display
        refreshBugListDisplay();
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

        /** Context hosting the view */
        public Context context;

        /** Create a new view holder for a given view item in the bug list */
        public ViewHolder(View itemView) {
            super(itemView);

            // Store references to the widgets on the view item
            bugTitleTextView = itemView.findViewById(R.id.bug_list_item_titleTextView);
            bugDateTextView = itemView.findViewById(R.id.bug_list_item_dateTextView);
            bugSolvedCheckBox = itemView.findViewById(R.id.bug_list_item_solvedCheckBox);

            // Get the context of the view. This will be the activity hosting the view.
            context = itemView.getContext();

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

                // start an instance of BugDetailsFragment
                Intent i = new Intent(context, BugDetailsActivity.class);
                // pass the id of the bug as an intent
                i.putExtra(BugAdapter.EXTRA_BUG_ID, bug.getID());
                context.startActivity(i);
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
