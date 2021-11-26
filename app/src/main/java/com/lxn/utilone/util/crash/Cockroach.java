package com.lxn.utilone.util.crash;

import android.os.Looper;


/**
 *  @author https://github.com/android-notes/Cockroach/tree/X
 *  功能描述: 统一异常处理的
 *  时 间： 2019-11-15 12:58
 */
public final class Cockroach {

    private static ExceptionHandler sExceptionHandler;
    //标记位，避免重复安装卸载
    private static boolean sInstalled = false;
    private static boolean sIsSafeMode;

    private Cockroach() {
    }

    public static void install(ExceptionHandler exceptionHandler) {
        if (sInstalled) {
            return;
        }
        sInstalled = true;
        sExceptionHandler = exceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (sExceptionHandler != null) {
                    sExceptionHandler.uncaughtExceptionHappened(t, e);
                }
                if (t == Looper.getMainLooper().getThread()) {
                    isChoreographerException(e);
                    safeMode();
                }
            }
        });

    }

    private static void notifyException(Throwable throwable) {
        if (sExceptionHandler == null) {
            return;
        }
        if (isSafeMode()) {
            sExceptionHandler.bandageExceptionHappened(throwable);
        } else {
            sExceptionHandler.uncaughtExceptionHappened(Looper.getMainLooper().getThread(), throwable);
            safeMode();
        }
    }

    public static boolean isSafeMode() {
        return sIsSafeMode;
    }

    private static void safeMode() {
        sIsSafeMode = true;
        if (sExceptionHandler != null) {
            sExceptionHandler.enterSafeMode();
        }
        while (true) {
            try {
                Looper.loop();
            } catch (Throwable e) {
                isChoreographerException(e);
                if (sExceptionHandler != null) {
                    sExceptionHandler.bandageExceptionHappened(e);
                }
            }
        }
    }

    /**
     * view measure layout draw时抛出异常会导致Choreographer挂掉
     * <p>
     * 建议直接杀死app。以后的版本会只关闭黑屏的Activity
     *
     * @param e
     */
    private static void isChoreographerException(Throwable e) {
        if (e == null || sExceptionHandler == null) {
            return;
        }
        StackTraceElement[] elements = e.getStackTrace();
        if (elements == null) {
            return;
        }

        for (int i = elements.length - 1; i > -1; i--) {
            if (elements.length - i > 20) {
                return;
            }
            StackTraceElement element = elements[i];
            if ("android.view.Choreographer".equals(element.getClassName())
                    && "Choreographer.java".equals(element.getFileName())
                    && "doFrame".equals(element.getMethodName())) {
                sExceptionHandler.mayBeBlackScreen(e);
                return;
            }

        }
    }


}
