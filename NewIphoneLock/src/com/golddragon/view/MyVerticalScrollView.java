package com.golddragon.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyVerticalScrollView extends ScrollView {

    Context mContext;
    private View mView;
    private float touchY;
    private int scrollY = 0;
    private boolean handleStop = false;
    private int eachStep = 0;

    private static final int MAX_SCROLL_HEIGHT = 1000;// 最大滑动距离
    private static final float SCROLL_RATIO = 0.7f;// 阻尼系数,越小阻力就越大

    private static final int OPEN_CAMERA_DIST = 450; // 滚动超过这个值后就打开相机

    private boolean mIsSlideOpen = false; // 是否可以滑动打开相机

    //5110 private int mOpenCameraRangeCoordinate[] = { 700, 800, 1000, 1200 }; // 打开相机滑动的坐标范围取值,第一，二个为x坐标，三，四为Y坐标
    private int mOpenCameraRangeCoordinate[] = { 650, 1000, 650, 1000 }; // p008打开相机滑动的坐标范围取值,第一，二个为x坐标，三，四为Y坐标
    
    private IPhoneLockView lockView;
    public MyVerticalScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyVerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MyVerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            this.mView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        float x = arg0.getX();
        float y = arg0.getY();
        System.out.println("x = " + x + " y = " + y);

        if (x > mOpenCameraRangeCoordinate[0] && x < mOpenCameraRangeCoordinate[1]
                && y > mOpenCameraRangeCoordinate[2] && y < mOpenCameraRangeCoordinate[3]) {
            mIsSlideOpen = true;
        }

        if (mIsSlideOpen) { // 判断是否通过触摸相机图标后滑动的
            if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
                touchY = arg0.getY();
            }
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mView == null) {
            return super.onTouchEvent(ev);
        } else {
            return commonOnTouchEvent(ev);
        }
        // return super.onTouchEvent(ev);
    }

    private boolean commonOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (touchY - nowY);
                // System.out.println("********************** ACTION_MOVE ========== nowY = "+nowY
                // +" deltaY = " +deltaY);
                if (deltaY > 0) { // 只能往上滑动，禁止向下滑动
                    touchY = nowY;
                    // System.out.println("********************** ACTION_MOVE ========== isNeedMove = "+isNeedMove());
                    // if (isNeedMove()) {
                    int offset = mView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        // System.out.println("********************** ACTION_MOVE ========== offset = "+offset);
                        mView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                        handleStop = false;
                    }
                    // }
                } else {
                    return false;// 只能往上滑动，禁止向下滑动
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mView.getScrollY() != 0) {
                    handleStop = true;
                    animation();
                    mIsSlideOpen = false; // 重置值
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isNeedMove() {
        int viewHight = mView.getMeasuredHeight();
        int srollHight = getHeight();
        int offset = viewHight - srollHight;
        int scrollY = getScrollY();

        System.out.println("============ ************ viewHight = " + viewHight + " srollHight = "
                + srollHight + " offset = " + offset + " scrollY = " + scrollY);
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    private void animation() {
        scrollY = mView.getScrollY();
        eachStep = scrollY / 10;
        resetPositionHandler.sendEmptyMessage(0);
    }

    Handler resetPositionHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("=============================scrollY = " + scrollY);
            if (scrollY > OPEN_CAMERA_DIST) {
                lockView.unLock();
                Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA"); // 调用照相机
                mContext.startActivity(intent);
            } else {
                if (scrollY != 0 && handleStop) {
                    scrollY -= eachStep;
                    if ((eachStep < 0 && scrollY > 0) || (eachStep > 0 && scrollY < 0)) {
                        scrollY = 0;
                    }
                    mView.scrollTo(0, scrollY);
                    this.sendEmptyMessageDelayed(0, 5);
                }
            }
        };
    };
    
    public void setLockView(IPhoneLockView lockview) {
        this.lockView = lockview;
    }
}