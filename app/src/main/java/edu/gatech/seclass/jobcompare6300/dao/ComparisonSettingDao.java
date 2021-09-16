package edu.gatech.seclass.jobcompare6300.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;

@Dao
public interface ComparisonSettingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ComparisonSetting comparisonSetting);

    @Query("SELECT * FROM comparison_setting_table WHERE id = 1")
    LiveData<ComparisonSetting> getSettings();

    @Query("UPDATE comparison_setting_table SET commute_time_weight = :commuteTimeWeight, " +
            "yearly_salary_weight = :yearlySalaryWeight, yearly_bonus__weight = :yearlyBonusWeight," +
            "retirement_benefits_weight = :retirementBenefitsWeight, leave_time_weight = :leaveTimeWeight ")
    void update(int commuteTimeWeight, int yearlySalaryWeight, int yearlyBonusWeight,
                int retirementBenefitsWeight, int leaveTimeWeight);
}
