package com.tubb.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tubb.picker.library.DatePicker;
import com.tubb.picker.library.DateUtils;
import com.tubb.picker.library.PickerDialog;
import com.tubb.picker.library.PickerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;
    private PickerDialog mDatePickerDialog;
    private View datePickerView;
    private PickerDialog mColorPickerDialog;
    private View colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
    }

    public void selectDate(View view){
        if (mDatePickerDialog == null) mDatePickerDialog = new PickerDialog(this);
        if (datePickerView == null) {
            datePickerView = LayoutInflater.from(this).inflate(R.layout.dialog_date_view, null);
            PickerView pvYear = (PickerView) datePickerView.findViewById(R.id.pvYear);
            PickerView pvMonth = (PickerView) datePickerView.findViewById(R.id.pvMonth);
            PickerView pvDay = (PickerView) datePickerView.findViewById(R.id.pvDay);
            final DatePicker datePicker = new DatePicker(pvYear, pvMonth, pvDay);
            datePicker.start(1970, 2050);
            datePicker.setSelectedYear(DateUtils.getCurrentYear());
            datePicker.setSelectedMonth(DateUtils.getCurrentMonth());
            datePicker.setSelectedDay(DateUtils.getCurrentDay());
            datePickerView.findViewById(R.id.tvDatePickerYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvInfo.setText(DateUtils.formatDate(datePicker.getSelectedYear(), datePicker.getSelectedMonth(), datePicker.getSelectedDay()));
                    mDatePickerDialog.dismiss();
                }
            });
        }
        mDatePickerDialog.showBottom(datePickerView);
    }

    public void selectColor(View view){
        if(mColorPickerDialog == null) mColorPickerDialog = new PickerDialog(this);
        if(colorPickerView == null){
            colorPickerView = LayoutInflater.from(this).inflate(R.layout.color_picker_view, null);
            final PickerView colorPv = (PickerView) colorPickerView.findViewById(R.id.pvColor);
            colorPickerView.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvInfo.setText(colorPv.getCurrentItem());
                    mColorPickerDialog.dismiss();
                }
            });
            colorPv.setData(getColors());
        }
        mColorPickerDialog.showBottom(colorPickerView);
    }

    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Indigo");
        colors.add("Violet");
        return colors;
    }
}
