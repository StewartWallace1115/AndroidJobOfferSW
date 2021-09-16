package edu.gatech.seclass.jobcompare6300.ui.JobOffer;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import edu.gatech.seclass.jobcompare6300.dao.JobDetailRepository;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;

public class JobOfferViewModel extends AndroidViewModel {

    private JobDetailRepository jobDetailRepository;
    private LiveData<JobDetail> currentJob;
    private LiveData<JobDetail> lastAddedJobOffer;

    public JobOfferViewModel(Application application) {
        super(application);
        jobDetailRepository = new JobDetailRepository(application);
        currentJob = jobDetailRepository.getCurrentJob();
        lastAddedJobOffer = jobDetailRepository.getLastAddedJobOffer();
    }

    void addJob(JobDetail jobDetail) {
        jobDetailRepository.insertJob(jobDetail);
    }

    // We use this to validate before comparing to current job
    LiveData<JobDetail> getCurrentJob() {
        return currentJob;
    }

    LiveData<JobDetail> getLastAddedJobOffer() {
        return lastAddedJobOffer;
    }

    LiveData<JobDetail> getSpecificJob(JobDetail jobDetail) {
        return jobDetailRepository.getSpecificJob(jobDetail);
    }
}