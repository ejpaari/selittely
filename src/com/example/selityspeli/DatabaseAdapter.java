package com.example.selityspeli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter {
	
    public static final String KEY_WORD = "word";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DIFFICULTY = "difficulty";
    public static final String KEY_ROWID = "_id";
    
    private static final String TAG = "DatabaseAdapter";
    private static final String DATABASE_CREATE_WORDS =
            "create table words (_id integer primary key autoincrement, word text not null, category text, difficulty integer);";
    private static final String DATABASE_CREATE_PEOPLE =
            "create table people (_id integer primary key autoincrement, name text not null, category text, difficulty integer);";
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;
    
    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
	
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_WORDS);
            db.execSQL(DATABASE_CREATE_PEOPLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS words");
            db.execSQL("DROP TABLE IF EXISTS people");
            onCreate(db);
        }
    }

   public DatabaseAdapter(Context ctx) {
       this.context = ctx;
   }

   public DatabaseAdapter open() throws SQLException {
       databaseHelper = new DatabaseHelper(context);
       database = databaseHelper.getWritableDatabase();
       return this;
   }

   public void close() {
	   databaseHelper.close();
   }

   /**
    * @return rowId or -1 if failed
    */
   public long addWord(String word, String category, int difficulty) {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_WORD, word);
       initialValues.put(KEY_CATEGORY, category);
       initialValues.put(KEY_DIFFICULTY, difficulty);

       return database.insert("words", null, initialValues);
   }
   
   /**
    * @return rowId or -1 if failed
    */
   public long addName(String name, String category, int difficulty) {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_WORD, name);
       initialValues.put(KEY_CATEGORY, category);
       initialValues.put(KEY_DIFFICULTY, difficulty);

       return database.insert("people", null, initialValues);
   }

   public Cursor fetchAllWords() {
       return database.query("words", new String[] {KEY_ROWID, KEY_WORD}, null, null, null, null, null);
   }
   
   public Cursor fetchAllNames() {
       return database.query("people", new String[] {KEY_ROWID, KEY_NAME}, null, null, null, null, null);
   }
}
