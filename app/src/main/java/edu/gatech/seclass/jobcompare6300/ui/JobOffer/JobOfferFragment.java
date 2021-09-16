package edu.gatech.seclass.jobcompare6300.ui.JobOffer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;
import edu.gatech.seclass.jobcompare6300.ui.util.AddEditJobHelper;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;
import edu.gatech.seclass.jobcompare6300.ui.util.FormValidationHelper;

public class JobOfferFragment extends Fragment {

    private AddEditJobHelper addEditJobHelper = new AddEditJobHelper();
    private FormValidationHelper form = new FormValidationHelper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_job_form, container, false);
        JobOfferViewModel jobOfferViewModel = new ViewModelProvider(this).get(JobOfferViewModel.class);

        // Validate decimal places for Double fields
        form.validateDoubleFields(root);

        // Enter add job offer flow
        addJobFlow(root, jobOfferViewModel);

        // Return to main menu on cancel
        root.findViewById(R.id.fragment_job_form_btn_cancel).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_job_offer_to_navigation_menu));

        return root;
    }

    private void addJobFlow(View root, JobOfferViewModel viewModel) {
        root.findViewById(R.id.fragment_job_form_btn_save).setOnClickListener(view -> {
            addJobToDatabase(root, viewModel);
        });
    }

    // Unfortunately, it is difficult to refactor this method further than this due to the specific ViewModel
    // We could however consolidate all the view models into one if we have time? Not sure if that would break anything
    private void addJobToDatabase(View view, JobOfferViewModel viewModel) {

        JobDetail jobDetail = addEditJobHelper.constructJobDetailObject(view, Constants.JOB_OFFER_FRAGMENT);
        // Check if jobDetail is null
        if (jobDetail == null) {
            Toast.makeText(requireContext(), Constants.EMPTY_FIELDS, Toast.LENGTH_LONG).show();
        } else {
            // Since we can keep adding jobs we need to check if job offer already exists
            checkForDuplicatesBeforeAdd(view, viewModel, jobDetail);
        }
    }

    private void checkForDuplicatesBeforeAdd(View view, JobOfferViewModel viewModel, JobDetail jobDetail) {
        viewModel.getSpecificJob(jobDetail).observe(getViewLifecycleOwner(), jobFromDatabase -> {
            // Check for duplicates
            if (jobFromDatabase != null && jobDetail.getTitle().equals(jobFromDatabase.getTitle())
                    && jobDetail.getCompany().equals(jobFromDatabase.getCompany())) {
                Log.d(Constants.JOB_OFFER_FRAGMENT + " - Duplicate entry found!", jobFromDatabase.toString());
                Toast.makeText(requireContext(), Constants.JOB_ALREADY_EXISTS, Toast.LENGTH_LONG).show();
            }

            // If there is no duplicate record allow add
            if (jobFromDatabase == null) {
                // Add job offer to database
                viewModel.addJob(jobDetail);
                Toast.makeText(requireContext(), Constants.JOB_OFFER_ADDED, Toast.LENGTH_LONG).show();
                // Navigate to success screen
                Navigation.findNavController(view).navigate(R.id.action_navigation_job_offer_to_jobOfferAddedFragment);
            }
        });
    }
}