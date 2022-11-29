package com.lxn.utilone.mvvm.bean

import com.google.gson.JsonParseException
import com.lxn.utilone.util.ToastUtils
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

/**
 * 一些枚举错误
 */
enum class HttpError(var code: Int, var errorMsg: String) {
    TOKEN_EXPIRE(3001, "token is expired"),
    PARAMS_ERROR(4003, "params is error") // ...... more
}

/**
 * 处理一些自定义code 错误的
 */
internal fun handlingApiExceptions(code: Int?, errorMsg: String?) = when (code) {
    HttpError.TOKEN_EXPIRE.code -> ToastUtils.toastshort(HttpError.TOKEN_EXPIRE.errorMsg)
    HttpError.PARAMS_ERROR.code -> ToastUtils.toastshort(HttpError.PARAMS_ERROR.errorMsg)
    else -> errorMsg?.let {
        ToastUtils.toastshort(it)
    }
}

/**
 * 处理一些网络错误
 */
internal fun handlingExceptions(e: Throwable) = when (e) {
    is HttpException -> ToastUtils.toastshort(e.message())

    is CancellationException -> {
    }
    is SocketTimeoutException -> {
    }
    is JsonParseException -> {
    }
    else -> {
    }
}