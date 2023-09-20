package com.lxn.utilone.activity

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lxn.utilone.databinding.ActivityDevBinding
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.ToastUtils


/**
 *  @author 李晓楠
 *  功能描述: 开发者页面
 *  时 间： 2023/1/5 15:49
 */
class DevActivity : BaseActivity() {

    private lateinit var binding: ActivityDevBinding


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) { //根据头条的方案设置的屏幕适配方案的
        super.onCreate(savedInstanceState)
        binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.linTop.topText.text = "开发者管理页面"
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.appTasks // 获取所有应用任务列表
        var numActivitiesInTask = 0
        for (taskInfo in tasks) {
            Log.i("lxnPush", "taskInfo==${taskInfo.taskInfo.taskId} === ${taskInfo.taskInfo.toString()}")
            if (taskInfo.taskInfo.taskId == taskId) {
                numActivitiesInTask = taskInfo.taskInfo.numActivities
                break
            }
        }

        //测试context 的问题
        Log.i("lxnContext", "Application:   $application ")
        Log.i("lxnContext", "ApplicationContext:   ${applicationContext} ")
        Log.i("lxnContext", "Activity:   ${this@DevActivity} ")
        Log.i("lxnContext", "Application BaseContext:   ${application.baseContext} ")
        Log.i("lxnContext", "Activity BaseContext:    ${baseContext} ")
        Log.i("lxnPush", "开发者管理页===taskid==${taskId}=${numActivitiesInTask}==: ${intent.toString()}==data=${intent.data.toString()}====extras=${intent.extras.toString()}")
        testFireBaseEvent()
        //模拟重启应用的
        binding.tvReStartApp.setOnClickListener {
            ToastUtils.toastshort("应用重启中")

            // 一种重启方式
//            val intent = packageManager.getLaunchIntentForPackage(application.packageName)
//            val restartIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
//            val mgr: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, restartIntent) // 1秒钟后重启应用
//            exitProcess(0)

            //另一种
            val i = packageManager.getLaunchIntentForPackage(packageName)
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                Process.killProcess(Process.myPid())
            }
        }

        //整个页面置灰的效果
        binding.tvGrey.setOnClickListener {
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            //设置灰度 0最灰  1最亮
            colorMatrix.setSaturation(0.1f)
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
        }

        //gps 调用测试的
        binding.tvGpsTest.setOnClickListener {
            XXPermissions.with(this)
                    // 申请单个权限
                    .permission(Permission.ACCESS_COARSE_LOCATION)
                    // 申请多个权限
                    .permission(Permission.ACCESS_FINE_LOCATION)
                    // 设置权限请求拦截器（局部设置）
                    //.interceptor(new PermissionInterceptor())
                    // 设置不触发错误检测机制（局部设置）
                    //.unchecked()
                    .request(object : OnPermissionCallback {

                        override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                            if (!allGranted) {
                                ToastUtils.toastshort("获取部分权限成功，但部分权限未正常授予")
                                return
                            }
                            //获取成功
                            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            val criteria = Criteria()
                            criteria.powerRequirement = Criteria.POWER_HIGH
                            criteria.isAltitudeRequired = true
//                            val provider = locationManager.getBestProvider(criteria,false) // 或 LocationManager.NETWORK_PROVIDER
                            val provider = LocationManager.NETWORK_PROVIDER // 或 LocationManager.NETWORK_PROVIDER
                            provider?.let {
                                val location = if (ActivityCompat.checkSelfPermission(this@DevActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@DevActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ToastUtils.toastshort("没有权限")
                                } else {
                                    locationManager.getLastKnownLocation(provider)
                                }
                                Log.i("lxntest", "location==${location.toString()}==provider==${provider}")
                                locationManager.requestLocationUpdates(provider, 500, 200f, locationListener)
                            }
                        }

                        override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                            if (doNotAskAgain) {
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(this@DevActivity, permissions)
                            } else {
                                ToastUtils.toastshort("权限获取失败")
                            }
                        }
                    })
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 处理位置信息变化
            val latitude = location.latitude
            val longitude = location.longitude
            // ...
            Log.i("lxntest", "更新之后的=====location==${location.toString()}")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // 位置提供者状态变化时的处理
            Log.i("lxntest", "更新之后的=====onStatusChanged==")
        }

        override fun onProviderEnabled(provider: String) {
//            super.onProviderEnabled(provider)
            // 位置提供者启用时的处理
            Log.i("lxntest", "onProviderEnabled==")
        }

        override fun onProviderDisabled(provider: String) {
//            super.onProviderDisabled(provider)
            // 位置提供者禁用时的处理
            Log.i("lxntest", "onProviderDisabled==")
        }

    }

    private fun testFireBaseEvent() {
        var bundle = Bundle()
        bundle.putString("my_custom_event", "custom log message")

        FirebaseAnalytics.getInstance(this@DevActivity).logEvent("lxntest2", bundle)


    }
}