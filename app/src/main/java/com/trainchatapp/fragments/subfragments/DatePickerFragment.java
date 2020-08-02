package com.trainchatapp.fragments.subfragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{


    private TextView dateText;

    public static final long _30DAYS_MILLIS = 86400000 * 30;

    public DatePickerFragment(TextView dateText){
        this.dateText = dateText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());


        c.add(Calendar.DATE, 30);


        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        dateText.setText(day + "/" + (month+1) + "/" + year);
    }

}