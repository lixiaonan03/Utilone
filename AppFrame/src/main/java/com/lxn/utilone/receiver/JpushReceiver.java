package com.lxn.utilone.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import com.lxn.utilone.activity.Welcome;
import com.lxn.utilone.util.LogUtils;
import com.lxn.utilone.util.ToastUtils;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @copyright:北京爱钱帮财富科技有限公司 功能描述: 极光推送服务的receiver处理
 * 作 者:  李晓楠
 * 时 间： 2017/1/13 10:06
 */
public class JpushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface
                    .EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            ToastUtils.toastshort("走JPushInterface.ACTION_MESSAGE_RECEIVED");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");


            //判断当前的应用是否在
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                    .ACTIVITY_SERVICE);
            //TODO 使用RunningTaskInfo判断当前应用启动task还在不 如果在的话调用到前台来没用启动栈
           /* List<ActivityManager.RunningTaskInfo> recentTasks = null;
            try {
                recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
            } catch (SecurityException e) {
                LogUtils.i("报错====="+e.getLocalizedMessage());
                e.printStackTrace();
            }
            boolean isgo=true;
            if(null!=recentTasks){
                LogUtils.i("recentTasks====="+recentTasks.size());
                for(ActivityManager.RunningTaskInfo one:recentTasks){
                    if(one.topActivity.getPackageName().equals(context.getPackageName())){
                        if (one.id>= 0) {
                            try {
                                isgo=false;
                                Intent intent4 = new Intent(Intent.ACTION_MAIN);
                                intent4.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent4.setClass(context, Welcome.class);
                                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                context.startActivity(intent4);
                                activityManager.moveTaskToFront(one.id, ActivityManager
                                        .MOVE_TASK_WITH_HOME);
                               activityManager.moveTaskToFront(one.id,0);
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogUtils.i("迁移报错==="+e.getLocalizedMessage());
                            }
                        }
                    }
                }
                if(isgo){
                    Intent intent22=new Intent(context, Welcome.class);
                    intent22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                            .FLAG_ACTIVITY_CLEAR_TOP|Intent
                            .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(intent22);
                }
            }else{
                Intent intent22=new Intent(context, Welcome.class);
                intent22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_CLEAR_TOP|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent22);
            }*/


            boolean isgo = true;
            if (Build.VERSION.SDK_INT > 21) {
                // 5.0及其以后的版本的处理
                List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
                if (null != tasks && tasks.size() > 0) {
                    for (ActivityManager.AppTask task : tasks) {
                        if (task.getTaskInfo().baseIntent.getComponent().getPackageName().equals(context
                                .getPackageName())) {
                            //跳转到前台
                            isgo = false;
                            task.moveToFront();
                            //如果需要跳转activity
                            //task.startActivity(context,intent,null);
                            break;
                        }
                    }
                }
            } else {
                // 5.0之前 获取正在运行的任务栈(一个应用程序占用一个任务栈) 最近使用的任务栈会在最前面
                List<ActivityManager.RecentTaskInfo> recentTasks1 = null;
                try {
                    recentTasks1 = activityManager.getRecentTasks(Integer.MAX_VALUE, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if (null != recentTasks1) {
                    for (ActivityManager.RecentTaskInfo one : recentTasks1) {
                        LogUtils.i("=one.origActivity==="+one.baseIntent.getComponent()
                                .getClassName());
                        if (one.baseIntent.getComponent()
                                .getPackageName().equals(context.getPackageName())) {
                            if (one.id >= 0) {
                                try {
                                    isgo = false;
                                    activityManager.moveTaskToFront(one.persistentId, ActivityManager.MOVE_TASK_WITH_HOME);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            if (isgo) {
                Intent intent22 = new Intent(context, Welcome.class);
                intent22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                        .FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent22);
            }



          /*  ActivityManager activityManager = (ActivityManager) context.getSystemService
          (Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
            .getRunningAppProcesses();
            LogUtils.i("运行context.getPackageName()====="+context.getPackageName());
            LogUtils.i("运行list====="+appProcesses.size());
            for (int i=0;i<appProcesses.size();i++){
                ActivityManager.RunningAppProcessInfo appProcess = appProcesses.get(i);
                if (appProcess.processName.equals(context.getPackageName())) {
                    LogUtils.i("匹配时的===="+i);
                    List<ActivityManager.RunningTaskInfo> recentTasks = null;
                    try {
                            recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
                    } catch (SecurityException e) {
                            LogUtils.i("报错====="+e.getLocalizedMessage());
                            e.printStackTrace();
                    }
                    boolean isgo=true;
                    for(ActivityManager.RunningTaskInfo one:recentTasks){
                            if(one.topActivity.getPackageName().equals(context.getPackageName())){
                                // This is an active task; it should just go to the foreground.
                                if (one.id>= 0) {
                                    LogUtils.i("抢====");
                                    try {
                                        isgo=false;
                                        activityManager.moveTaskToFront(one.id, ActivityManager
                                                    .MOVE_TASK_WITH_HOME);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogUtils.i("迁移报错==="+e.getLocalizedMessage());
                                    }
                                }
                            }
                    }
                    if(isgo){
                        Intent intent22=new Intent(context, Welcome.class);
                        intent22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                                .FLAG_ACTIVITY_CLEAR_TASK|Intent
                                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        context.startActivity(intent22);
                    }
                }else{
                    //没有存在当前应用的进程

                }*/
        }else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString
                    (JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface
                    .EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " +
                    "" + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


        /**
         * 判断应用当前是否运行中，包括后台运行
         * @param context
         * @return
         */

    private boolean isApplicationRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : taskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }



        return false;
    }


}

