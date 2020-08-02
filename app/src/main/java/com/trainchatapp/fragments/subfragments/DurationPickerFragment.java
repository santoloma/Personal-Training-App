package com.trainchatapp.fragments.subfragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.trainchatapp.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DurationPickerFragment extends DialogFragment {

    private TextView textView;
    private List<String> durations;
    private Button okBtn;
    private Button cancelBtn;


    public DurationPickerFragment(TextView textView){
        this.textView = textView;
        this.durations = Arrays.asList(new String[]{"30 mins", "60 mins", "90 mins", "120 mins"});
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_availability);
        final RadioGroup rg = dialog.findViewById(R.id.radioGroup);
        okBtn = dialog.findViewById(R.id.ok_btn);
        cancelBtn = dialog.findViewById(R.id.cancel_btn);

        for(String freeSlot: durations){
            RadioButton rb = new RadioButton(getContext()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setId(freeSlot.hashCode());
            rb.setText(freeSlot);
            rb.setTag(freeSlot);
            rg.addView(rb);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!durations.isEmpty()) {
                    textView.setText((rg.findViewById(rg.getCheckedRadioButtonId()).getTag() + ""));
                }
                dialog.cancel();
            }
        });


        return dialog;
    }
}
