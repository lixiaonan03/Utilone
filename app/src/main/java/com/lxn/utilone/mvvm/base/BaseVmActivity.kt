package com.lxn.utilone.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.mvvm.BaseViewModel

/**
  *  @author 李晓楠
  *  功能描述: 参考网上的例子整理的base类          https://github.com/hegaojian/JetpackMvvm
  *  时 间： 2023/2/9 14:13
  */
abstract class BaseVmActivity<VM : BaseViewModel> : BaseKtActivity() {

    lateinit var mViewModel: VM



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()

    }


    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

}