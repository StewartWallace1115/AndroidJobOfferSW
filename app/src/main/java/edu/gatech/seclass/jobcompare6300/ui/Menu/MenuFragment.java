package edu.gatech.seclass.jobcompare6300.ui.Menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;
import edu.gatech.seclass.jobcompare6300.ui.util.SnackBarHelper;


public class MenuFragment extends Fragment {

    private SnackBarHelper snacks = new SnackBarHelper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Enter current job
        root.findViewById(R.id.fragment_menu_btn_current_job).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.navigation_current_job));
        // Enter job offers
        root.findViewById(R.id.fragment_menu_btn_job_offer).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.navigation_job_offer));
        // Adjust comparison settings
        root.findViewById(R.id.fragment_menu_btn_adjust_settings).setOnClickListener(view ->
                Navigation.findNavController(root).navigate(R.id.navigation_adjust_comparison_settings));

        // Pull all jobs from database (if exist) before allowing navigation to compare job offers page
        checkJobsBeforeCompareNav(root, menuViewModel);

        return root;
    }

    private void checkJobsBeforeCompareNav(View root, MenuViewModel viewModel) {
        root.findViewById(R.id.fragment_menu_btn_compare_job_offers).setOnClickListener(view -> {
            viewModel.getAllJobs().observe(getViewLifecycleOwner(), jobDetailList -> {
                // If at least two jobs do not exist then display option to add job offer
                Log.d(Constants.MENU_FRAGMENT + " - jobDetailsList:", String.valueOf(jobDetailList.size()));
                if (jobDetailList.size() < 2) {
                    snacks.activateSnackBar(root, Constants.CHECK_BEFORE_COMPARE_MESSAGE,
                            Constants.SNACKBAR_ADD_JOB_OFFER_MESSAGE);
                } else {
                    // Otherwise, at least two jobs exist so allow navigation to compare job offers page
                    Navigation.findNavController(root).navigate(R.id.navigation_compare_jobs);
                }
            });
        });
    }
}