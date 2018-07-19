package ch.uzh.supersede.feedbacklibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.ObjectUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ConfigurationConstants.*;
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

    public long writeBoolean(String key, boolean value) {
        ContentValues values = new ContentValues();
        values.put(NumberTableEntry.COLUMN_NAME_KEY, key);
        values.put(NumberTableEntry.COLUMN_NAME_VALUE, value ? 1 : 0);
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
                FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID,
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
                FeedbackTableEntry.COLUMN_NAME_RESPONDED}, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID + LIKE + String.valueOf(feedbackBean.getFeedbackId()), null, null, null, null, null);
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
                FeedbackTableEntry.COLUMN_NAME_RESPONDED_TIMESTAMP}, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID + LIKE + String.valueOf(feedbackBean.getFeedbackId()), null, null, null, null, null);
        long newRowId = 0;
        if (cursor.moveToFirst()) {
            subscribed = cursor.getInt(0);
            subscribedTime = cursor.getLong(1);
            voted = cursor.getInt(2);
            votedTime = cursor.getLong(3);
            responded = cursor.getInt(4);
            respondedTime = cursor.getLong(5);
            deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackBean.getFeedbackId()));
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
        values.put(FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackBean.getFeedbackId()));
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
            deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackBean.getFeedbackId()));
        } else {
            newRowId = db.insert(FeedbackTableEntry.TABLE_NAME, "null", values);
            writeTags(db, feedbackBean.getFeedbackId(), feedbackBean.getTags());
        }

        cursor.close();
        db.close();
        return newRowId;
    }

    private void writeTags(SQLiteDatabase db, Long feedbackId, String[] tags) {
        for (String tag : (tags == null ? new String[0] : tags)) {
            ContentValues values = new ContentValues();
            values.put(TagTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackId));
            values.put(TagTableEntry.COLUMN_NAME_TAG, tag);
            db.insert(TagTableEntry.TABLE_NAME, "null", values);
        }
    }

    public String[] readTags(Long feedbackId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(true, TagTableEntry.TABLE_NAME, new String[]{TagTableEntry.COLUMN_NAME_TAG}, TagTableEntry.COLUMN_NAME_FEEDBACK_ID + (feedbackId == null ? NEQ + ZERO : EQ +
                        feedbackId), null, null, null, null,
                null);
        ArrayList<String> tags = new ArrayList<>();
        while (cursor.moveToNext()) {
            tags.add(cursor.getString(0));
        }
        cursor.close();
        return tags.toArray(new String[tags.size()]);
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
        deleteWithoutClose(db, FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackBean.getFeedbackId()));
    }

    public void deleteFeedback(LocalFeedbackBean feedbackBean) {
        delete(FeedbackTableEntry.TABLE_NAME, FeedbackTableEntry.COLUMN_NAME_FEEDBACK_ID, String.valueOf(feedbackBean.getFeedbackId()));
    }

    public void writeConfiguration(LocalConfigurationBean config) {
        // Host Application
        writeString(CONFIG_HOST_APPLICATION_ID, config.getHostApplicationId());
        writeLong(CONFIG_HOST_APPLICATION_LONG_ID, config.getHostApplicationLongId());
        writeString(CONFIG_HOST_APPLICATION_NAME, config.getHostApplicationName());
        writeString(CONFIG_HOST_APPLICATION_LANGUAGE, config.getHostApplicationLanguage());
        // Miscellaneous
        writeInteger(CONFIG_PULL_INTERVAL_MINUTES, config.getPullIntervalMinutes());
        writeBoolean(CONFIG_IS_DEVELOPER, config.isDeveloper());
        writeString(CONFIG_REPOSITORY_LOGIN, config.getRepositoryLogin());
        writeString(CONFIG_REPOSITORY_PASS, config.getRepositoryPass());
        // Audio / Screenshot
        writeInteger(CONFIG_AUDIO_ORDER, config.getAudioOrder());
        writeDouble(CONFIG_MAX_AUDIO_TIME, config.getMaxAudioTime());
        writeInteger(CONFIG_SCREENSHOT_ORDER, config.getScreenshotOrder());
        writeBoolean(CONFIG_IS_SCREENSHOT_EDITABLE, config.isScreenshotEditable());
        // Rating
        writeInteger(CONFIG_RATING_ORDER, config.getRatingOrder());
        writeString(CONFIG_RATING_TITLE, config.getRatingTitle());
        writeString(CONFIG_RATING_ICON, config.getRatingIcon());
        writeInteger(CONFIG_RATING_DEFAULT_VALUE, config.getDefaultRatingValue());
        writeInteger(CONFIG_MAX_RATING_VALUE, config.getMaxRatingValue());
        // Text
        writeInteger(CONFIG_TEXT_ORDER, config.getTextOrder());
        writeString(CONFIG_TEXT_LABEL, config.getTextLabel());
        writeString(CONFIG_TEXT_HINT, config.getTextHint());
        writeInteger(CONFIG_MAX_TEXT_LENGTH, config.getMaxTextLength());
        writeInteger(CONFIG_MIN_TEXT_LENGTH, config.getMinTextLength());
        // Tag
        writeInteger(CONFIG_MAX_TAG_LENGTH, config.getMaxTagLength());
        writeInteger(CONFIG_MIN_TAG_LENGTH, config.getMinTagLength());
        writeInteger(CONFIG_MAX_TAG_NUMBER, config.getMaxTagNumber());
        writeInteger(CONFIG_MIN_TAG_NUMBER, config.getMinTagNumber());
        writeInteger(CONFIG_MAX_TAG_RECOMMENDATION_NUMBER, config.getMaxTagRecommendationNumber());
        // Title
        writeInteger(CONFIG_MAX_TITLE_LENGTH, config.getMaxTitleLength());
        writeInteger(CONFIG_MIN_TITLE_LENGTH, config.getMinTitleLength());
        // Response
        writeInteger(CONFIG_MAX_RESPONSE_LENGTH, config.getMaxResponseLength());
        writeInteger(CONFIG_MIN_RESPONSE_LENGTH, config.getMinResponseLength());
        // User
        writeInteger(CONFIG_MAX_USER_NAME_LENGTH, config.getMaxUserNameLength());
        writeInteger(CONFIG_MIN_USER_NAME_LENGTH, config.getMinUserNameLength());
        // Style
        writeString(CONFIG_STYLE, config.getStyle().toString());
        writeBoolean(CONFIG_IS_COLORING_VERTICAL, config.isColoringVertical());
        Integer[] colors = config.getTopColors();
        for (int i = 0; i < colors.length; i++) {
            writeInteger(CONFIG_COLOR + i, colors[i]);
        }
        writeInteger(CONFIG_COLORS_LENGTH, colors.length);
    }

    public LocalConfigurationBean readConfiguration() {
        int colorsLength = readInteger(CONFIG_COLORS_LENGTH, 0);
        Integer[] topColors = new Integer[colorsLength];
        for (int i = 0; i < colorsLength; i++) {
            topColors[i] = readInteger(CONFIG_COLOR + i, 0);
        }

        return new LocalConfigurationBean.Builder()
                // Host Application
                .withHostApplicationId(readString(CONFIG_HOST_APPLICATION_ID, null))
                .withHostApplicationLongId(readLong(CONFIG_HOST_APPLICATION_LONG_ID, null))
                .withHostApplicationName(readString(CONFIG_HOST_APPLICATION_NAME, null))
                .withHostApplicationLanguage(readString(CONFIG_HOST_APPLICATION_LANGUAGE, null))
                // Miscellaneous
                .withPullIntervalMinutes(readInteger(CONFIG_PULL_INTERVAL_MINUTES, null))
                .withIsDeveloper(readBoolean(CONFIG_IS_DEVELOPER, null))
                .withRepositoryLogin(readString(CONFIG_REPOSITORY_LOGIN, null))
                .withRepositoryPassword(readString(CONFIG_REPOSITORY_PASS, null))
                // Audio / Screenshot
                .withAudioOrder(readInteger(CONFIG_AUDIO_ORDER, null))
                .withMaxAudioTime(readDouble(CONFIG_MAX_AUDIO_TIME, null))
                .withScreenshotOrder(readInteger(CONFIG_SCREENSHOT_ORDER, null))
                .withIsScreenshotEditable(readBoolean(CONFIG_IS_SCREENSHOT_EDITABLE, null))
                // Rating
                .withRatingOrder(readInteger(CONFIG_RATING_ORDER, null))
                .withRatingTitle(readString(CONFIG_RATING_TITLE, null))
                .withRatingIcon(readString(CONFIG_RATING_ICON, null))
                .withRatingDefaultValue(readInteger(CONFIG_RATING_DEFAULT_VALUE, null))
                .withMaxRatingValue(readInteger(CONFIG_MAX_RATING_VALUE, null))
                // Text
                .withTextOrder(readInteger(CONFIG_TEXT_ORDER, null))
                .withTextLabel(readString(CONFIG_TEXT_LABEL, null))
                .withTextHint(readString(CONFIG_TEXT_HINT, null))
                .withMaxTextLength(readInteger(CONFIG_MAX_TEXT_LENGTH, null))
                .withMinTextLength(readInteger(CONFIG_MIN_TEXT_LENGTH, null))
                // Tag
                .withMaxTagLength(readInteger(CONFIG_MAX_TAG_LENGTH, null))
                .withMinTagLength(readInteger(CONFIG_MIN_TAG_LENGTH, null))
                .withMaxTagNumber(readInteger(CONFIG_MAX_TAG_NUMBER, null))
                .withMinTagNumber(readInteger(CONFIG_MIN_TAG_NUMBER, null))
                .withMaxTagRecommendationNumber(readInteger(CONFIG_MAX_TAG_RECOMMENDATION_NUMBER, null))
                // Title
                .withMaxTitleLength(readInteger(CONFIG_MAX_TITLE_LENGTH, null))
                .withMinTitleLength(readInteger(CONFIG_MIN_TITLE_LENGTH, null))
                // Response
                .withMaxResponseLength(readInteger(CONFIG_MAX_RESPONSE_LENGTH, null))
                .withMinResponseLength(readInteger(CONFIG_MIN_RESPONSE_LENGTH, null))
                // User
                .withMaxUserNameLength(readInteger(CONFIG_MAX_USER_NAME_LENGTH, null))
                .withMinUserNameLength(readInteger(CONFIG_MIN_USER_NAME_LENGTH, null))
                // Style
                .withStyle(IFeedbackStyleConfiguration.FEEDBACK_STYLE.valueOf(readString(CONFIG_STYLE, "DARK")))
                .withIsColoringVertical(readBoolean(CONFIG_IS_COLORING_VERTICAL, null))
                .withTopColors(topColors)
                .build();
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

    public Boolean readBoolean(String key, Boolean valueIfNull) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(NumberTableEntry.TABLE_NAME, new String[]{NumberTableEntry.COLUMN_NAME_VALUE}, NumberTableEntry.COLUMN_NAME_KEY + LIKE + QUOTES + key + QUOTES, null, null, null,
                null, null);
        Integer i = null;
        if (cursor.moveToFirst()) {
            i = cursor.getInt(0);
        }
        cursor.close();
        if (i == null) {
            return valueIfNull;
        }
        return i == 1 ? true : false;
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
