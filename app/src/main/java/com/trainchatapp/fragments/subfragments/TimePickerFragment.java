package com.trainchatapp.fragments.subfragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import com.trainchatapp.R;


public class TimePickerFragment extends DialogFragment {

    private TextView timeText;
    private TimePicker timePicker;
    private Button okBtn;
    private Button cancelBtn;
    /*
    private int minValueHour;
    private int maxValueHour;
    private int minValueMinute;
    private int maxValueMinute;

     */


    public TimePickerFragment(TextView timeText){
        this.timeText = timeText;
        /*

            TextView timeText, int minValueHour, int maxValueHour, int minValueMinute, int maxValueMinute){
        this.timeText = timeText;
        this.minValueHour = minValueHour;
        this.maxValueHour = maxValueHour;
        this.minValueMinute = minValueMinute;
        this.maxValueMinute = maxValueMinute;

         */
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_time_picker);

        timePicker = dialog.findViewById(R.id.timePicker);


        /*

        hourPicker.setMinValue(minValueHour);
        hourPicker.setMaxValue(maxValueHour);
        minutePicker.setMinValue(minValueMinute);
        minutePicker.setMaxValue(59);

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal == minValueHour){
                    minutePicker.setMinValue(minValueMinute);
                }else{
                    minutePicker.setMinValue(0);
                }
                if(newVal == maxValueHour){
                    minutePicker.setMaxValue(maxValueMinute);
                }else{
                    minutePicker.setMaxValue(59);
                }
            }
        });

         */


        okBtn = dialog.findViewById(R.id.ok_btn);
        cancelBtn = dialog.findViewById(R.id.cancel_btn);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                timeText.setText(hour + ":" + minute);
                dialog.cancel();
            }
        });

        return dialog;
    }
}
