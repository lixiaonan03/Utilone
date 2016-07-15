package com.lxn.utilone.util;

import android.content.Context;


import com.lxn.utilone.AppManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

/**
 * 异常处理的类
 * @author lxn
 *
 */
public class BadHandler implements UncaughtExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(BadHandler.class);
	// 是否开启Debug
	public static final boolean DEBUG = true;
	// 默认的UncaughtExceptionHandler处理类
	@SuppressWarnings("unused")
	private UncaughtExceptionHandler mDefaultHandler;
	// 实例
	private static BadHandler INSTANCE;
	@SuppressWarnings("unused")
	private Context context;

	private BadHandler() {
	}

	public static BadHandler getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new BadHandler();
		}
		return INSTANCE;
	}

	public void init(Context ctx) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.context = ctx;
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (DEBUG) {
			writerLog(ex);
		}else{
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}
	/**
	 * 写入SD卡中
	 * @param ex
	 */
	private void writerLog(Throwable ex) {
		if (ex == null) {
			return;
		}
		logger.error(getDriverInfo().toString());
		logger.error("BadHandler", ex);
		//ex.printStackTrace();
		//ShopApplication.doEdit();
		AppManager.getAppManager().AppExit();
		System.exit(1);
		android.os.Process.killProcess(android.os.Process.myPid());

	/*	// 重启应用
		context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
		//干掉当前的程序
		AppManager.getAppManager().AppExit();
		android.os.Process.killProcess(android.os.Process.myPid());*/
	}

	// 获取时间和手机信息
	private StringBuilder getDriverInfo() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日   HH:mm:ss ");
		sb.append("日期:");
		sb.append(formatter.format(System.currentTimeMillis()));
		sb.append("\n 手机型号:");
		sb.append(android.os.Build.MODEL);
		sb.append("\n 手机系统版本:");
		sb.append(android.os.Build.VERSION.RELEASE);
		sb.append("\n");
		return sb;
	}

}
