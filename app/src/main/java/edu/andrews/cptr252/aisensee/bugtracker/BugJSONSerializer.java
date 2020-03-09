package edu.andrews.cptr252.aisensee.bugtracker;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Read/write a list of bugs from/to a JSON file.
 */
public class BugJSONSerializer {

    // initialize needed values
    private Context mContext;   // note that the context gets information as to where you're operating at, file wise. needed to let the app send a request to the system to access files, then system sends info back
    private String mFilename;

    public BugJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    /**
     * Load list of bugs from JSON file on local device.
     * @return the list of bugs read from file.
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public ArrayList<Bug> loadBugs() throws IOException, JSONException {
        ArrayList<Bug> bugs = new ArrayList<>();
        BufferedReader reader = null;
        try {
            // open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // add next line from JSON file to StringBuilder
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();   // parse the JSON format. Create an array of JSON objects.

            // build the array of bugs from JSONObjects
            for (int i = 0; i < array.length(); i++ ) {
                bugs.add(new Bug(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // we will ignore this one, since it happens when we start afresh
        } finally { // finally Block always executes after try and catch blocks
            if (reader != null)
                reader.close(); // here we close the file if it is still open, even if exception occurred.
        }
        return bugs;
    }

    /**
     * Write a list of bugs to local device
     * @param bugs is list of bugs to save
     * @throws JSONException
     * @throws IOException
     */
    public void saveBugs(ArrayList<Bug> bugs) throws JSONException, IOException {       // creates an array of JSON objects from array of bugs
        // build an array in JSON
        JSONArray array = new JSONArray();
        for (Bug bug : bugs) {
            array.put(bug.toJSON());
        }

        // write the file to disk
        Writer writer = null;
        try {   // Convert array of JSON objects to strings and write those strings to a file. As we did earlier, use a Context object to open the file for writing on the device. Then write to the file using regular Java (not special Android) classes.
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
