package com.lxn.utilone.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.lzyzsd.jsbridge.BridgeWebView
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.github.lzyzsd.jsbridge.DefaultHandler
import com.google.gson.Gson
import com.lxn.utilone.R

/**
  *  @author 李晓楠
  *  功能描述: 测试js 交互数据
  *  时 间： 2022/7/15 11:48 
  */
@Route(path = ActivityConstans.JS_WebView_PATH, name = "jsWebView使用的")
class JsWebViewActivity : AppCompatActivity(),View.OnClickListener{

    private val TAG = "MainActivity"

    var webView: BridgeWebView? = null

    var button: Button? = null

    var RESULT_CODE = 0

    var mUploadMessage: ValueCallback<Uri>? = null

    internal class Location {
        var address: String? = null
    }

    internal class User {
        var name: String? = null
        var location: Location? = null
        var testStr: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_js_web_view)

        webView = findViewById<View>(R.id.webView) as BridgeWebView
        webView?.apply {
            setDefaultHandler(DefaultHandler())
            webChromeClient = object : WebChromeClient() {
                fun openFileChooser(uploadMsg: ValueCallback<Uri>, AcceptType: String?, capture: String?) {
                    this.openFileChooser(uploadMsg)
                }

                fun openFileChooser(uploadMsg: ValueCallback<Uri>, AcceptType: String?) {
                    this.openFileChooser(uploadMsg)
                }

                fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                    mUploadMessage = uploadMsg
                    pickFile()
                }
            }
            loadUrl("file:///android_asset/demo.html")
            registerHandler("submitFromWeb") { data, function ->
                Log.i(TAG, "handler = submitFromWeb, data from web = $data")
                function.onCallBack("submitFromWeb exe, response data 中文 from Java")
            }
            val user = User()
            val location = Location()
            location.address = "SDU"
            user.location = location
            user.name = "大头鬼"
            callHandler("functionInJs", Gson().toJson(user)) { }
            send("hello")
        }

        button = findViewById<View>(R.id.button) as Button
        button?.setOnClickListener(this)
    }

    fun pickFile() {
        val chooserIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooserIntent.type = "image/*"
        startActivityForResult(chooserIntent, RESULT_CODE)
    }

    override fun onClick(v: View?) {
        if (button == v) {
            webView?.callHandler("functionInJs", "data from Java", CallBackFunction { data ->
                // TODO Auto-generated method stub
                Log.i(TAG, "reponse data from js $data")
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return
            }
            val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }

}