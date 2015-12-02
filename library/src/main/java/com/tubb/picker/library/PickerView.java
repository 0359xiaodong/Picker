package com.tubb.picker.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PickerView extends View
{

    @SuppressWarnings("unused")
    public static final String TAG = "PickerView";

    private List<String> mDataList;
    private int mCurrentSelected;
    private Paint mPaint;

    private float mSpeed = 2.0f;

    private float mMarginAlpha = 2.0f;

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

    private int mLayoutWidthAttr;
    private int mLayoutHeightAttr;

    Handler updateHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (Math.abs(mMoveLen) < mSpeed)
            {
                mMoveLen = 0;
                if (mTask != null)
                {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * mSpeed;
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
        mSpeed = a.getFloat(R.styleable.PickerView_Speed, mSpeed);
        mMarginAlpha = a.getFloat(R.styleable.PickerView_MarginAlpha, mMarginAlpha);
        a.recycle();
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLayoutWidthAttr = getLayoutParams().width;
        mLayoutHeightAttr = getLayoutParams().height;
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
        int index = mDataList.indexOf(item);
        if(index < 0){
            mCurrentSelected = mDataList.size() / 2;
        } else { // adjust the middle item
            LinkedList<String> snakeList = new LinkedList<>(mDataList);
            int middleIndex = snakeList.size() / 2;
            if(index == middleIndex){
                mCurrentSelected = index;
            }else{
                if(index < middleIndex){
                    int preCursor = middleIndex - index;
                    List<String> preLast = snakeList.subList(snakeList.size() - preCursor, snakeList.size());
                    List<String> rList = new LinkedList<>();
                    rList.addAll(preLast);
                    for (String date:rList){
                        snakeList.remove(date);
                    }
                    snakeList.addAll(0, rList);
                }else if(index > middleIndex){
                    int lastCursor = index - middleIndex;
                    List<String> lastPre = snakeList.subList(0, lastCursor);
                    List<String> rList = new LinkedList<>();
                    rList.addAll(lastPre);
                    for (String date:rList){
                        snakeList.remove(date);
                    }
                    snakeList.addAll(snakeList.size(), rList);
                }
                mDataList = snakeList;
                mCurrentSelected = mDataList.size() / 2;
            }
        }
        invalidate();
    }

    private void performSelect()
    {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    public void setData(List<String> datas)
    {
        if(datas == null || datas.size() == 0) return;
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;

        // find the largest length item
        List<String> sortList = new ArrayList<>(datas);
        Collections.sort(sortList, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return rhs.length() - lhs.length();
            }
        });

        // calculate width
        String item = sortList.get(0);
        if(getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Style.FILL);
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize(mMaxTextSize);
            mViewWidth = (int)(paint.measureText(item) + 0.5f);
        }

        // calculate height (base on MinTextSize)
        if(getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mColorText);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(mMinTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(item.substring(0, 1), 0, 1, bounds);
            int itemHeight = bounds.height();
            mViewHeight = (int)((mMarginAlpha - 1.0f) * itemHeight * (datas.size()-1)) + itemHeight * datas.size();
        }
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
        if(mLayoutWidthAttr != ViewGroup.LayoutParams.WRAP_CONTENT){
            mViewWidth = getMeasuredWidth();
        }
        if(mLayoutHeightAttr != ViewGroup.LayoutParams.WRAP_CONTENT){
            mViewHeight = getMeasuredHeight();
        }
        setMeasuredDimension(mViewWidth, mViewHeight);
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
        float d = mMarginAlpha * mMinTextSize * position + type * mMoveLen;
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

        if (mMoveLen > mMarginAlpha * mMinTextSize / 2)
        {
            moveTailToHead();
            mMoveLen = mMoveLen - mMarginAlpha * mMinTextSize;
        } else if (mMoveLen < -mMarginAlpha * mMinTextSize / 2)
        {
            moveHeadToTail();
            mMoveLen = mMoveLen + mMarginAlpha * mMinTextSize;
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

