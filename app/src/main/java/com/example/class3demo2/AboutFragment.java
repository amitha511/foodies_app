package com.example.class3demo2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.class3demo2.databinding.FragmentAboutBinding;

import java.util.zip.Inflater;


public class AboutFragment extends Fragment {
    FragmentAboutBinding binding;
    int d = 1;
    int m = 0;
    int y = 2023;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        setDate();
        binding.dateEditBtn.setOnClickListener((view)->{
            Dialog dialog = new DatePickerDialog(getContext(),(datePicker,yy,mm,dd)->{
                y = yy;
                m = mm;
                d = dd;
                setDate();
            },y,m,d);
            dialog.show();
        });


        binding.dateInputEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Dialog dialog = new DatePickerDialog(getContext(),(datePicker,yy,mm,dd)->{
                        y = yy;
                        m = mm;
                        d = dd;
                        setDate2();
                    },y,m,d);
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        return binding.getRoot();
    }
    void setDate2(){
        binding.dateInputEt.setText("" + d + "/" + (m +1) + "/" + y);
    }
    void setDate(){
        binding.dateTv.setText("" + d + "/" + (m +1) + "/" + y);
    }
}