package com.example.thereselarsson.da401a_assignment_1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static int year;
    public static int month;
    public static int day;
    public String date;
    private EnterTransactionFragment etf;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        etf = new EnterTransactionFragment();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Do something with the date chosen by the user
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        date = Integer.toString(day) + "/" + Integer.toString(month) + "-" + Integer.toString(year);
        //etf.setDateButtonText(date); //gives null
        etf.setDate(date);
    }
}
