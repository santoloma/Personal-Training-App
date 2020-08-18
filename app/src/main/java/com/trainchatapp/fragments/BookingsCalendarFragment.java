package com.trainchatapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.timessquare.CalendarPickerView;
import com.trainchatapp.R;
import com.trainchatapp.utils.DateTimeUtils;
import java.util.Calendar;
import java.util.Date;


public class BookingsCalendarFragment extends Fragment {

    private CalendarPickerView calendarPicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings_calendar, container, false);
        calendarPicker = view.findViewById(R.id.calendarPicker);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);

        calendarPicker.init(today, calendar.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDate(today);

        calendarPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                System.out.println(DateTimeUtils.SDF_DATE.format(date));
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        return view;
    }
}