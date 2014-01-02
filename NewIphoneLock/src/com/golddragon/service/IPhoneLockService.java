package com.golddragon.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.golddragon.activity.MainActivity;
import com.golddragon.constant.ConstantsUtil;

/**
 * 锁屏服务
 * 
 * @author Administrator
 * 
 */
public class IPhoneLockService extends Service {
    private Intent mLockIntent = null;

    private ContentResolver contentResolver;
    private Context mContext;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        super.onCreate();
        mContext = IPhoneLockService.this;

        contentResolver = getContentResolver();

        mLockIntent = new Intent(mContext, MainActivity.class);
        mLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /* 注册广播 */
        IntentFilter mScreenOnFilter = new IntentFilter();
        mScreenOnFilter.addAction("android.intent.action.SCREEN_ON");
        mScreenOnFilter.addAction("android.intent.action.SCREEN_OFF");
        mContext.registerReceiver(mScreenOnOrOffReceiver, mScreenOnFilter);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("**************onStartCommand ================= ");
        return Service.START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mScreenOnOrOffReceiver);
        // 在此重新启动
        startService(new Intent(mContext, IPhoneLockService.class));
    }

    // 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
    private BroadcastReceiver mScreenOnOrOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("android.intent.action.SCREEN_OFF")
                    || action.equals("android.intent.action.SCREEN_ON")) {
                // 在这里判断是否要启动自有锁屏
                int state = queryLockState();
                System.out.println("**************action ================= " + action);
                System.out.println("**************state ================= " + state);

                if (state == 1) {
                    System.out.println("mScreenOnOrOffReceiver disableKeyguard");

                    /*发送透明状态栏广播*/
                    Intent broadIntent = new Intent();
                    broadIntent.setAction("com.golddragon.statusbar.transparent");
                    broadIntent.putExtra("is_transparent", true);
                    sendBroadcast(broadIntent);
                    /*发送透明状态栏广播end*/

                    startActivity(mLockIntent);
                }
            }
        }

    };

    /**
     * 查询当前锁屏幕状态
     */
    private int queryLockState() {
        int state = 0;
        // 查找
        Cursor cursor = contentResolver.query(ConstantsUtil.CONTENT_URI, new String[] {
                ConstantsUtil.ID, ConstantsUtil.FLAG }, null, null, null);

        while (cursor.moveToNext()) {
            state = cursor.getInt(1);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return state;
    }

}
