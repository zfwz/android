package com.golddragon.tools;

/**
 * 自定义的KeyguardScreen类
 * 
 * @author zhangfeng
 * 
 */
public interface IKeyguardScreen {
	public void onResume();

	public void onPause();

	public void onUpdate();

	boolean needsInput();

	void cleanUp();
}