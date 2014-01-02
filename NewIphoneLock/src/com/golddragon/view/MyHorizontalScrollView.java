package com.golddragon.view;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 
 * 回弹处理
 * 
 * @author zhangfeng
 * 
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
	private static final String TAG = "MyHorizontalScrollView";

	// context
	Context mContext;

	// the child View
	private View mChildView;

	private boolean handleStop = false;

	private static final int MAX_SCROLL_HEIGHT = 500; // 最大滚动距离
	private static final float SCROLL_DRAG = 0.6f; // 阻力系数，值越小 阻力越大
	private float RESET_RADIO = 0.9f;

	private Vibrator mVibrator; // 声明一个振动器对象

	private IPhoneLockView lockView;

	private static final int UNLOCK_DIST = -200; // 滚动超过这个值后就解锁

	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mVibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		// get child View
		if (getChildCount() > 0) {
			this.mChildView = getChildAt(0);
		}
	}

	public void setLockView(IPhoneLockView lockview) {
		this.lockView = lockview;
	}

	private float touchX = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// get touch X
			touchX = arg0.getX();
			mVibrator.vibrate(new long[] { 0, 100, 0, 0 }, -1);
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mChildView != null) {
			commonOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	private void commonOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			float nowX = ev.getX();
			int deltaX = (int) (touchX - nowX);
			touchX = nowX;
			if (isEdge()) {
				int offset = mChildView.getScrollX();
				if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
					mChildView.scrollBy((int) (deltaX * SCROLL_DRAG), 0);
					handleStop = false;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mChildView.getScrollX() != 0) {
				handleStop = true;
				startAnimation();
			}
			break;
		default:
			break;
		}
	}

	/*
	 * whether to edge
	 */
	private boolean isEdge() {
		// get the child view Width
		int childViewWidth = mChildView.getMeasuredWidth();
		// get the ScrollView Width
		int srollViewWidth = this.getWidth();
		// get
		int tempOffset = childViewWidth - srollViewWidth;
		int scrollX = this.getScrollX();
		if (scrollX == 0 || scrollX == tempOffset) {
			return true;
		}
		return false;
	}

	private void startAnimation() {
		resetChildViewPositionHandler.sendEmptyMessage(0);
	}

	private float childScrollX = 0;
	Handler resetChildViewPositionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			childScrollX = mChildView.getScrollX();
			System.out.println("===================childScrollX = "
					+ childScrollX);
			if (childScrollX != 0 && handleStop) {
				if (childScrollX < UNLOCK_DIST) { // 负值为向右滑动，即为解锁方向，小于－200即解锁
					lockView.unLock();
				} else {
					childScrollX = childScrollX * RESET_RADIO;
					if (Math.abs(childScrollX) <= 2) {
						childScrollX = 0;
					}
					mChildView.scrollTo((int) childScrollX, 0);
					this.sendEmptyMessageDelayed(0, 3);
				}
			}
		};
	};
}
