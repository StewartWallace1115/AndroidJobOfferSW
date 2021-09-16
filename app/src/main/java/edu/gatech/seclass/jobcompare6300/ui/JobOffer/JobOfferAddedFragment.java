package edu.gatech.seclass.jobcompare6300.ui.JobOffer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;
import edu.gatech.seclass.jobcompare6300.ui.CompareJobs.CompareResultTableActivity;
import edu.gatech.seclass.jobcompare6300.ui.util.CompareHelper;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;
import edu.gatech.seclass.jobcompare6300.ui.util.SnackBarHelper;

public class JobOfferAddedFragment extends Fragment {

    private SnackBarHelper snacks = new SnackBarHelper();
    private CompareHelper compareHelper = new CompareHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_job_offer_added, container, false);
        // This should be ok to use
        JobOfferViewModel jobOfferViewModel = new ViewModelProvider(this).get(JobOfferViewModel.class);

        // For debugging purposes
        jobOfferViewModel.getLastAddedJobOffer().observe(getViewLifecycleOwner(), lastAddedJobOffer -> {
            Log.d(Constants.JOB_OFFER_ADDED_FRAGMENT, lastAddedJobOffer.toString());
        });

        // Enter another job offer
        root.findViewById(R.id.fragment_job_offer_added_btn_enter_offer).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_job_offer_added_to_navigation_job_offer));

        // Return to main menu
        root.findViewById(R.id.fragment_job_offer_added_btn_return_main).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_job_offer_added_to_navigation_menu));

        // By this point we know that at least one job exist (since we are on the job offer success screen)
        // Pull current job from database (if exist) before allowing navigation to comparison table
        checkCurrentJobBeforeCompare(root, jobOfferViewModel);

        return root;
    }

    private void checkCurrentJobBeforeCompare(View root, JobOfferViewModel viewModel) {
        root.findViewById(R.id.fragment_job_offer_added_btn_compare_to_current).setOnClickListener(view -> {
            viewModel.getCurrentJob().observe(getViewLifecycleOwner(), jobDetail -> {
                // If current job does not exist then display option to add current job
                if (jobDetail == null) {
                    snacks.activateSnackBar(root, Constants.CHECK_CURRENT_BEFORE_COMPARE_MESSAGE,
                            Constants.SNACKBAR_ADD_CURRENT_JOB_MESSAGE);
                } else {
                    // Otherwise, current job exists so display comparison table
                    // for newly added job and current job
                    compareWithCurrent(root, viewModel, jobDetail);
                }
            });
        });
    }

    private void compareWithCurrent(View root, JobOfferViewModel viewModel, JobDetail currentJob) {
        viewModel.getLastAddedJobOffer().observe(getViewLifecycleOwner(), jobDetail -> {
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "last job title:" + jobDetail.getTitle());
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "last job company:" + jobDetail.getCompany());

            String lastAddedJobAsString = compareHelper.buildJobAttributesAsString(jobDetail, false);
            String currentJobAsString = compareHelper.buildJobAttributesAsString(currentJob, true);
            Intent in = new Intent(getActivity(), CompareResultTableActivity.class);
            in.putExtra(Constants.FIRST_JOB, currentJobAsString);
            in.putExtra(Constants.SECOND_JOB, lastAddedJobAsString);
            startActivity(in);
        });
    }
}