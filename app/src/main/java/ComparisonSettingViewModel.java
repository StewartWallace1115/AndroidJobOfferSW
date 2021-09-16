package edu.gatech.seclass.jobcompare6300.ui.ComparisonSetting;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import edu.gatech.seclass.jobcompare6300.dao.ComparisonSettingRepository;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;

public class ComparisonSettingViewModel extends AndroidViewModel {

    private ComparisonSettingRepository comparisonSettingRepository;
    LiveData<ComparisonSetting> weights;

    public ComparisonSettingViewModel(Application application) {
        super(application);
        comparisonSettingRepository = new ComparisonSettingRepository(application);
        weights = comparisonSettingRepository.getSettings();
    }

    /**
     * Calls apply changes to store data in database.
     *
     * param comparisonSetting Contains data from the view (AdjustComparisonSetting)
     */
    void setWeights(ComparisonSetting comparisonSetting) {
        comparisonSettingRepository.updateSettings(comparisonSetting);
    }

    /**
     * Populates initial view from the database.
     *
     * return all weights from comparison settings table
     */
    LiveData<ComparisonSetting> getWeights() {
        return weights;
    }

}