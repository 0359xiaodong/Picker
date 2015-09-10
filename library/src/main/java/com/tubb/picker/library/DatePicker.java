package com.tubb.picker.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by bingbing.tu
 * 2015/8/14.
 */
public class DatePicker {

    protected int previousYear = 1970;
    protected int afterYear = 2050;

    PickerView mPvYear;
    PickerView mPvMonth;
    PickerView mPvDay;

    public DatePicker(PickerView pvYear, PickerView pvMonth, PickerView pvDay) {
        mPvYear = pvYear;
        mPvMonth = pvMonth;
        mPvDay = pvDay;
    }

    public void start(){
        start(this.previousYear, this.afterYear);
    }

    /**
     *
     * @param previousYear
     * @param afterYear
     */
    public void start(int previousYear, int afterYear) {

        if(previousYear <=0 || afterYear <= 0 || previousYear >= afterYear){
            throw new IllegalArgumentException("previousYear or afterYear must be set correct");
        }

        this.previousYear = previousYear;
        this.afterYear = afterYear;

        List<String> years = new ArrayList<>(afterYear - previousYear + 1);
        for (int year = previousYear; year <= afterYear; year++) {
            years.add(String.format(Locale.getDefault(), "%d年", year));
        }

        mPvYear.setData(years);
        mPvMonth.setData(DateUtils.getMonths());
        mPvDay.setData(DateUtils.getDayList(getSelectedYear(), getSelectedMonth()));

        mPvYear.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String year) {
                updatePvDay();
            }
        });

        mPvMonth.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String month) {
                updatePvDay();
            }
        });

        mPvMonth.setCurrentItem(String.format(Locale.getDefault(), "%d月", DateUtils.getCurrentMonth()));
        mPvDay.setCurrentItem(String.format(Locale.getDefault(), "%d日", DateUtils.getCurrentDay()));
    }

    private void updatePvDay() {
        mPvDay.setData(DateUtils.getDayList(getSelectedYear(), getSelectedMonth()));
    }

    public void setSelectedYear(int year) {
        mPvYear.setCurrentItem(String.format(Locale.getDefault(), "%d年", year));
    }

    public void setSelectedMonth(int month) {
        mPvMonth.setCurrentItem(String.format(Locale.getDefault(), "%d月", month));
    }

    public void setSelectedDay(int day) {
        mPvDay.setCurrentItem(String.format(Locale.getDefault(), "%d日", day));
    }

    public int getSelectedYear() {
        return Integer.parseInt(mPvYear.getCurrentItem().substring(0, mPvYear.getCurrentItem().length() - 1));
    }

    public int getSelectedMonth() {
        return Integer.parseInt(mPvMonth.getCurrentItem().substring(0, mPvMonth.getCurrentItem().length() - 1));
    }

    public int getSelectedDay() {
        return Integer.parseInt(mPvDay.getCurrentItem().substring(0, mPvDay.getCurrentItem().length() - 1));
    }

}
