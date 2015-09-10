package com.tubb.picker.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 选择器（仿iOS）
 */
public class PickerView extends View
{

    @SuppressWarnings("unused")
    public static final String TAG = "PickerView";
    public static final float MARGIN_ALPHA = 2.8f;
    public static final float SPEED = 2;

    private List<String> mDataList;
    private int mCurrentSelected;
    private Paint mPaint;

    private int mMaxTextSize = 60;
    private int mMinTextSize = 36;

    private int mMaxTextAlpha = 255;
    private int mMinTextAlpha = 120;

    private int mColorText = 0x333333;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (Math.abs(mMoveLen) < SPEED)
            {
                mMoveLen = 0;
                if (mTask != null)
                {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };

    public PickerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView, 0, defStyle);
        mMaxTextSize = a.getDimensionPixelSize(R.styleable.PickerView_MaxTextSize, mMaxTextSize);
        mMinTextSize = a.getDimensionPixelSize(R.styleable.PickerView_MinTextSize, mMinTextSize);
        mColorText = a.getColor(R.styleable.PickerView_TextColor, mColorText);
        mMaxTextAlpha = a.getInteger(R.styleable.PickerView_MaxTextAlpha, mMaxTextAlpha);
        mMinTextAlpha = a.getInteger(R.styleable.PickerView_MinTextAlpha, mMinTextAlpha);
        a.recycle();
        init();
    }

    public void setOnSelectListener(onSelectListener listener)
    {
        mSelectListener = listener;
    }

    public String getCurrentItem(){
        return mDataList.get(mCurrentSelected);
    }

    public void setCurrentItem(String item){
        if(item == null || "".equals(item)) return;
        mCurrentSelected = mDataList.indexOf(item);
        if(mCurrentSelected < 0) mCurrentSelected = mDataList.size() / 2;
        invalidate();
    }

    private void performSelect()
    {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    public void setData(List<String> datas)
    {
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    @SuppressWarnings("unused") // public api
    public void setSelected(int selected)
    {
        mCurrentSelected = selected;
    }

    private void moveHeadToTail()
    {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead()
    {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        isInit = true;
    }

    private void init()
    {
        timer = new Timer();
        mDataList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (isInit && mDataList.size() > 0)
            drawData(canvas);
    }

    private void drawData(Canvas canvas)
    {
        float scale = parabola(mMaxTextSize, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        for (int i = 1; (mCurrentSelected - i) >= 0; i++)
        {
            drawOtherText(canvas, i, -1);
        }
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++)
        {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * @param canvas
     * @param position
     * @param type
     */
    private void drawOtherText(Canvas canvas, int position, int type)
    {
        float d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mMaxTextSize, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * @param zero
     * @param x
     * @return scale
     */
    private float parabola(float zero, float x)
    {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event)
    {
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event)
    {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2)
        {
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2)
        {
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp()
    {
        if (Math.abs(mMoveLen) < 0.0001)
        {
            mMoveLen = 0;
            return;
        }
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask
    {
        Handler handler;

        public MyTimerTask(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run()
        {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface onSelectListener
    {
        void onSelect(String text);
    }
}

