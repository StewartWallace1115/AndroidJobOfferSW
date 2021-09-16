package edu.gatech.seclass.jobcompare6300.ui.CompareJobs;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.jobcompare6300.dao.ComparisonSettingRepository;
import edu.gatech.seclass.jobcompare6300.dao.JobDetailRepository;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;


public class CompareJobsViewModel extends AndroidViewModel {

    private JobDetailRepository jobDetailRepository;
    private ComparisonSettingRepository comparisonSettingRepository;
    private LiveData<ComparisonSetting> settings;

    public CompareJobsViewModel(Application application) {
        super(application);
        jobDetailRepository = new JobDetailRepository(application);
        comparisonSettingRepository = new ComparisonSettingRepository(application);
        settings = comparisonSettingRepository.getSettings();

    }

    LiveData<List<JobDetail>> getAllJobs() {
        return jobDetailRepository.getAllJobs();
    }

    LiveData<ComparisonSetting> getSettings() {
        return settings;
    }


}