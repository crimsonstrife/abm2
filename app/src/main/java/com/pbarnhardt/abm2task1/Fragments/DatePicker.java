package com.pbarnhardt.abm2task1.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.pbarnhardt.abm2task1.Views.BaseDetailActivity;

import java.util.Calendar;

public class DatePicker extends DialogFragment {
    //types
    static final int STARTDATE = 1;
    static final int ENDDATE = 2;
    private int datePickerOrigin;
    int datePickerReturn = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (BaseDetailActivity) getActivity(), year, month, dayOfMonth);
    }
}
