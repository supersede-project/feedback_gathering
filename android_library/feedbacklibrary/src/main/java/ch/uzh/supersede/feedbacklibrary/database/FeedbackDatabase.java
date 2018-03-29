package ch.uzh.supersede.feedbacklibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.uzh.supersede.feedbacklibrary.utils.ObjectUtility;


public class FeedbackDatabase extends AbstractFeedbackDatabase {
    private static FeedbackDatabase instance = null;
    private FeedbackDbHelper m_databaseHelper = null;

    public static FeedbackDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackDatabase(context);
        }
        return instance;
    }

    private FeedbackDatabase(Context context) {
        m_databaseHelper = new FeedbackDbHelper(context);
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

    public Double readDouble(String key, Double valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        Double d = null;
        if (cursor.moveToFirst()) {
            d = cursor.getDouble(0);
        }
        return ObjectUtility.nvl(d, valueIfNull);
    }

    public Float readFloat(String key, Float valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        Float f = null;
        if (cursor.moveToFirst()) {
            f = cursor.getFloat(0);
        }
        return ObjectUtility.nvl(f, valueIfNull);
    }

    public Integer readInteger(String key, Integer valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        Integer i = null;
        if (cursor.moveToFirst()) {
            i = cursor.getInt(0);
        }
        return ObjectUtility.nvl(i, valueIfNull);
    }

    public Long readLong(String key, Long valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        Long l = null;
        if (cursor.moveToFirst()) {
            l = cursor.getLong(0);
        }
        return ObjectUtility.nvl(l, valueIfNull);
    }

    public Short readShort(String key, Short valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        Short s = null;
        if (cursor.moveToFirst()) {
            s = cursor.getShort(0);
        }
        return ObjectUtility.nvl(s, valueIfNull);
    }

    public String readString(String key, String valueIfNull) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TextTableEntry.TABLE_NAME, new String[]{TextTableEntry.COLUMN_NAME_VALUE}, TextTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        String s = null;
        if (cursor.moveToFirst()) {
            s = cursor.getString(0);
        }
        return ObjectUtility.nvl(s, valueIfNull);
    }

    public byte[] readBytes(String key) {
        SQLiteDatabase db = m_databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DataTableEntry.TABLE_NAME, new String[]{DataTableEntry.COLUMN_NAME_VALUE}, DataTableEntry.COLUMN_NAME_KEY + " LIKE '" + key + "'", null, null, null, null, null);
        byte[] b = null;
        if (cursor.moveToFirst()) {
            b = cursor.getBlob(0);
        }
        return b;
    }

    public void deleteNumber(String key){
        delete(NumberTableEntry.TABLE_NAME,NumberTableEntry.COLUMN_NAME_KEY,key);
    }
    public void deleteString(String key){
        delete(TextTableEntry.TABLE_NAME,TextTableEntry.COLUMN_NAME_KEY,key);
    }
    public void deleteData(String key){
        delete(DataTableEntry.TABLE_NAME,DataTableEntry.COLUMN_NAME_KEY,key);
    }


    private void delete(String tableName, String keyColumn, String key){
        SQLiteDatabase db = m_databaseHelper.getWritableDatabase();
        String selection = keyColumn + " LIKE ?";
        db.delete(tableName, selection, new String[]{key});
        db.close();
    }

    private long insert(String tableName, String keyColumn, String key, ContentValues values) {
        delete(tableName,keyColumn,key);
        SQLiteDatabase db = m_databaseHelper.getWritableDatabase();
        long newRowId;
        newRowId = db.insert(tableName, "null", values);
        db.close();
        return newRowId;
    }
}
