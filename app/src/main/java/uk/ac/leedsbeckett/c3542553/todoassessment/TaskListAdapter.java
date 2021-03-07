package uk.ac.leedsbeckett.c3542553.todoassessment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/*
Task List Adapter. Code based on: https://github.com/google-developer-training/android-fundamentals/blob/master/RecyclerView/app/src/main/java/com/example/android/recyclerview/WordListAdapter.java
Jake Hopkins c3542553
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final LayoutInflater mInflater; // LayoutInflater is used to duplicate the TaskListAdapter for each task

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView taskItemView; // Task name
        public final CheckBox taskCheckView; // Task completion status
        final TaskListAdapter mAdapter; // Connects the recyclerview to the layout inflater

        public TaskViewHolder(View itemView, TaskListAdapter adapter) {
            super(itemView);
            taskItemView = itemView.findViewById(R.id.task); // Identify object
            taskCheckView = itemView.findViewById(R.id.task_tickbox); // Identify object
            taskCheckView.setOnClickListener(new View.OnClickListener() { // Add click listener to checkboxes
                @Override
                public void onClick(View v) {
                    int mPosition = getLayoutPosition(); // Get which task had status change
                    MainActivity.taskStatus.set(mPosition, taskCheckView.isChecked()); // Update status in list
                    MainActivity.dataSave();
                }
            });
            this.mAdapter = adapter; // Connect internal adapter to main adapter
            itemView.setOnClickListener(this); // Assign click listener
        }

        //Detect when task is clicked in the list.
        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition(); // Get pos of task clicked.
            Intent intent = new Intent(MainActivity.mContext, EditTaskActivity.class); // Create intent to change to edit activity
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK); // Edit task needs to be opened from start every time to load in new task
            intent.putExtra("position", mPosition); // add position of task (number) into intent
            Log.d("Task intent", String.valueOf(intent.getIntExtra("position",-1)));
            MainActivity.mContext.startActivity(intent); // Start activity
        }
    }

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context); //Create inflater object from the MainActivity context
    }

    @NonNull
    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.todolist_item,
                parent, false); // Identify the "Item view" as the inflatable fragment
        return new TaskViewHolder(mItemView, this); // Return the inflatable fragment object
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskViewHolder holder, int position) {
        String mCurrent = MainActivity.taskList.get(position); // Get task name from list
        holder.taskItemView.setText(mCurrent);  // Set the text in the fragment to the task name
        holder.taskCheckView.setChecked(MainActivity.taskStatus.get(position)); // Set the status of the task in the checked box
    }

    @Override
    public int getItemCount() {
        return MainActivity.taskList.size(); // Return the length of the task list / amount of tasks
    }
}
