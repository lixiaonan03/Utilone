package com.lxn.utilone.mvvm.repository

import androidx.core.util.Pools
import com.lxn.utilone.BuildConfig
import com.lxn.utilone.mvvm.bean.*
import com.lxn.utilone.util.jvm.hashMapOf2
import com.lxn.utilone.util.toolutil.GsonUtils
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


open class BaseRepository {

    /**
     * 请求网络的
     */
    suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        //TODO 模拟测试先暂定500s for test
        delay(500)
        runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ApiEmptyResponse()
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        handlingExceptions(e)
        return ApiErrorResponse(e)
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
        return if (data.isSuccess) {
            getHttpSuccessResponse(data)
        } else {
            handlingApiExceptions(data.errorCode, data.errorMsg)
            ApiFailedResponse(data.errorCode, data.errorMsg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            ApiEmptyResponse()
        } else {
            ApiSuccessResponse(data)
        }
    }


    /**
     * 将传入的参数打包成RequestBody
     */
    private val CONTENT_TYPE_JSON = "application/json;charset=utf-8".toMediaType()
    private val CONTENT_TYPE_OCTET_STREAM = "application/octet-stream;charset=utf-8".toMediaTypeOrNull()
    private val pool = Pools.SynchronizedPool<LinkedHashMap<String, Any>>(16)

    /**
     * 生成请求体的
     * vararg 意思是可变参数,也就是可以传多个 Pair
     */
    public fun packToBody(vararg params: Pair<String, Any?>): RequestBody {
        val map = pool.acquire() ?: LinkedHashMap(16)
        for (p in params) {
            val v = p.second ?: continue
            map[p.first] = v
        }

        val body = GsonUtils.toJson(map).toRequestBody(CONTENT_TYPE_JSON)
        map.clear()
        pool.release(map)
        return body
    }

    /**
     * 传入对象当参数的
     */
    public fun packToBodyForObject(param: Any): RequestBody {
        return GsonUtils.toJson(param).toRequestBody(CONTENT_TYPE_JSON)
    }


    /**
     * 传入文件 只传入文件的
     */
    public fun packToBodyForFile(file: File): RequestBody {
        return file.asRequestBody(CONTENT_TYPE_OCTET_STREAM)
    }
    /**
     * 传入文件 只传入文件的
     */
    public fun packToBodyForFile(byte: ByteArray): RequestBody {
        return byte.toRequestBody(CONTENT_TYPE_OCTET_STREAM)
    }

    /**
     * 传入文件并携带其他参数的
     */
    public fun packToBodyForFileAndOtherPart(map: Map<String,String>,file: File,fileKey: String,fileName: String): RequestBody {
//        val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)
//        val numbersMap1 = hashMapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)
//        val maptest = hashMapOf2<String,Int>{
//            "a" toValue  1
//            "b" toValue 2
//            "c" toValue 3
//        }
        val requestBody: RequestBody = file.asRequestBody(CONTENT_TYPE_OCTET_STREAM)
        val build = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key,value) in map){
            build.addFormDataPart(key, value)
        }
        build.addFormDataPart(fileKey,fileName,requestBody)
        return  build.build()

    }



}