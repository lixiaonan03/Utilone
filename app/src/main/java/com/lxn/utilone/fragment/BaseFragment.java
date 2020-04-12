package com.lxn.utilone.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * 基本的fragment  这个是根据 setUserVisibleHint 这个方法来的 但是v4包中的fragment的这个方法有点问题 不回调 所以用另一个
 */
public class BaseFragment extends Fragment {

    /**
     * 是否初始化完毕
     */
    private boolean isPrepared;

    /**
     * 是否第一次调用onResume()
     */
    private boolean isFirstResume = true;

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initPrepare(savedInstanceState);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (!isHidden()) {
            onFragmentResume();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!isHidden()) {
            onFragmentPause();
        }
    }

    /**
     * 当前fragment显示状态发生改变时执行的方法 隐藏是 hidden值为true
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        if (!hidden) {
            onFragmentResume();
        } else {
            //第一次fragment不可见是的处理
			/*if (isFirstInvisible) {
				isFirstInvisible = false;
				onFirstInvisible();
			} else {
				onFragmentPause();
			}*/
            onFragmentPause();
        }
        super.onHiddenChanged(hidden);
    }


    private synchronized void initPrepare(Bundle savedInstanceState) {
        if (!isPrepared) {
            onFirstVisible(savedInstanceState);
            onFragmentResume();
        } else {
            isPrepared = true;
        }
    }


    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstInvisible() {
    }


    /**
     * 第一次fragment可见（进行初始化工作）
     *
     * @param savedInstanceState
     */
    public void onFirstVisible(Bundle savedInstanceState) {

    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onFragmentResume() {
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onFragmentPause() {
    }
}
