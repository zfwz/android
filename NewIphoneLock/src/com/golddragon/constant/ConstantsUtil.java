package com.golddragon.constant;

import android.net.Uri;

/**
 * 常量类，保存的是数据库表字段名 以及 uri相关
 * @author zhangfeng
 */
public class ConstantsUtil {

	public static final String TABLE = "lockscreen";
	public static final String ID = "_id";
	public static final String FLAG = "flag";
	public static final int VERSION = 1;

	public static final String SCHEME = "content://";
	// authority必须与manifest中配置一致，其作用是解析该uri时可以找到对应的ContentProvider
	public static final String AUTHORITY = "com.golddragon.lockscreen";
	public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/"
			+ TABLE);

	// Mime类型所对应的匹配码
	public static final int CODE_ALL_DATA = 1;
	public static final int CODE_SPECIFIC_DATA = 2;
	// Mime类型
	public static final String CONTENT_ALL_TYPE = "vnd.android.cursor.item/"
			+ TABLE;// 单一数据
	public static final String CONTENT_SPECIFIC_TYPE = "vnd.android.cursor.dir/"
			+ TABLE;// 多项数据


}
