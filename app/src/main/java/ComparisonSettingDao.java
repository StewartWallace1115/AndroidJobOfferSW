package edu.gatech.seclass.jobcompare6300.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;

@Dao
public interface ComparisonSettingDao {


    /**
     * Inserts comparisonSetting object into database.
     *
     * param comparisonSetting to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ComparisonSetting comparisonSetting);

    /**
     * Retrieves comparisonSetting from database.
     *
     * returns comparisonSetting 
     */
    @Query("SELECT * FROM comparison_setting_table")
    LiveData<ComparisonSetting> getSettings();


    /**
     * Updates comparisonSetting in database
     *
     */
    @Query("UPDATE comparison_setting_table SET commute_time_weight = :commuteTimeWeight, " +
            "yearly_salary_weight = :yearlySalaryWeight, yearly_bonus__weight = :yearlyBonusWeight," +
            "retirement_benefits_weight = :retirementBenefitsWeight, leave_time_weight = :leaveTimeWeight ")
    void update(int commuteTimeWeight, int yearlySalaryWeight, int yearlyBonusWeight,
                int retirementBenefitsWeight, int leaveTimeWeight);
}
