package com.example.mdo.cm_tp_todolist;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mdo.cm_tp_todolist.db.NotesDbAdapter;

import java.util.ArrayList;

//import com.example.mdo.cm_tp_todolist.db.TaskDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //private TaskDbHelper mHelper;
    // Instanciação de um adaptador e abertura deste
    private NotesDbAdapter mDbHelper;
    private Cursor mNotesCursor;




    private ListView mTaskListView;

    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //mHelper = new TaskDbHelper(this);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        //get list view
        mTaskListView = (ListView) findViewById(R.id.list_todo);



        updateUI();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:

                LayoutInflater factory = LayoutInflater.from(this);

                //text_entry is an Layout XML file containing two text field to display in alert dialog
                final View textEntryView = factory.inflate(R.layout.text_entry, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.title_);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.body_);
                final EditText input3 = (EditText) textEntryView.findViewById(R.id.priority_);

                //input1.setText("", TextView.BufferType.EDITABLE);
                //input2.setText("", TextView.BufferType.EDITABLE);
                //input3.setText("", TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Enter the Text:")
                        .setView(textEntryView)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                //Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                                //Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
                                //Log.i("AlertDialog","TextEntry 3 Entered "+input3.getText().toString());

                                String title = String.valueOf(input1.getText());
                                String body = String.valueOf(input2.getText());
                                long dateInit = System.currentTimeMillis();
                                long dateEnd = 0;
                                int priority = Integer.parseInt(input3.getText().toString());

                                mDbHelper.createNote(title, body, dateInit, dateEnd, priority);

                                updateUI();
                        /* User clicked OK so do some stuff */
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                        //.setNegativeButton("Cancel",
                        //new DialogInterface.OnClickListener() {
                        //    public void onClick(DialogInterface dialog,
                        //                        int whichButton) {
                        //    /*
                        //     * User clicked cancel so do some stuff
                        //    */
                        //    }
                        //});
                alert.show();



                /*


                //Log.d(TAG, "Add a new task");
                final EditText taskEditName = new EditText(this);
                final EditText taskEditPriority = new EditText(this);

                //final EditText taskEditText = new EditText(this);


                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        //.setView(taskEditText)
                        .setView(taskEditName)
                        //.setView(taskEditPriority)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //String task = String.valueOf(taskEditText.getText());
                                //Log.d(TAG, "Task to add: " + task);

                                String task = String.valueOf(taskEditName.getText());
                                //int priority = Integer.parseInt(taskEditPriority.getText().toString());
                                //priority = 0;

                                mDbHelper.createNote(task, " ", 0, 0, 0);

                                */
/*
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                //insert values from new task
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task );
                                values.put(TaskContract.TaskEntry.COL_DATETIME_CREATION, System.currentTimeMillis() );
                                //values.put(TaskContract.TaskEntry.COL_DATETIME_COMPLETION, 0 );
                                //values.put(TaskContract.TaskEntry.COL_PRIORITY, 0 );


                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                 *//*

                            }
                        })

                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();


                updateUI();
*/


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {

        ArrayList<String> taskList = new ArrayList<>();

        mNotesCursor = mDbHelper.fetchAllNotes();
        //startManagingCursor(mNotesCursor);


        //SQLiteDatabase db = mHelper.getReadableDatabase();

        //Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
        //        new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
        //        null, null, null, null, null);

        while (mNotesCursor.moveToNext()) {
            int idx = mNotesCursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(mNotesCursor.getString(idx));
        }

        if (mAdapter == null) {

            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);

            mTaskListView.setAdapter(mAdapter);

        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        mNotesCursor.close();
        //db.close();

    }

   /* public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }*/

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());

        mDbHelper.deleteNote( task );

        //db.close();
        updateUI();
    }

}