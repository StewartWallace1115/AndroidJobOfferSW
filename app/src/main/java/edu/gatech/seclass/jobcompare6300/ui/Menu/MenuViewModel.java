package edu.gatech.seclass.jobcompare6300.ui.Menu;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.gatech.seclass.jobcompare6300.dao.JobDetailRepository;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;

public class MenuViewModel extends AndroidViewModel {

    private LiveData<List<JobDetail>> allJobs;

    public MenuViewModel(Application application) {
        super(application);
        JobDetailRepository jobDetailRepository = new JobDetailRepository(application);
        allJobs = jobDetailRepository.getAllJobs();
    }

    // We use this to validate before allowing to compare jobs
    LiveData<List<JobDetail>> getAllJobs() {
        return allJobs;
    }
}