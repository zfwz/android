package com.golddragon.receiver;

import com.golddragon.service.IPhoneLockService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听开机广播
 * 
 * @author zhangfeng
 * 
 */
public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { // boot
			context.startService(new Intent(context, IPhoneLockService.class));
		}
	}
}
