package edu.gatech.seclass.jobcompare6300.ui.CurrentJob;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import edu.gatech.seclass.jobcompare6300.dao.JobDetailRepository;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;

public class CurrentJobViewModel extends AndroidViewModel {

    private JobDetailRepository jobDetailRepository;
    private LiveData<JobDetail> currentJob;

    public CurrentJobViewModel(Application application) {
        super(application);
        jobDetailRepository = new JobDetailRepository(application);
        currentJob = jobDetailRepository.getCurrentJob();
    }

    void addCurrentJob(JobDetail jobDetail) {
        jobDetailRepository.insertJob(jobDetail);
    }

    LiveData<JobDetail> getCurrentJob() {
        return currentJob;
    }

    LiveData<JobDetail> getSpecificJob(JobDetail jobDetail) {
        return jobDetailRepository.getSpecificJob(jobDetail);
    }

    void updateCurrentJob(JobDetail jobDetail) {
        jobDetailRepository.updateCurrentJob(jobDetail);
    }
}
