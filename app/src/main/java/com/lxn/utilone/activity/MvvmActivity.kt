package com.lxn.utilone.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.databinding.ActivityMvvmBinding
import com.lxn.utilone.mvvm.ApiViewModel
import com.lxn.utilone.mvvm.AppViewModel
import com.lxn.utilone.mvvm.bean.WxArticleBean
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.operationutil.ThreadUtils

/**
 *  @author 李晓楠
 *  功能描述: MVVM 模式的学习 尤其是网络库的使员工
 *  关于网络模块整体参考的： https://github.com/ldlywt/FastJetpack
 *  时 间： 2022/10/27 11:18
 */
@Route(path = ActivityConstans.MVVM_PATH, name = "mvvm模式学习")
class MvvmActivity : BaseActivity() {

    private lateinit var binding: ActivityMvvmBinding

    private val mViewModel by viewModels<ApiViewModel>()
    private val appViewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvvmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.linTop.topText.text = "mvvm模式学习"

        binding.tvLogin.setOnClickListener {
            //TODO 模拟登录的 在这个地方模拟  网络请求完成之后很快关闭看网络请求是否会取消
            //retrofit 取消的流程可以参考  https://www.jianshu.com/p/00e313e0bb93
            mViewModel.login("FastJetpack", "FastJetpack")

            appViewModel.requestFromDb()
            Log.i("net","点击完成之后")
            ThreadUtils.runOnUiThreadDelayed({finish()},500)
        }


        mViewModel.userLiveData.observeState(this) {
            onSuccess {
                binding.tvContent.text = it.toString()
            }
            onComplete { //可以关闭加载框
            }
        }

        mViewModel.mediatorLiveDataLiveData.observe(this) {
            binding.tvSourcesNetContent.text = it.data.toString()
        }
        //模拟请求网络数据的
        binding.tvSourcesNet.setOnClickListener {
            binding.tvSourcesNetContent.text = ""
            mViewModel.requestFromNet()
        }
        //模拟请求从数据库拿数据的
        binding.tvSourcesDb.setOnClickListener {
            binding.tvSourcesNetContent.text = ""
            mViewModel.requestFromDb()
        }

        binding.tvNet.setOnClickListener {
            mViewModel.requestNet()
        }
        mViewModel.wxArticleLiveData.observeState(this) {
            onSuccess { data: List<WxArticleBean> -> binding.tvNetContent.text = data.toString() }

            onFailed { code, msg ->
            }

            onException {
                //请求失败之后的处理的
            }

            onEmpty {
            }

            onComplete {
                //请求完成的处理的
            }
        }
        binding.tvNetError.setOnClickListener {
            mViewModel.requestNetError()
        }
        mViewModel.wxArticleLiveData.observeState(this) {
            onSuccess { data: List<WxArticleBean> -> binding.tvNetErrorContent.text = data.toString() }

            onFailed { code, msg ->
            }

            onException {
                //请求失败之后的处理的
                binding.tvNetErrorContent.text = it.message
            }

            onEmpty {
            }

            onComplete {
                //请求完成的处理的
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("net","onDestroy()==被回调")
    }
}