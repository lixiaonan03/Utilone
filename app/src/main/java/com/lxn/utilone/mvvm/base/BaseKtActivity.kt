package com.lxn.utilone.mvvm.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lxn.utilone.databinding.ActivityBaseToolbarBinding
import com.lxn.utilone.databinding.TopBackBinding

/**
 *  @author 李晓楠
 *  功能描述: base基础类
 *  时 间： 2023/2/9 15:49
 */
abstract class BaseKtActivity : AppCompatActivity() {

    /**
     * UI的样式
     */
    enum class UIType {
        /**
         * 自定义的view 也就不就不包含顶部菜单栏
         */
        CUSTOM,

        /**
         * 默认也就是包含 顶部菜单栏的
         */
        DEFAULT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        if(getUiMode() == UIType.CUSTOM){
            setContentView(getView())
        }else{
            val binding = ActivityBaseToolbarBinding.inflate(layoutInflater)
            binding.frlContentView.addView(getView())
            setContentView(binding.root)
            updateTopBarView(binding.linTop,null)
//            binding.linTop.topText.setText(getToolbarTitleRes())
        }
        //TODO 这个地方得测试下
        initView(savedInstanceState)
    }

    /**
     * 设置UI的样式的
     * @return
     */
    abstract fun getUiMode(): UIType?
    /**
     * 获取页面的布局文件的View
     * @return
     */
    abstract fun getView(): View

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 修改顶部View
     */
    open fun updateTopBarView(topVb:TopBackBinding,backClick:((view:View?)->Unit)?){
        topVb.topBack.setOnClickListener {
            if(backClick!=null){
                backClick.invoke(it)
            }else{
                onBackPressedDispatcher.onBackPressed()
            }

        }
    }

    /**
     * 设置布局之前的处理
     */
    open fun beforeSetContentView(){

    }

}