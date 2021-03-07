package uk.ac.leedsbeckett.c3542553.todoassessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
Edit Task Activity - Task List Assignment
Jake Hopkins c3542553
 */
public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "EditTaskActivity";
    public static Context mContext; // Context extracted to be used in delete prompt fragment
    public static int location; // location of task in the list
    EditText taskName;
    EditText taskDesc;
    CheckBox taskComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Log.d(TAG, "create");
        mContext = getBaseContext(); // Assign context
        Intent intent = getIntent();
        location = intent.getIntExtra("position", -1);
        taskName = findViewById(R.id.task_name_textbox); // locate task name
        taskDesc = findViewById(R.id.task_desc_textbox); // locate task desc
        taskComp = findViewById(R.id.task_done_box); // locate task completion

        // CLICK LISTENERS
        FloatingActionButton fab = findViewById(R.id.fab_confirm); // locate fab
        fab.setOnClickListener(new View.OnClickListener() { // fab click listener
            @Override
            public void onClick(View view) {
                fabClicked();
            }
        });
        Button del = findViewById(R.id.delete_task_button); // locate delete button
        del.setOnClickListener(new View.OnClickListener() { // delete listener
            @Override
            public void onClick(View v) {
                DeletePrompt dp = new DeletePrompt(); // open delete prompt
                dp.show(getSupportFragmentManager(), "Delete Prompt");
            }
        });
    }

    public void fabClicked() { // Called when FAB is clicked
        boolean nameEmpty = TextUtils.isEmpty(taskName.getText()); // Check name is filled
        boolean descEmpty = TextUtils.isEmpty(taskDesc.getText()); // Check description is filled

        if (!nameEmpty && !descEmpty) { // if both name and desc have been entered
            MainActivity.taskList.set(location, taskName.getText().toString()); // edit the data in the list
            MainActivity.descList.set(location, taskDesc.getText().toString()); // edit the data in the list
            MainActivity.taskStatus.set(location, taskComp.isChecked()); // edit the state of completion
            Intent openMainActivity = new Intent(EditTaskActivity.this, MainActivity.class); // create intent to switch to main activity
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(openMainActivity, 0); // load intent
        } else { // if one hasn't been entered
            if (nameEmpty) {
                taskName.setError("A task name is required!"); // inform user of error
            }
            if (descEmpty) {
                taskDesc.setError("A task description is required!"); // inform user of error
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Start");
        Log.d(TAG, "Task #" + String.valueOf(location));
        taskName.setText(MainActivity.taskList.get(location)); // Get task name from list
        taskDesc.setText(MainActivity.descList.get(location)); // Get task desc from list
        taskComp.setChecked(MainActivity.taskStatus.get(location)); // get task completion status
    }

    public static class DeletePrompt extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // create the dialog fragment for a delete prompt
            builder.setMessage(R.string.delete_prompt)
                    .setPositiveButton(R.string.delete_task, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { // If delete is confirmed
                            MainActivity.taskList.remove(location); // Remove task data from list
                            MainActivity.descList.remove(location); // Remove task data from list
                            MainActivity.taskStatus.remove(location); // Remove task data from list
                            MainActivity.dataSave(); // Call data save function to save data onto local files
                            startActivity(new Intent(EditTaskActivity.mContext, MainActivity.class)); // Return to main activity
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}