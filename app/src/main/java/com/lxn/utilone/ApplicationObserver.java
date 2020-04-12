package com.lxn.utilone;



import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @author lixiaonan
 * 功能描述: 添加全局生命周期的监听的
 * 时 间： 2019-09-07 16:52
 */
public class ApplicationObserver implements DefaultLifecycleObserver {
    private String TAG = this.getClass().getName();

    public ApplicationObserver() {

    }
    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    /**
     * 应用程序出现到前台时调用
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    /**
     * 应用程序出现到前台时调用
     */
    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        //TODO 注意，这一类的生命周期回调，都存在700ms的延迟处理，并不是立即执行的  原因： ProcessLifecycleOwner#activityResumed()
        // 快速的前后台切换很可能导致生命周期的回调不触发
    }

    /**
     * 应用程序退出到后台时调用
     */
    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    /**
     * 应用程序退出到后台时调用
     */
    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
    }
}
