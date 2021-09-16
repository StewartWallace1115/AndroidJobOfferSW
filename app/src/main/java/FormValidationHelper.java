package edu.gatech.seclass.jobcompare6300.ui.util;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.R;

public class FormValidationHelper {

  
    
    /**
     * Check input validation for all EditTextFields on a given screen.
     * Parms: ArrayList<EditText>: List of all EditTextFields on the screen 
     * Return: True if validation passes, false if validation fails. 
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


    /**
     * Tests if string is invalid unsigned int
     * Parms: Input as a string
     * Return: true if signed int or non-integer, false is unsigned int
     */
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

    /**
     * Check if String is empty 
     * Parms: Input as a String
     * Return: true if empty, false is nonempty
     */
    private Boolean isEmpty(String text) {
        return text.length() == 0;
    }
}
