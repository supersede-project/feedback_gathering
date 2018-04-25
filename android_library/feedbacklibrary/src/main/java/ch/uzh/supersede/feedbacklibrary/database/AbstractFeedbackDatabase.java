package ch.uzh.supersede.feedbacklibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_HOST_APPLICATION_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SHARED_PREFERENCES_ID;

public abstract class AbstractFeedbackDatabase {
    //Datatypes
    protected static final String TEXT_TYPE = " TEXT ";
    protected static final String NUMBER_TYPE = " INTEGER ";
    protected static final String FLOATING_NUMBER_TYPE = " REAL ";
    protected static final String DATA_TYPE = " BLOB ";
    protected static final String KEY_TYPE = " PRIMARY KEY ";
    protected static final String COMMA_SEP = ",";
    public static final String ALTER_TABLE = "ALTER TABLE ";
    public static final String RENAME_TO_TEMP = " RENAME TO 'TEMP_";
    public static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";
    public static final String INSERT_INTO = "INSERT INTO ";
    public static final String SUB_SELECT = " SELECT ";
    public static final String BRACES_OPEN = " ( ";
    public static final String BRACES_CLOSE = " ) ";
    public static final String FROM_TEMP = " FROM TEMP_";
    public static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    public static final String DROP_TABLE_IF_EXISTS_TEMP = "DROP TABLE IF EXISTS TEMP_";
    public static final String LIKE = " LIKE ";
    public static final String EQ = " = ";
    public static final String NEQ = " != ";
    public static final String ONE = " 1 ";
    public static final String MINONE = " -1 ";
    public static final String ZERO = " 0 ";
    public static final String QUOTES = "'";
    public static final String LIKE_WILDCARD = " LIKE ? ";

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

    //Table for Feedback id, title, votes, own, ownCreationTimestamp, subscribed, timestampSubscribed, voted, timeStampVoted
    protected static class FeedbackTableEntry implements BaseColumns {
        public static final String TABLE_NAME = " FEEDBACK_TABLE ";
        public static final String COLUMN_NAME_FEEDBACK_UID = " FEEDBACK_UID ";
        public static final String COLUMN_NAME_TITLE = " TITLE ";
        public static final String COLUMN_NAME_VOTES = " VOTES ";
        public static final String COLUMN_NAME_OWNER = " OWNER ";
        public static final String COLUMN_NAME_CREATION_TIMESTAMP = " CREATION_TIMESTAMP ";
        public static final String COLUMN_NAME_SUBSCRIBED = " SUBSCRIBED ";
        public static final String COLUMN_NAME_SUBSCRIBED_TIMESTAMP = " SUBSCRIBED_TIMESTAMP ";
        public static final String COLUMN_NAME_VOTED = " VOTED ";
        public static final String COLUMN_NAME_VOTED_TIMESTAMP = " VOTED_TIMESTAMP ";
        public static final String COLUMN_NAME_RESPONDED = " RESPONDED ";
        public static final String COLUMN_NAME_RESPONDED_TIMESTAMP = " RESPONDED_TIMESTAMP ";
    }

    //Creation
    private String[] collectTableCreation() {
        return new String[]{
                CREATE_TABLE_IF_NOT_EXISTS + NumberTableEntry.TABLE_NAME + BRACES_OPEN +
                        NumberTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + FLOATING_NUMBER_TYPE + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE + BRACES_CLOSE,
                CREATE_TABLE_IF_NOT_EXISTS + TextTableEntry.TABLE_NAME + BRACES_OPEN +
                        TextTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + TEXT_TYPE + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE  + BRACES_CLOSE,
                CREATE_TABLE_IF_NOT_EXISTS + DataTableEntry.TABLE_NAME + BRACES_OPEN +
                        DataTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_KEY + TEXT_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_VALUE + DATA_TYPE + COMMA_SEP +
                        DataTableEntry.COLUMN_NAME_TIMESTAMP + NUMBER_TYPE  + BRACES_CLOSE,
                CREATE_TABLE_IF_NOT_EXISTS + FeedbackTableEntry.TABLE_NAME + BRACES_OPEN +
                        FeedbackTableEntry._ID + NUMBER_TYPE + KEY_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID + TEXT_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTES + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_OWNER + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_CREATION_TIMESTAMP + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED + NUMBER_TYPE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP + NUMBER_TYPE  + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED + NUMBER_TYPE  + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP + NUMBER_TYPE  + BRACES_CLOSE,};
    }
    //Upgrade
    private String[] collectTableBackup() {
        return new String[]{
                ALTER_TABLE + NumberTableEntry.TABLE_NAME + RENAME_TO_TEMP + NumberTableEntry.TABLE_NAME + "'",
                ALTER_TABLE + TextTableEntry.TABLE_NAME + RENAME_TO_TEMP + TextTableEntry.TABLE_NAME + "'",
                ALTER_TABLE + DataTableEntry.TABLE_NAME + RENAME_TO_TEMP + DataTableEntry.TABLE_NAME + "'",
                ALTER_TABLE + FeedbackTableEntry.TABLE_NAME + RENAME_TO_TEMP + FeedbackTableEntry.TABLE_NAME + "'",
        };
    }
    
    private String[] collectTableDeletion() {
        return new String[]{
                DROP_TABLE_IF_EXISTS + NumberTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS + TextTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS + DataTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS + FeedbackTableEntry.TABLE_NAME,
        };
    }
    private String[] collectTableRestoration() {
        return new String[]{
                INSERT_INTO + NumberTableEntry.TABLE_NAME + BRACES_OPEN + NumberTableEntry._ID + COMMA_SEP + NumberTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + NumberTableEntry.COLUMN_NAME_TIMESTAMP + BRACES_CLOSE + SUB_SELECT + NumberTableEntry._ID + COMMA_SEP + NumberTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + NumberTableEntry.COLUMN_NAME_TIMESTAMP + FROM_TEMP + NumberTableEntry.TABLE_NAME,
                INSERT_INTO + TextTableEntry.TABLE_NAME + BRACES_OPEN + TextTableEntry._ID + COMMA_SEP + TextTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + TextTableEntry.COLUMN_NAME_TIMESTAMP + BRACES_CLOSE + SUB_SELECT + TextTableEntry._ID + COMMA_SEP + TextTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        TextTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + TextTableEntry.COLUMN_NAME_TIMESTAMP + FROM_TEMP + TextTableEntry.TABLE_NAME,
                INSERT_INTO + DataTableEntry.TABLE_NAME + BRACES_OPEN + DataTableEntry._ID + COMMA_SEP + DataTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + DataTableEntry.COLUMN_NAME_TIMESTAMP + BRACES_CLOSE + SUB_SELECT + DataTableEntry._ID + COMMA_SEP + DataTableEntry.COLUMN_NAME_KEY + COMMA_SEP +
                        NumberTableEntry.COLUMN_NAME_VALUE + COMMA_SEP + DataTableEntry.COLUMN_NAME_TIMESTAMP + FROM_TEMP + DataTableEntry.TABLE_NAME,
                INSERT_INTO + FeedbackTableEntry.TABLE_NAME + BRACES_OPEN +
                        FeedbackTableEntry._ID + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_TITLE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTES + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_OWNER + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_CREATION_TIMESTAMP + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP +
                        BRACES_CLOSE + SUB_SELECT +
                        FeedbackTableEntry._ID + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_TITLE + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTES + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_OWNER + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_CREATION_TIMESTAMP + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP +COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED + COMMA_SEP +
                        FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP + FROM_TEMP + FeedbackTableEntry.TABLE_NAME,
        };
    }
    private String[] collectTableCleanup() {
        return new String[]{
                DROP_TABLE_IF_EXISTS_TEMP + NumberTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS_TEMP + TextTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS_TEMP + NumberTableEntry.TABLE_NAME,
                DROP_TABLE_IF_EXISTS_TEMP + FeedbackTableEntry.TABLE_NAME,
        };
    }


    protected class FeedbackDbHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "FeedbackDb";
        private static final String DATABASE_ENDING = ".db";
        private static final String FILE_DIR = "Feedback";

        FeedbackDbHelper(Context context) {
            super(context, Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator + context.getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getString(EXTRA_KEY_HOST_APPLICATION_NAME,"")+DATABASE_NAME+DATABASE_ENDING, null, DATABASE_VERSION);
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
