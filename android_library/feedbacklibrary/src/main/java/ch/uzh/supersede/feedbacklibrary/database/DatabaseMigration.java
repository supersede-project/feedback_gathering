package ch.uzh.supersede.feedbacklibrary.database;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.utils.ConfigurationUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.DATABASE_VERSION;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.DATABASE_VERSION_NUMBER;

public final class DatabaseMigration implements Runnable {
    private final Context context;
    private final LocalConfigurationBean configuration;

    public DatabaseMigration(final Context context, final LocalConfigurationBean configuration) {
        this.context = context;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        boolean isMigrationNeeded = DATABASE_VERSION_NUMBER != FeedbackDatabase.getInstance(context).readDouble(DATABASE_VERSION, 0.0);

        if (isMigrationNeeded) {
            Log.i(getClass().getSimpleName(), "DatabaseMigration started");

            boolean isSuccessful = execDatabaseMigration();
            if (isSuccessful) {
                FeedbackDatabase.newInstance(context).writeDouble(DATABASE_VERSION, DATABASE_VERSION_NUMBER);
                ConfigurationUtility.execStoreStateToDatabase(context, configuration);
            }

            Log.i(getClass().getSimpleName(), "DatabaseMigration ended with " + (isSuccessful ? "SUCCESS" : "ERROR"));
        }
    }

    @SuppressWarnings("squid:S4042")
    private boolean execDatabaseMigration() {
        String databaseName = FeedbackDatabase.getInstance(context).getDatabaseName();
        File file = new File(databaseName);
        return file.delete();
    }
}
