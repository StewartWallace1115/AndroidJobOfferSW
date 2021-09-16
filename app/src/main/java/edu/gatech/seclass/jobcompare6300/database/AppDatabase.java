package edu.gatech.seclass.jobcompare6300.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.gatech.seclass.jobcompare6300.dao.ComparisonSettingDao;
import edu.gatech.seclass.jobcompare6300.dao.JobDetailDao;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;

// Database code template from: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
@Database(entities = {JobDetail.class, ComparisonSetting.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService DATABASE_WRITE_EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppDatabase appDatabaseInstance;

    private static RoomDatabase.Callback defaultComparisonSetting = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            DATABASE_WRITE_EXECUTOR.execute(() -> {
                ComparisonSettingDao dao = appDatabaseInstance.comparisonSettingDao();
                ComparisonSetting comparisonSetting =
                        new ComparisonSetting(1, 1, 1, 1, 1);
                dao.insert(comparisonSetting);
            });
        }
    };


    public static AppDatabase getDatabase(final Context context) {
        if (appDatabaseInstance == null) {
            synchronized (AppDatabase.class) {
                if (appDatabaseInstance == null) {
                    appDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, Constants.JOB_COMPARE_DATABASE).addCallback(defaultComparisonSetting)
                            .build();
                    context.deleteDatabase(Constants.JOB_COMPARE_DATABASE);
                }
            }
        }
        return appDatabaseInstance;
    }

    // For testing purposes
    public static void deleteDatabase(final Context context) {
        context.deleteDatabase(Constants.JOB_COMPARE_DATABASE);

        if (appDatabaseInstance != null) {
            appDatabaseInstance = null;
        }
    }

    public abstract ComparisonSettingDao comparisonSettingDao();

    public abstract JobDetailDao jobDetailDao();
}
