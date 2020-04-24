package com.itfitness.fitnessviewpager.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @ProjectName: FitnessViewPager
 * @Package: com.itfitness.fitnessviewpager.widget
 * @ClassName: SimpleScrollLayout
 * @Description: java类作用描述
 * @Author: 作者名 itfitness
 * @CreateDate: 2020/4/24 12:03
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/24 12:03
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class SimpleScrollLayout extends ViewGroup {
    private View mHeadView;
    private int mHeadHeight;
    private ScrollView mScrollView;
    private GestureDetector gestureDetector;
    private float mStartY;
    private int mtopY = 0;
    private boolean isDownScroll = false;
    public SimpleScrollLayout(Context context) {
        super(context);
        init();
    }

    public SimpleScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeadView.layout(l,t,r,mHeadHeight);
        getChildAt(1).layout(l,mHeadHeight,r/2,b+mHeadHeight);
        getChildAt(2).layout(r/2,mHeadHeight,r,b+mHeadHeight);
    }

    private void init() {
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(getScrollY()+distanceY<mHeadHeight){
                    if(getScrollY()>=0){
                        if(getScrollY()+distanceY<0){
                            scrollTo(0, 0);
                        }else {
                            scrollBy(0, (int) distanceY);
                        }
                    }
                }else if(getScrollY()+distanceY>=mHeadHeight){
                    scrollTo(0, mHeadHeight);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("测试搜索",mStartY+"==="+ev.getY());
                isDownScroll = ev.getY()-mStartY>0;
                if(isDownScroll){
                    Log.e("测试搜索a","true");
                }else {
                    Log.e("测试搜索a","false");
                }
                break;
            case MotionEvent.ACTION_UP:
                if(getScrollY()>mHeadHeight){
                    scrollTo(0, mHeadHeight);
                }else if(getScrollY()<0){
                    scrollTo(0, 0);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        ScrollView scrollView = (ScrollView) getChildAt(1);
//        int scrollY = scrollView.getScrollY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                mStartY = ev.getY();
                gestureDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if(mScrollView.getScrollY()==0 && mtopY>0 && isDownScroll){
                    return true;
                }
                if(getScrollY()>=mHeadHeight){
                    mtopY = mHeadHeight;
                    return false;
                }else {
                    mtopY = getScrollY();
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
//        switch (event.getAction()){
//            case MotionEvent.ACTION_UP:
//                if(mtopY == mHeadHeight){
//                    scrollTo(0,mtopY);
//                }
//                break;
//        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for(int i = 0 ; i < getChildCount() ; i ++ ){
            View childAt = getChildAt(i);
            LayoutParams layoutParams = childAt.getLayoutParams();
            //获取子控件的MeasureSpec
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;
            if(i == 1 || i==2){
                int i1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) / 2, MeasureSpec.EXACTLY);
//                layoutParams.width = layoutParams.width/2;
                childWidthMeasureSpec = getChildMeasureSpec(i1, 0, layoutParams.width);
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, layoutParams.height);
            }else {
                childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, layoutParams.width);
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, layoutParams.height);
            }
            childAt.measure(childWidthMeasureSpec,childHeightMeasureSpec);//对子控件进行度量
            if(i==0){
                mHeadView = childAt;
                mHeadHeight = mHeadView.getMeasuredHeight();
            }else if(childAt instanceof ScrollView){
                mScrollView = (ScrollView) childAt;
            }
        }
//        mHeadView = getChildAt(0);
//        LayoutParams layoutParams = mHeadView.getLayoutParams();
//        //获取子控件的MeasureSpec
//        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, layoutParams.width);
//        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, layoutParams.height);
//        mHeadView.measure(childWidthMeasureSpec,childHeightMeasureSpec);//对子控件进行度量
//        mHeadHeight = mHeadView.getMeasuredHeight();
//        getChildAt(1).measure(widthMeasureSpec,heightMeasureSpec);
//        Log.e("测试高度",mHeadHeight+"");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
