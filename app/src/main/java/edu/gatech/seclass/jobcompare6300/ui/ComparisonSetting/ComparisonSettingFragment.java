package edu.gatech.seclass.jobcompare6300.ui.ComparisonSetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;
import edu.gatech.seclass.jobcompare6300.ui.util.FormValidationHelper;
import edu.gatech.seclass.jobcompare6300.ui.util.SettingsHelper;

public class ComparisonSettingFragment extends Fragment {

    private ComparisonSettingViewModel comparisonSettingViewModel;
    private SettingsHelper settingsHelper = new SettingsHelper();
    private FormValidationHelper form = new FormValidationHelper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        comparisonSettingViewModel = new ViewModelProvider(this).get(ComparisonSettingViewModel.class);
        // Because the hint fields were refactored, this will help get the EditText fields
        View view = inflater.inflate(R.layout.fragment_comparison_setting, container, false);
        ArrayList<EditText> editTextArrayList = settingsHelper.buildEditTextList(view);

        // Populate form with default weight values
        comparisonSettingViewModel.getWeights().observe(getViewLifecycleOwner(), new Observer<ComparisonSetting>() {
            @Override
            public void onChanged(@Nullable final ComparisonSetting update) {
                if (update != null) {
                    // Retrieves data from view and populates a ComparisonSetting object
                    settingsHelper.setEditTextFieldsWithInitialValues(view, update);
                }
            }
        });

        // Submit onClick
        view.findViewById(R.id.fragment_setting_button_submit).setOnClickListener(v -> {
            if (form.checkInputValid(editTextArrayList)) {
                comparisonSettingViewModel.setWeights(settingsHelper.updateWeightsWithFormValues(view, Constants.COMPARISON_SETTING_FRAGMENT));
                Toast.makeText(requireContext(), Constants.SETTINGS_UPDATED, Toast.LENGTH_LONG).show();
                // Return to main menu
                Navigation.findNavController(view).navigate(R.id.action_navigation_setting_comparison_setting_to_navigation_menu);
            }
        });

        // Cancel onClick
        view.findViewById(R.id.fragment_setting_button_cancel).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_setting_comparison_setting_to_navigation_menu));

        return view;
    }
}