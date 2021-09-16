package edu.gatech.seclass.jobcompare6300.ui.util;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.R;

public class FormValidationHelper {

    /**
     * ~~~~~ Validators for Job Form (Both Current Job and Job Offer page) ~~~~~
     */

    public void validateDoubleFields(View view) {
        // Get Double form fields
        EditText commuteTimeEditText = view.findViewById(R.id.fragment_job_form_editText_commute_time);
        EditText yearlySalaryEditText = view.findViewById(R.id.fragment_job_form_editText_yearly_salary);
        EditText yearlyBonusEditText = view.findViewById(R.id.fragment_job_form_editText_yearly_bonus);
        EditText retirementBenefitsEditText = view.findViewById(R.id.fragment_job_form_editText_retirement_benefits);

        commuteTimeEditText.setFilters(new InputFilter[]{new DecimalEditTextFilter(2)});
        yearlySalaryEditText.setFilters(new InputFilter[]{new DecimalEditTextFilter(6)});
        yearlyBonusEditText.setFilters(new InputFilter[]{new DecimalEditTextFilter(6)});
        retirementBenefitsEditText.setFilters(new InputFilter[]{new DecimalEditTextFilter(2)});
    }

    public Boolean isStringFieldValid(EditText text) {
        if (text.length() == 0 || text.getText().toString().matches("[0-9]+")) {
            text.setError(Constants.REQUIRED_FIELD);
            return true;
        } else {
            return false;
        }
    }

    public Boolean isNumberFieldValid(EditText text, Boolean isInteger) {
        String numberAsString = text.getText().toString();

        if (isInteger) {
            // Cannot parse a blank string
            try {
                Integer.parseInt(numberAsString);
            } catch (NumberFormatException e) {
                text.setError(Constants.MUST_BE_WHOLE_NUMBER);
                return true;
            }
        } else {
            // Cannot parse a blank string
            try {
                Double.parseDouble(numberAsString);
            } catch (NumberFormatException e) {
                text.setError(Constants.MUST_BE_DECIMAL);
                return true;
            }
        }

        // Allow zero for decimals but not whole numbers
        if (numberAsString.isEmpty() || (numberAsString.charAt(0) == '0' && isInteger)) {
            text.setError(Constants.INVALID_INTEGER_VALUE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * ~~~~~ Validators for Comparison Settings page ~~~~~
     */

    public Boolean checkInputValid(ArrayList<EditText> array) {
        Boolean validInput = true;

        for (int i = 0; i < array.size(); ++i) {
            String current = array.get(i).getText().toString();
            if (isEmpty(current)) {
                array.get(i).setError(Constants.FIELD_CANNOT_BE_EMPTY);
                validInput = false;
            } else if (isInputInvalidUnsignedInt(current)) {
                array.get(i).setError(Constants.MUST_BE_INT);
                validInput = false;
            } else if (Integer.parseInt(current) > 100) {
                array.get(i).setError(Constants.MUST_BE_UNDER_LIMIT);
                validInput = false;
            }

        }
        return validInput;
    }

    private Boolean isInputInvalidUnsignedInt(String text) {
        try {
            int number = Integer.parseInt(text);
            if (number < 0)
                return true;
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    private Boolean isEmpty(String text) {
        return text.length() == 0;
    }
}
