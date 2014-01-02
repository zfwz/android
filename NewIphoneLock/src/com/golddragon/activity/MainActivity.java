package com.golddragon.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.golddragon.service.IPhoneLockService;
import com.golddragon.tools.BatteryObserver;
import com.golddragon.tools.BatteryObserver.OnBatteryChange;
import com.golddragon.view.IPhoneLockView;

/**
 * 主activity
 * 
 * @author zhangfeng
 * 
 */
public class MainActivity extends Activity implements OnBatteryChange {
    private BatteryObserver batteryObserver;
    private IPhoneLockView root;
    private RelativeLayout lock_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.lock_iphone_view, null);

        setContentView(view);

        root = (IPhoneLockView) view.findViewById(R.id.root);
        lock_bg = (RelativeLayout) root.findViewById(R.id.lock_bg);
        
        batteryObserver = BatteryObserver.getInstance(this);
        batteryObserver.register();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int widthPixels = metric.widthPixels; // 屏幕宽度（像素）
        int heightPixels = metric.heightPixels; // 屏幕高度（像素）
        
        /* 获取壁纸 */
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthPixels,
                heightPixels); // 加大高度，为了避免底部navigationbar在隐藏和显示之间绘制界面时的高度差，防止没有铺满全屏
        lock_bg.setLayoutParams(params);

        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别
        // 对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下
        float step = 0;
        // 计算出屏幕的偏移量
        step = (bm.getWidth() - widthPixels) / 6;
        // 截取相应屏幕的Bitmap
        /*if ((int) (heightPixels) > bm.getHeight()) {
            heightPixels = bm.getHeight();
        }*/
        Bitmap pbm = Bitmap.createBitmap(bm, (int) (3 * step), 0, (int) (widthPixels),
                (int) (heightPixels));
        // 设置 背景
        lock_bg.setBackgroundDrawable(new BitmapDrawable(pbm));
        /* 获取壁纸end */

        startService(new Intent(MainActivity.this, IPhoneLockService.class));
    }

    protected void onResume() {
        super.onResume();
        // System.out.println("MainActivity = ==============================onResume ");
        batteryObserver.setOnBatteryChange(this);
        root.onResume();
    }

    protected void onPause() {
        super.onPause();
        // System.out.println("MainActivity = ==============================onPause ");
        root.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        // System.out.println("MainActivity = ==============================onDestroy ");
        batteryObserver.unRegister();
    }

    @Override
    public void onChange(int status, int level, int scale) {
        root.onBattery(status, level, scale);
        root.onUpdate();
    }

    // 屏蔽掉Back键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int code = event.getKeyCode();
        if (code == KeyEvent.KEYCODE_BACK) {
            return true;
        } else if (code == KeyEvent.KEYCODE_HOME) {
            System.out.println("home");
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
