package edu.gatech.seclass.jobcompare6300.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import edu.gatech.seclass.jobcompare6300.database.AppDatabase;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;

public class ComparisonSettingRepository {

    private ComparisonSettingDao comparisonSettingDao;
    private LiveData<ComparisonSetting> comparisonSetting;

    public ComparisonSettingRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        comparisonSettingDao = database.comparisonSettingDao();
        comparisonSetting = comparisonSettingDao.getSettings();
    }


    public LiveData<ComparisonSetting> getSettings() {
        return comparisonSetting;
    }

    /**
     * Inserts comparisonSetting object into database 
     */ 
    public void insert(ComparisonSetting comparisonSetting) {
        AppDatabase.DATABASE_WRITE_EXECUTOR.execute(() -> {
            comparisonSettingDao.insert(comparisonSetting);
        });
    }

    /**
     * Updates comparisonSetting object in database 
     */ 
    public void updateSettings(ComparisonSetting comparisonSetting) {
        AppDatabase.DATABASE_WRITE_EXECUTOR.execute(() -> {
            comparisonSettingDao.update(comparisonSetting.getCommuteTimeWeight(), comparisonSetting.getYearlySalaryWeight(),
                    comparisonSetting.getYearlyBonusWeight(), comparisonSetting.getRetirementBenefitsWeight(), comparisonSetting.getLeaveTimeWeight());
        });
    }
}
