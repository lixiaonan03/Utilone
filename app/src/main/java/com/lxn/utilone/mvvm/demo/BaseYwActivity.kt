package com.lxn.utilone.mvvm.demo

import com.kaopiz.kprogresshud.KProgressHUD
import com.lxn.utilone.mvvm.BaseViewModel
import com.lxn.utilone.mvvm.base.BaseVmActivity

/**
 * 业务集成的普通的
 * @author：李晓楠
 * 时间：2023/2/13 10:57
 */
abstract class BaseYwActivity<VM : BaseViewModel> : BaseVmActivity<VM>(){

    private var mProgressHUD:KProgressHUD? = null
    override fun createObserver() {
        //显示加载转圈进度条的
        mViewModel.showLoading.observe(this) {
             if(it){
                 showProgress(true)
             }else{
                 dismissProgress()
             }
        }
    }


    /**
     * 同步方法 可覆写的
     */
    @Synchronized
    open fun getProgress(): KProgressHUD {
        if (mProgressHUD == null) {
            mProgressHUD = KProgressHUD.create(this)
        }
        return mProgressHUD ?: KProgressHUD.create(this)
    }

    open fun showProgress(cancelAble: Boolean) {
        if (!isFinishing && !isDestroyed) {
            getProgress().setCancellable(cancelAble)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).show()
        }
    }

    open fun showProgress(percentsProgress: Int) {
        getProgress().setCancellable(true).setMaxProgress(100).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE).show()
        getProgress().setProgress(percentsProgress)
    }

    open fun dismissProgress() {
        if (!isFinishing || !isDestroyed) {
            getProgress().dismiss()
        }
    }
}

