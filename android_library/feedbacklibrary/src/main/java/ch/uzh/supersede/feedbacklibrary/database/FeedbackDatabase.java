package ch.uzh.supersede.feedbacklibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackState;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.ObjectUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.ALL;


public class FeedbackDatabase extends AbstractFeedbackDatabase {


    private static FeedbackDatabase instance = null;
    private FeedbackDbHelper databaseHelper = null;

    public static FeedbackDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackDatabase(context);
        }
        return instance;
    }

    private FeedbackDatabase(Context context) {
        databaseHelper = new FeedbackDbHelper(context);
    }


    public long writeDouble(String key, Double value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeFloat(String key, Float value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeInteger(String key, Integer value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeLong(String key, Long value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeShort(String key, Short value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeString(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(TextTableEntry.TABLE_NAME, TextTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public long writeByte(String key, byte[] value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value);
        return insert(DataTableEntry.TABLE_NAME, DataTableEntry.COLUMN_NAME_KEY, key, values);
    }

    public List<LocalFeedbackBean> getFeedbackBeans(Enums.FETCH_MODE mode) {
        String selection = ONE + EQ + ONE;
        switch (mode) {
            case OWN:
                selection = FeedbackTableEntry.COLUMN_NAME_OWNER + EQ + ONE;
                break;
            case VOTED:
                selection = FeedbackTableEntry.COLUMN_NAME_VOTED + NEQ + ZERO;
                break;
            case UP_VOTED:
                selection = FeedbackTableEntry.COLUMN_NAME_VOTED + EQ + ONE;
                break;
            case DOWN_VOTED:
                selection = FeedbackTableEntry.COLUMN_NAME_VOTED + EQ + MIN_ONE;
                break;
            case SUBSCRIBED:
                selection = FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED + EQ + ONE;
                break;
            case RESPONDED:
                selection = FeedbackTableEntry.COLUMN_NAME_RESPONDED + EQ + ONE;
                break;
            default:
                break;
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(FeedbackTableEntry.TABLE_NAME, new String[]{
                FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID,
                FeedbackTableEntry.COLUMN_NAME_TITLE,
                FeedbackTableEntry.COLUMN_NAME_VOTES,
                FeedbackTableEntry.COLUMN_NAME_RESPONSES,
                FeedbackTableEntry.COLUMN_NAME_STATUS,
                FeedbackTableEntry.COLUMN_NAME_OWNER,
                FeedbackTableEntry.COLUMN_NAME_CREATION_TIMESTAMP,
                FeedbackTableEntry.COLUMN_NAME_VOTED,
                FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP,
                FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED,
                FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP,
                FeedbackTableEntry.COLUMN_NAME_RESPONDED,
                FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP}, selection, null, null, null, null, null);
        ArrayList<LocalFeedbackBean> beans = new ArrayList<>();
        while (cursor.moveToNext()) {
            LocalFeedbackBean bean = new LocalFeedbackBean(cursor);
            beans.add(bean);
        }
        cursor.close();
        db.close();
        return beans;
    }

    public LocalFeedbackState getFeedbackState(FeedbackBean feedbackBean) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(FeedbackTableEntry.TABLE_NAME, new String[]{
                FeedbackTableEntry.COLUMN_NAME_OWNER,
                FeedbackTableEntry.COLUMN_NAME_VOTED,
                FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED,
                FeedbackTableEntry.COLUMN_NAME_RESPONDED}, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID + LIKE + QUOTES + feedbackBean
                .getFeedbackUid()
                .toString() + QUOTES, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            LocalFeedbackState localFeedbackState = new LocalFeedbackState(cursor);
            cursor.close();
            db.close();
            return localFeedbackState;
        }
        cursor.close();
        db.close();
        return new LocalFeedbackState(0, 0, 0, 0);
    }

    public long writeFeedback(FeedbackBean feedbackBean, Enums.SAVE_MODE mode) {
        ContentValues values = new ContentValues();
        int owner = 0;
        int voted = 0;
        long votedTime = 0;
        int subscribed = 0;
        long subscribedTime = 0;
        int responded = 0;
        long respondedTime = 0;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(FeedbackTableEntry.TABLE_NAME, new String[]{
                FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED,
                FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP,
                FeedbackTableEntry.COLUMN_NAME_VOTED,
                FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP,
                FeedbackTableEntry.COLUMN_NAME_RESPONDED,
                FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP}, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID + LIKE + QUOTES + feedbackBean
                .getFeedbackUid()
                .toString() + QUOTES, null, null, null, null, null);
        long newRowId = 0;
        if (cursor.moveToFirst()) {
            subscribed = cursor.getInt(0);
            subscribedTime = cursor.getLong(1);
            voted = cursor.getInt(2);
            votedTime = cursor.getLong(3);
            responded = cursor.getInt(4);
            respondedTime = cursor.getLong(5);
            deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID, feedbackBean.getFeedbackUid().toString());
        }
        switch (mode) {
            case CREATED:
                owner = 1;
                break;
            case UP_VOTED:
                voted++;
                votedTime = System.currentTimeMillis();
                break;
            case DOWN_VOTED:
                voted--;
                votedTime = System.currentTimeMillis();
                break;
            case SUBSCRIBED:
                subscribed = 1;
                subscribedTime = System.currentTimeMillis();
                break;
            case UN_SUBSCRIBED:
                subscribed = 0;
                subscribedTime = 0;
                break;
            case RESPONDED:
                responded = 1;
                respondedTime = System.currentTimeMillis();
                break;
        }
        values.put(FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID, feedbackBean.getFeedbackUid().toString());
        values.put(FeedbackTableEntry.COLUMN_NAME_TITLE, feedbackBean.getTitle());
        values.put(FeedbackTableEntry.COLUMN_NAME_VOTES, feedbackBean.getUpVotes());
        values.put(FeedbackTableEntry.COLUMN_NAME_RESPONSES, feedbackBean.getResponses());
        values.put(FeedbackTableEntry.COLUMN_NAME_STATUS, feedbackBean.getFeedbackStatus().getLabel());
        values.put(FeedbackTableEntry.COLUMN_NAME_OWNER, owner);
        values.put(FeedbackTableEntry.COLUMN_NAME_CREATION_TIMESTAMP, feedbackBean.getTimeStamp());
        values.put(FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED, subscribed);
        values.put(FeedbackTableEntry.COLUMN_NAME_SUBSCRIBED_TIMESTAMP, subscribedTime);
        values.put(FeedbackTableEntry.COLUMN_NAME_VOTED, voted);
        values.put(FeedbackTableEntry.COLUMN_NAME_VOTED_TIMESTAMP, votedTime);
        values.put(FeedbackTableEntry.COLUMN_NAME_RESPONDED, responded);
        values.put(FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP, respondedTime);

        //Removal
        if (subscribed == 0 && owner == 0 && voted == 0 && responded == 0) {
            deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID, feedbackBean.getFeedbackUid().toString());
        } else {
            newRowId = db.insert(FeedbackTableEntry.TABLE_NAME, "null", values);
        }

        cursor.close();
        db.close();
        return newRowId;
    }

    public void wipeAllStoredFeedback() {
        List<LocalFeedbackBean> feedbackBeans = getFeedbackBeans(ALL);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        for (LocalFeedbackBean feedbackBean : feedbackBeans) {
            deleteFeedbackWithoutClose(db, feedbackBean);
        }
        db.close();
    }

    public void deleteFeedbackWithoutClose(SQLiteDatabase db, LocalFeedbackBean feedbackBean) {
        deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID, feedbackBean.getFeedbackUid().toString());
    }

    public void deleteFeedback(LocalFeedbackBean feedbackBean) {
        delete(FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_UID, feedbackBean.getFeedbackUid().toString());
    }

    public Double readDouble(String key, Double valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Double d = null;
        if (cursor.moveToFirst()) {
            d = cursor.getDouble(0);
        }
        cursor.close();
        return ObjectUtility.nvl(d, valueIfNull);
    }

    public Float readFloat(String key, Float valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Float f = null;
        if (cursor.moveToFirst()) {
            f = cursor.getFloat(0);
        }
        cursor.close();
        return ObjectUtility.nvl(f, valueIfNull);
    }

    public Integer readInteger(String key, Integer valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Integer i = null;
        if (cursor.moveToFirst()) {
            i = cursor.getInt(0);
        }
        cursor.close();
        return ObjectUtility.nvl(i, valueIfNull);
    }

    public Long readLong(String key, Long valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Long l = null;
        if (cursor.moveToFirst()) {
            l = cursor.getLong(0);
        }
        cursor.close();
        return ObjectUtility.nvl(l, valueIfNull);
    }

    public Short readShort(String key, Short valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Short s = null;
        if (cursor.moveToFirst()) {
            s = cursor.getShort(0);
        }
        cursor.close();
        return ObjectUtility.nvl(s, valueIfNull);
    }

    public String readString(String key, String valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TextTableEntry.TABLE_NAME, new String[]{TextTableEntry.COLUMN_NAME_VALUE}, TextTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null, null,
                null);
        String s = null;
        if (cursor.moveToFirst()) {
            s = cursor.getString(0);
        }
        cursor.close();
        return ObjectUtility.nvl(s, valueIfNull);
    }

    public byte[] readBytes(String key) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DataTableEntry.TABLE_NAME, new String[]{DataTableEntry.COLUMN_NAME_VALUE}, DataTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null, null,
                null);
        byte[] b = null;
        if (cursor.moveToFirst()) {
            b = cursor.getBlob(0);
        }
        cursor.close();
        return b;
    }

    public void deleteNumber(String key) {
        delete(NumberTableEntry.TABLE_NAME, NumberTableEntry.COLUMN_NAME_KEY, key);
    }

    public void deleteString(String key) {
        delete(TextTableEntry.TABLE_NAME, TextTableEntry.COLUMN_NAME_KEY, key);
    }

    public void deleteData(String key) {
        delete(DataTableEntry.TABLE_NAME, DataTableEntry.COLUMN_NAME_KEY, key);
    }

    private void delete(String tableName, String keyColumn, String key) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selection = keyColumn + LIKE_WILDCARD;
        db.delete(tableName, selection, new String[]{key});
        db.close();
    }

    private void deleteWithoutClose(SQLiteDatabase db, String tableName, String keyColumn, String key) {
        String selection = keyColumn + LIKE_WILDCARD;
        db.delete(tableName, selection, new String[]{key});
    }

    private long insert(String tableName, String keyColumn, String key, ContentValues values) {
        delete(tableName, keyColumn, key);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long newRowId;
        newRowId = db.insert(tableName, "null", values);
        db.close();
        return newRowId;
    }
}
