package com.example.mdo.cm_tp_todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";

    public static final String KEY_DATE_INIT = "dateInit";
    public static final String KEY_DATE_END = "dateEnd";
    public static final String KEY_PRIORITY = "priority";

    public static final String KEY_ROWID = "_id";


    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Declaração SQL para criar a base de dados
     */
    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, " +
                    "title text not null, " +
                    "body text not null, " +
                    "dateInit integer, " +
                    "dateEnd integer, " +
                    "priority integer);";


    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //Quando  se corre o programa é efectuada a declaração de
        //criação da base de dados
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        // Quando se faz alguma acção que altera a base de dados
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    /**
     * Construtor –  A partir do contexto permite que a base de dados
     * seja aberta e criada
     *
     * @param ctx o contexto em que se irá trabalhar
     */
    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Abrir a bases de dados das notas. Se não se conseguir abrir
     * tenta-se criar uma nova instancia da base de dados.
     * Se não se conseguir criar, manda uma excepção para sinalizar o
     * fracasso
     *
     * @return this
     * @throws SQLException se a base de dados não conseguir nem ser
     * criada nem aberta
     */
    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    /**
     * Criar uma nova nota utilizando o titulo e o corpo da nota. Se a
     * nota for criada com sucesso retorna-se o rowId da nota, se não
     * -1 para indicar o fracasso.
     *
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createNote(String title, String body, Long dateInit, Long dateEnd, Integer priority ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_DATE_INIT, dateInit);
        initialValues.put(KEY_DATE_END, dateEnd);
        initialValues.put(KEY_PRIORITY, priority);
        //insere se na base de dados os valores
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Apagar uma nota dado o seu rowId
     *
     * @param rowId id da nota a apagar
     * @return true se for apagada, false caso contrario
     */
    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Apagar uma nota dado o seu titulo
     *
     * @param task id da nota a apagar
     * @return true se for apagada, false caso contrario
     */
    public void deleteNote(String task) {

        mDb.delete( DATABASE_TABLE, KEY_TITLE + " = ?",
                new String[]{task});
        //return mDb.delete(DATABASE_TABLE, KEY_TITLE + "=" + new String[]{title}, null) > 0;
    }

    /**
     * Retorna o Cursor sobre a lista de todas as notas da base de
     * dados
     * @return Cursor sobre todas as notas
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,KEY_BODY,KEY_DATE_INIT,KEY_DATE_END,KEY_PRIORITY}, null, null, null, null, null);
    }

    /**
     * Retorna o Cursor posicionado sobre a nota que tem rowId indicado
     *
     * @param rowId id da nota a recuperar
     * @return Cursor posicionado na nota se a encontrar
     * @throws SQLException se a nota não for encontrada
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,KEY_BODY,KEY_DATE_INIT,KEY_DATE_END,KEY_PRIORITY}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update de uma nota
     *
     * @param rowId id da nota a modificar
     * @param title valor para alterar o titulo da nota
     * @param body valor para alterar o corpo da nota
     * @return true se foi alterada com sucesso, false caso contrario
     */
    public boolean updateNote(long rowId, String title, String body, Integer dateInit, Integer dateEnd, Integer priority) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_DATE_INIT, body);
        args.put(KEY_DATE_END, body);
        args.put(KEY_PRIORITY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
