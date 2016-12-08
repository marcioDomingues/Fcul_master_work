package com.example.mdo.cm_tp_todolist.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mdo.cm_tp_todolist.TaskContract;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        /*  SQLITE TABLE
            CREATE TABLE tasks (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL
               );
       */


        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + "   INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                TaskContract.TaskEntry.COL_TASK_TITLE + "   TEXT NOT NULL,  " +
                TaskContract.TaskEntry.COL_DATETIME_CREATION + "   INTEGER,  )";
                //+
               // TaskContract.TaskEntry.COL_DATETIME_COMPLETION  + " INTEGER," +
               // TaskContract.TaskEntry.COL_PRIORITY + " INTEGER);";
        //TODO REVIEW THE FIELDS
        // NOTE
        // And inserting the data includes this:
        //values.put(COLUMN_DATETIME, System.currentTimeMillis());

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
