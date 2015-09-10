package com.tubb.picker.library;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by bingbing.tu
 * 2015/8/12.
 */
public class PickerDialog {

    private Dialog mDialog;
    private View contentView;
    private int gravity;
    private int windowAnimations;

    public PickerDialog(Context context){
        mDialog = new Dialog(context, R.style.CaptainDialogStyle);
    }

    public void showCenter(View contentView) {
        if(contentView != null) show();
        setContentView(contentView);
        setGravity(Gravity.CENTER);
        setLayout(contentView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
        show();
    }

    public void showTop(View contentView) {
        if(contentView != null) show();
        setContentView(contentView);
        setGravity(Gravity.TOP);
        setWindowAnimations(R.style.TopDialogAnim);
        setLayout(contentView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
        show();
    }

    public void showBottom(View contentView) {
        if(contentView != null) show();
        setContentView(contentView);
        setGravity(Gravity.BOTTOM);
        setWindowAnimations(R.style.BottomDialogAnim);
        setLayout(contentView.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
        show();
    }

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        mDialog.getWindow().setContentView(contentView);
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        mDialog.getWindow().setGravity(gravity);
    }

    public void setWindowAnimations(int windowAnimations) {
        this.windowAnimations = windowAnimations;
        mDialog.getWindow().setWindowAnimations(windowAnimations);
    }

    public void setLayout(int width, int height){
        mDialog.getWindow().setLayout(width, height);
    }

    public void clearBackground(){
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public boolean isShowing(){
        return mDialog.isShowing();
    }
}
