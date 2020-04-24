package com.itfitness.fitnessviewpager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import com.itfitness.fitnessviewpager.R;

/**
 * @ProjectName: FitnessViewPager
 * @Package: com.itfitness.fitnessviewpager.widget
 * @ClassName: FitnessViewPager
 * @Description: java类作用描述
 * @Author: 作者名 itfitness
 * @CreateDate: 2020/4/24 9:58
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/24 9:58
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FitnessViewPager extends ViewGroup {
    private GestureDetector gestureDetector;
    private int currentPos = 0;//当前的位置
    private Scroller scroller;
    private float mStartX,mStartY;
    public FitnessViewPager(Context context) {
        this(context,null);
    }

    public FitnessViewPager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FitnessViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollBy((int) distanceX,0);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            postInvalidate();
        }
        super.computeScroll();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            //如果是view:触发view的测量;如果是ViewGroup，触发测量ViewGroup中的子view
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getX();
                mStartY = ev.getY();
                gestureDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                float y = ev.getY();
                Log.e("测试滑动呀",y+"==="+mStartY);
                float moveX = x - mStartX;
                float moveY = y - mStartY;
                if(Math.abs(moveX)>Math.abs(moveY)){
                    //横向滑动，拦截子View的事件
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                //获取X轴相对的滑动距离
                int scrollX = getScrollX();
                currentPos = (scrollX + getWidth() / 2) / getWidth();
                if(currentPos<0){
                    currentPos = 0;
                }
                if(currentPos>=getChildCount()-1){
                    currentPos = getChildCount()-1;
                }
                break;
            case MotionEvent.ACTION_UP:
//                scrollTo(currentPos*getWidth(),0);
                scroller.startScroll(getScrollX(),0,-(getScrollX()-currentPos*getWidth()),0);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        for(int  i = 0 ; i < getChildCount() ; i++){
            View childView = getChildAt(i);
            int left = paddingLeft + (measuredWidth-paddingLeft-paddingRight)*i;
            int top = paddingTop;
            int right = (measuredWidth-paddingRight) * (i+1);
            int bottom = measuredHeight-paddingBottom;
            childView.layout(left,top,right,bottom);
        }
    }
}
