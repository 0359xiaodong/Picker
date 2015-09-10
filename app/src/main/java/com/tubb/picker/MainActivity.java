package com.tubb.picker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tubb.picker.library.DatePicker;
import com.tubb.picker.library.DateUtils;
import com.tubb.picker.library.PickerDialog;
import com.tubb.picker.library.PickerView;


public class MainActivity extends AppCompatActivity {

    private TextView tvDate;
    private PickerDialog mDatePickerDialog;
    private View datePickerView;
    private PickerView pvYear;
    private PickerView pvMonth;
    private PickerView pvDay;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDate = (TextView)findViewById(R.id.tvDate);
    }

    public void selectDate(View view){
        if (mDatePickerDialog == null) mDatePickerDialog = new PickerDialog(this);
        if (datePickerView == null) {
            datePickerView = LayoutInflater.from(this).inflate(R.layout.dialog_date_view, null);
            pvYear = (PickerView) datePickerView.findViewById(R.id.pvYear);
            pvMonth = (PickerView) datePickerView.findViewById(R.id.pvMonth);
            pvDay = (PickerView) datePickerView.findViewById(R.id.pvDay);
            datePicker = new DatePicker(pvYear, pvMonth, pvDay);
            datePicker.start(1970, 2050);
            datePicker.setSelectedYear(2015);
            datePicker.setSelectedMonth(9);
            datePicker.setSelectedDay(10);
            datePickerView.findViewById(R.id.tvDatePickerYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvDate.setText(DateUtils.formatDate(datePicker.getSelectedYear(), datePicker.getSelectedMonth(), datePicker.getSelectedDay()));
                    mDatePickerDialog.dismiss();
                }
            });
        }
        mDatePickerDialog.showBottom(datePickerView);
    }
}
