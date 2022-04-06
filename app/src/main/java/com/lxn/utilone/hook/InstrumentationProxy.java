package com.lxn.utilone.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.lxn.utilone.util.Log;

import java.lang.reflect.Method;

/**
 * @author lixiaonan
 * 功能描述: 代理类的
 * 时 间： 2021/12/29
 */
public class InstrumentationProxy extends Instrumentation {

    Instrumentation instrumentation;

    public InstrumentationProxy(Instrumentation instrumentation) {
        Log.i("lxnHook","hook技术InstrumentationProxy");
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Log.i("lxnHook","hook技术");
        //通过反射找到 Instrumentation 的 execStartActivity
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod("execStartActivity", Context
                    .class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation,who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
