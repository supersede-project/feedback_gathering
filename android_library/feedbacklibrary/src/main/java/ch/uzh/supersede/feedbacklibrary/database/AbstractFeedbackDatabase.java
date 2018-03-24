package ch.uzh.supersede.feedbacklibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;

import java.io.File;

public abstract class AbstractFeedbackDatabase {
    //Datatypes
    protected static final String TEXT_TYPE = " TEXT ";
    protected static final String NUMBER_TYPE = " INTEGER ";
    protected static final String FLOATING_NUMBER_TYPE = " REAL ";
    protected static final String DATA_TYPE = " BLOB ";
    protected static final String KEY_TYPE = " PRIMARY KEY ";
    protected static final String COMMA_SEP = ",";

    //Table for Numbers
    protected static class NumberTableEntry implements BaseColumns {
        public static final String TABLE_NAME = " NUMBER_TABLE ";
        public static final String COLUMN_NAME_KEY = " KEY ";
        public static final String COLUMN_NAME_VALUE = " VALUE ";
        public static final String COLUMN_NAME_TIMESTAMP = " TIMESTAMP ";
    }
    //Table for Texts
    protected static class TextTableEntry implements BaseColumns {
        public static final String TABLE_NAME = " TEXT_TABLE ";
        public static final String COLUMN_NAME_KEY = " KEY ";
        public static final String COLUMN_NAME_VALUE = " VALUE ";
        public static final String COLUMN_NAME_TIMESTAMP = " TIMESTAMP ";
    }
    //Table for Data
    protected static class DataTableEntry implements BaseColumns {
        public static final String TABLE_NAME = " DATA_TABLE ";
        public static final String COLUMN_NAME_KEY = " KEY ";
        public static final String COLUMN_NAME_VALUE = " VALUE ";
        public static final String COLUMN_NAME_TIMESTAMP = " TIMESTAMP ";
    }

    //Creation
    private String[] collectTableCreation() {
        return new String[]{
                "CREATE TABLE IF NOT EXISTS " + NumberTableEntry.TABLE_NAME + " (" +
                        NumberTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + FLOATING_NUMBER_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE + "" + " )",
                "CREATE TABLE IF NOT EXISTS " + TextTableEntry.TABLE_NAME + " (" +
                        TextTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + TEXT_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE + "" + " )",
                "CREATE TABLE IF NOT EXISTS " + DataTableEntry.TABLE_NAME + " (" +
                        DataTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_VALUE + DATA_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE + "" + " )",};
    }
    //Upgrade
    private String[] collectTableBackup() {
        return new String[]{
                "ALTER TABLE " + NumberTableEntry.TABLE_NAME + " RENAME TO 'TEMP_" + NumberTableEntry.TABLE_NAME + "'",
                "ALTER TABLE " + TextTableEntry.TABLE_NAME + " RENAME TO 'TEMP_" + TextTableEntry.TABLE_NAME + "'",
                "ALTER TABLE " + DataTableEntry.TABLE_NAME + " RENAME TO 'TEMP_" + DataTableEntry.TABLE_NAME + "'",
        };
    }
    private String[] collectTableDeletion() {
        return new String[]{
                "DROP TABLE IF EXISTS " + NumberTableEntry.TABLE_NAME,
                "DROP TABLE IF EXISTS " + TextTableEntry.TABLE_NAME,
                "DROP TABLE IF EXISTS " + DataTableEntry.TABLE_NAME,
        };
    }
    private String[] collectTableRestoration() {
        return new String[]{
                "INSERT INTO " + NumberTableEntry.TABLE_NAME + " (" + NumberTableEntry._ID + COMMA_SEP + NumberTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + NumberTableEntry.COLUMN_NAME_TIMESTAMP + ") SELECT " + NumberTableEntry._ID + COMMA_SEP + NumberTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + NumberTableEntry.COLUMN_NAME_TIMESTAMP + " FROM TEMP_" + NumberTableEntry.TABLE_NAME,
                "INSERT INTO " + TextTableEntry.TABLE_NAME + " (" + TextTableEntry._ID + COMMA_SEP + TextTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + TextTableEntry.COLUMN_NAME_TIMESTAMP + ") SELECT " + TextTableEntry._ID + COMMA_SEP + TextTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + TextTableEntry.COLUMN_NAME_TIMESTAMP + " FROM TEMP_" + TextTableEntry.TABLE_NAME,
                "INSERT INTO " + DataTableEntry.TABLE_NAME + " (" + DataTableEntry._ID + COMMA_SEP + DataTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + DataTableEntry.COLUMN_NAME_TIMESTAMP + ") SELECT " + DataTableEntry._ID + COMMA_SEP + DataTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + DataTableEntry.COLUMN_NAME_TIMESTAMP + " FROM TEMP_" + DataTableEntry.TABLE_NAME,
        };
    }
    private String[] collectTableCleanup() {
        return new String[]{
                "DROP TABLE IF EXISTS TEMP_" + NumberTableEntry.TABLE_NAME,
                "DROP TABLE IF EXISTS TEMP_" + TextTableEntry.TABLE_NAME,
                "DROP TABLE IF EXISTS TEMP_" + NumberTableEntry.TABLE_NAME,
        };
    }


    protected class FeedbackDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedbackDb.db";
        public static final String FILE_DIR = "Feedback";

        public FeedbackDbHelper(Context context) {
            super(context, Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            for (String creation : collectTableCreation()){
                db.execSQL(creation);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (String backup : collectTableBackup()){
                db.execSQL(backup);
            }
            for (String deletion : collectTableDeletion()){
                db.execSQL(deletion);
            }
            onCreate(db);
            for (String restoration : collectTableRestoration()){
                db.execSQL(restoration);
            }
            for (String cleanup : collectTableCleanup()){
                db.execSQL(cleanup);
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
