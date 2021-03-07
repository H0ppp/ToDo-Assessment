package uk.ac.leedsbeckett.c3542553.todoassessment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.LinkedList;

/*
Add Task Activity - Task List Assignment
Jake Hopkins c3542553
 */
public class AddTaskActivity extends AppCompatActivity {
    EditText taskName;
    EditText taskDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskName = findViewById(R.id.task_name_textbox); // Locate task name box
        taskDesc = findViewById(R.id.task_desc_textbox); // Locate task desc box
        FloatingActionButton fab = findViewById(R.id.fab_confirm); // Locate fab
        fab.setOnClickListener(new View.OnClickListener() { // fab clicked
            @Override
            public void onClick(View view) {
                fabClicked();
            }
        });
    }

    public void fabClicked() {
        boolean nameEmpty = TextUtils.isEmpty(taskName.getText()); // Check name is filled
        boolean descEmpty = TextUtils.isEmpty(taskDesc.getText()); // Check description is filled

        if (!nameEmpty && !descEmpty) { // if both name and desc have been entered
            MainActivity.taskList.addLast(taskName.getText().toString()); // Add the data into the list
            MainActivity.descList.addLast(taskDesc.getText().toString()); // Add the data into the list
            MainActivity.taskStatus.addLast(false); // Tasks default to false

            Intent openMainActivity = new Intent(AddTaskActivity.this, MainActivity.class); // Create intent to change back to main activity
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(openMainActivity, 0);
        } else { // if one hasn't been entered
            if (nameEmpty) {
                taskName.setError("A task name is required!"); // inform user of error
            }
            if (descEmpty) {
                taskDesc.setError("A task description is required!"); // inform user of error
            }

        }
    }
}