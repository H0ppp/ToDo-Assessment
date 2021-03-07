package uk.ac.leedsbeckett.c3542553.todoassessment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

/*
Main Activity - Task List Assignment
Jake Hopkins c3542553
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    public static LinkedList<String> taskList = new LinkedList<>(); //Create the list for task names
    public static LinkedList<String> descList = new LinkedList<>(); //Create the list for task descriptions
    public static LinkedList<Boolean> taskStatus = new LinkedList<>(); //Create the list for task status
    public static Context mContext; // Get context to change activity in other classes
    private static SharedPreferences sharedPref; // All data saved to device
    TextView noTask; // Messages to say no tasks
    TextView noTaskPrompt; // Messages to say no tasks
    RecyclerView mRecyclerView; // Main recycler view
    TaskListAdapter mAdapter; // Bridge between main recycler and the task fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("taskData", MODE_PRIVATE); // Load data
        setContentView(R.layout.activity_main);
        noTask = findViewById(R.id.text_notasks); // Identify no task prompt
        noTaskPrompt = findViewById(R.id.text_notasks_prompt); // Identify no task prompt
        mContext = getBaseContext(); // Assign context
        mRecyclerView = findViewById(R.id.list_view); // Identify the recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)); // Add the dividers between the tasks on the list
        Log.d(TAG, "create");
        // Add task button
        FloatingActionButton fab = findViewById(R.id.fab_addtask); // Identify FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class)); // Start Add task activity on fab click
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataLoad(); // Load all data from saved info
        Log.d(TAG, "start");
        mAdapter = new TaskListAdapter(this); // Update the adapter to show the new list
        mRecyclerView.setAdapter(mAdapter); // Update the adapter to show the new list
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy");
        dataSave(); // Save all data in lists to the device
    }

    protected void onStop() {
        super.onStop();
        Log.d("DATA", "stop");
        dataSave(); // Save all data in lists to the device
    }

    public void dataLoad() {
        int length = sharedPref.getInt("LENGTH", -1); // Pull length value from data
        Log.d(TAG, String.valueOf(length)); // Print length to log for error checking
        if (taskList.isEmpty()) { // Check if data is currently cached
            if (length > 0) { // check for saved data
                for (int i = 0; i < length; i++) { // iterate through saved data and cache
                    taskList.addLast(sharedPref.getString("Task Name: " + i, null));
                    descList.addLast(sharedPref.getString("Task Desc: " + i, null));
                    taskStatus.addLast(sharedPref.getBoolean("Task Status: " + i, false));
                }
                noTask.setVisibility(View.INVISIBLE); // Hide task prompts if tasks exist
                noTaskPrompt.setVisibility(View.INVISIBLE); // Hide task prompts if tasks exist
            } else {
                noTask.setVisibility(View.VISIBLE); // Show task prompts if tasks don't exist
                noTaskPrompt.setVisibility(View.VISIBLE); // Show task prompts if tasks don't exist
            }
        } else {
            noTask.setVisibility(View.INVISIBLE); // Hide task prompts if tasks exist
            noTaskPrompt.setVisibility(View.INVISIBLE); // Hide task prompts if tasks exist
        }
    }

    public static void dataSave() {
        SharedPreferences.Editor editor = sharedPref.edit(); // Create editor of stored data
        for (int i = 0; i < taskList.size(); i++) {
            editor.putString("Task Name: " + i, taskList.get(i)); // Add the strings in the lists into the stored data
        }
        for (int i = 0; i < descList.size(); i++) {
            editor.putString("Task Desc: " + i, descList.get(i)); // Add the strings in the lists into the stored data
        }
        for (int i = 0; i < taskStatus.size(); i++) {
            editor.putBoolean("Task Status: " + i, taskStatus.get(i)); // Add the booleans in the lists into the stored data
        }
        editor.putInt("LENGTH", taskList.size()); // Add the length to the data to allow iteration when reloading them
        editor.commit();
        Log.d(TAG, "Data saved"); // Confirmation
        Log.d(TAG, "length: " + taskList.size()); // Log length for error checking
    }
}