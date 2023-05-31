package com.lxn.utilone.activity.uiconfig

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lxn.utilone.databinding.DialogMearsueBinding

/**
 * @author：李晓楠
 * 时间：2023/5/12 11:11
 */
class MyDialogFragment : DialogFragment() {


    private lateinit var binding: DialogMearsueBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // 加载布局文件
        // 返回布局文件对应的 View
        binding = DialogMearsueBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val params = dialog!!.window!!.attributes
        val sss = params.width == ViewGroup.LayoutParams.WRAP_CONTENT
        val a1 = params.horizontalWeight
        val b1 = params.verticalWeight

        Log.i("lxn438", "horizontalWeight==" + a1 + "verticalWeight==" + b1)


        //设置dialog宽高
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        //如果宽度设置成固定就好使
        val w1 =  activity?.window?.decorView?.width
        params.width = w1 ?:1080
        dialog!!.window!!.attributes = params


        //设置对话框背景色透明
        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        dialog!!.window!!.setBackgroundDrawable(colorDrawable) //设置透明背景
        //设置透明背景
        dialog!!.window!!.setDimAmount(0f)
    }


    fun setContent(str:String){
        binding.tvText.text = str
    }
}