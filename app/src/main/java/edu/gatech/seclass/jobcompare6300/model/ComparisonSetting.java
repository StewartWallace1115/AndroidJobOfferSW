package edu.gatech.seclass.jobcompare6300.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "comparison_setting_table", primaryKeys = "id")
public class ComparisonSetting {

    @ColumnInfo(name = "commute_time_weight", defaultValue = "1")
    private int commuteTimeWeight = 1;

    @ColumnInfo(name = "yearly_salary_weight", defaultValue = "1")
    private int yearlySalaryWeight = 1;

    @ColumnInfo(name = "yearly_bonus__weight", defaultValue = "1")
    private int yearlyBonusWeight = 1;

    @ColumnInfo(name = "retirement_benefits_weight", defaultValue = "1")
    private int retirementBenefitsWeight = 1;

    @ColumnInfo(name = "leave_time_weight", defaultValue = "1")
    private int leaveTimeWeight = 1;

    // Only a single row so use a single id.
    @ColumnInfo(name = "id", defaultValue = "1")
    private int id = 1;

    public ComparisonSetting(int commuteTimeWeight, int yearlySalaryWeight, int yearlyBonusWeight,
                             int retirementBenefitsWeight, int leaveTimeWeight) {
        this.commuteTimeWeight = commuteTimeWeight;
        this.yearlySalaryWeight = yearlySalaryWeight;
        this.yearlyBonusWeight = yearlyBonusWeight;
        this.retirementBenefitsWeight = retirementBenefitsWeight;
        this.leaveTimeWeight = leaveTimeWeight;
    }

    @Override
    public String toString() {
        return "ComparisonSettingObject [" +
                "commuteTimeWeight=" + commuteTimeWeight + ", " +
                "yearlySalaryWeight=" + yearlySalaryWeight + ", " +
                "yearlyBonusWeight=" + yearlyBonusWeight + ", " +
                "retirementBenefitsWeight=" + retirementBenefitsWeight + ", " +
                "leaveTimeWeight=" + leaveTimeWeight +
                "]";
    }
}
