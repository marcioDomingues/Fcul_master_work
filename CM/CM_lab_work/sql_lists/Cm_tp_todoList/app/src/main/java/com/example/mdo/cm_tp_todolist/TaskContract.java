package com.example.mdo.cm_tp_todolist;


import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.mdo.cm_tp_todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_DATETIME_CREATION = "creation_time";
        public static final String COL_DATETIME_COMPLETION = "completion_time";
        public static final String COL_PRIORITY = "priority";
    }
}