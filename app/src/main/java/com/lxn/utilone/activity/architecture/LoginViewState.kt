package com.lxn.utilone.activity.architecture

/**
 * 各种状态
 * @author：李晓楠
 * 时间：2023/6/21 14:14
 */
data class LoginViewState(val userName: String = "", val password: String = "") {

    //是否可以登录的判断
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6
    //是否展示密码提示的
    val passwordTipVisible: Boolean
        get() = password.length in 1..5
}

/**
Sealed class（密封类） 是一个有特定数量子类的类，看上去和枚举有点类似，所不同的是，在枚举中，我们每个类型只有一个对象（实例）；而在密封类中，同一个类可以拥有几个对象。
Sealed class（密封类）的所有子类都必须与密封类在同一文件中
Sealed class（密封类）的子类的子类可以定义在任何地方，并不需要和密封类定义在同一个文件中
Sealed class（密封类）没有构造函数，不可以直接实例化，只能实例化内部的子类
 */
sealed class LoginViewEvent {
    //展示吐司
    data class ShowToast(val message: String) : LoginViewEvent()
    //展示加载进度条
    object ShowLoadingDialog : LoginViewEvent()
    //取消加载加载进度条
    object DismissLoadingDialog : LoginViewEvent()
}

/**
 * 登录相关的一些动作
 */
sealed class LoginAction{
    data class UpdateUserName(val userName: String) : LoginAction()
    data class UpdatePassword(val password: String) : LoginAction()
    object Login : LoginAction()
}