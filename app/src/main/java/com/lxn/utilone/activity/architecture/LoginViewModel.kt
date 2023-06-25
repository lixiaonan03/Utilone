package com.lxn.utilone.activity.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author：李晓楠
 * 时间：2023/6/21 14:08
 */
class LoginViewModel: ViewModel() {
    //当前页面状态和数据
    private val _viewStates = MutableStateFlow(LoginViewState())
    //MutableStateFlow 通过 asStateFlow 转换成 StateFlow(只读)
    val viewStates = _viewStates.asStateFlow()

    //登录相关的事件
    private val _viewEvents = SharedFlowEvents<LoginViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()


    fun dispatch(viewAction:LoginAction){
        when(viewAction){
            is LoginAction.UpdateUserName -> updateUserName(viewAction.userName)
            is LoginAction.UpdatePassword -> updatePassword(viewAction.password)
            is LoginAction.Login -> login()
        }
    }
    //修改用户名
    private fun updateUserName(userName: String) {
        //copy 函数是因为 LoginViewState 是data class 类的关系
        _viewStates.setState { copy(userName = userName) }
        //和上面的类似
//        _viewStates.value = _viewStates.value.copy(userName = userName)
    }
    //修改密码
    private fun updatePassword(password: String) {
        _viewStates.setState { copy(password = password) }
    }

    /**
     * 模仿登录操作
     */
    private fun login() {
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登录成功")
            }.onStart {
                _viewEvents.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                        LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast(it)
                )
            }.catch {
                _viewStates.setState { copy(password = "") }
                _viewEvents.setEvent(
                        LoginViewEvent.DismissLoadingDialog, LoginViewEvent.ShowToast("登录失败")
                )
            }.collect()
        }
    }

    //模拟登录各种操作
    private suspend fun loginLogic() {
        viewStates.value.let {
            val userName = it.userName
            val password = it.password
            delay(2000)
            throw Exception("登录失败")
//            "$userName,$password"
        }
    }

}