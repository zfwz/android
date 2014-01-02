package com.golddragon.tools;

public interface IObserver {

	public void register();

	public void unRegister();

	public boolean isRegister();
}