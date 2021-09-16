package edu.gatech.seclass.jobcompare6300.ui.CurrentJob;

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

public class CurrentJobFragment extends Fragment {

    private AddEditJobHelper addEditJobHelper = new AddEditJobHelper();
    private FormValidationHelper form = new FormValidationHelper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_job_form, container, false);
        CurrentJobViewModel currentJobViewModel = new ViewModelProvider(this).get(CurrentJobViewModel.class);

        // Validate decimal places for Double fields
        form.validateDoubleFields(root);

        // Pull current job from database
        currentJobViewModel.getCurrentJob().observe(getViewLifecycleOwner(), jobDetail -> {
            // If current job does not exist then go to enter current job flow
            if (jobDetail == null) {
                addOrUpdateFlow(root, currentJobViewModel, false);
            } else {
                // Otherwise, populate fields with current job information
                addEditJobHelper.populateFieldsWithCurrentJob(root, jobDetail, Constants.CURRENT_JOB_FRAGMENT);
                addOrUpdateFlow(root, currentJobViewModel, true);
            }
        });

        // Return to main menu on cancel
        root.findViewById(R.id.fragment_job_form_btn_cancel).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_current_job_to_navigation_menu));

        return root;
    }

    private void addOrUpdateFlow(View root, CurrentJobViewModel viewModel, Boolean isUpdate) {
        root.findViewById(R.id.fragment_job_form_btn_save).setOnClickListener(view -> {
            // If current job does not exist in database
            addEditJobToDatabase(root, viewModel, isUpdate);
        });
    }

    // Unfortunately, it is difficult to refactor this method further than this due to the specific ViewModel
    // We could however consolidate all the view models into one if we have time? Not sure if that would break anything
    private void addEditJobToDatabase(View view, CurrentJobViewModel viewModel, Boolean isUpdate) {

        JobDetail jobDetail = addEditJobHelper.constructJobDetailObject(view, Constants.CURRENT_JOB_FRAGMENT);
        // Check if jobDetail is null
        if (jobDetail == null) {
            Toast.makeText(requireContext(), Constants.EMPTY_FIELDS, Toast.LENGTH_LONG).show();
        } else {
            // Check if job title and company (which are primary keys) already exists
            checkForDuplicatesBeforeAddOrUpdate(view, viewModel, jobDetail, isUpdate);
        }
    }

    private void checkForDuplicatesBeforeAddOrUpdate(View view, CurrentJobViewModel viewModel, JobDetail jobDetail, Boolean isUpdate) {
        viewModel.getSpecificJob(jobDetail).observe(getViewLifecycleOwner(), jobFromDatabase -> {
            // Check for duplicates
            if (jobFromDatabase != null && jobDetail.getTitle().equals(jobFromDatabase.getTitle())
                    && jobDetail.getCompany().equals(jobFromDatabase.getCompany())) {
                Log.d(Constants.CURRENT_JOB_FRAGMENT + " - Duplicate entry found!", jobFromDatabase.toString());
                Toast.makeText(requireContext(), Constants.JOB_ALREADY_EXISTS, Toast.LENGTH_LONG).show();
            }

            // Add or update database depending on flag
            if (isUpdate) {
                viewModel.updateCurrentJob(jobDetail);
                Toast.makeText(requireContext(), Constants.CURRENT_POSITION_UPDATED, Toast.LENGTH_LONG).show();
                // Return to main menu
                Navigation.findNavController(view).navigate(R.id.action_navigation_current_job_to_navigation_menu);
            } else if (!isUpdate && jobFromDatabase == null) {
                viewModel.addCurrentJob(jobDetail);
                Toast.makeText(requireContext(), Constants.CURRENT_POSITION_ADDED, Toast.LENGTH_LONG).show();
                // Return to main menu
                Navigation.findNavController(view).navigate(R.id.action_navigation_current_job_to_navigation_menu);
            }
        });
    }
}