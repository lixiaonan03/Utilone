package com.lxn.utilone.activity.architecture

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.lxn.utilone.activity.BaseActivity
import com.lxn.utilone.databinding.ActivityArchitectureBinding
import com.lxn.utilone.databinding.ActivityLoginBinding
import com.lxn.utilone.databinding.ActivityRvBinding
import com.lxn.utilone.databinding.ActivitySuanfaBinding
import com.lxn.utilone.util.ToastUtils

/**
  *  @author 李晓楠
  *  功能描述: 是用flow 写的登录界面的 使用mvi 架构的
  *  时 间： 2023/6/21 13:56
  */
class FlowLoginActivity : BaseActivity(){

     companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, FlowLoginActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context?.startActivity(intent)
        }
    }

    private lateinit var vb: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.linTop.topText.text = "mvi架构登录界面"


        vb.editUserName.addTextChangedListener {
            viewModel.dispatch(LoginAction.UpdateUserName(it.toString()))
        }
        vb.editPassword.addTextChangedListener {
            viewModel.dispatch(LoginAction.UpdatePassword(it.toString()))
        }
        vb.btnLogin.setOnClickListener {
            viewModel.dispatch(LoginAction.Login)
        }

        initViewStates()
        initViewEvents()
    }


    /**
     * 处理状态
     */
    private fun initViewStates() {
        viewModel.viewStates.let { states ->
            states.observeState(this, LoginViewState::userName) {
                vb.editUserName.setText(it)
                vb.editUserName.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::password) {
                vb.editPassword.setText(it)
                vb.editPassword.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::isLoginEnable) {
                vb.btnLogin.isEnabled = it
                vb.btnLogin.alpha = if (it) 1f else 0.5f
            }
            states.observeState(this, LoginViewState::passwordTipVisible) {
                vb.tvLabel.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun initViewEvents() {
        viewModel.viewEvents.observeEvent(this) {
            when (it) {
                is LoginViewEvent.ShowToast -> ToastUtils.toastshort(it.message)
                is LoginViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is LoginViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
            }
        }
    }

    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }
}