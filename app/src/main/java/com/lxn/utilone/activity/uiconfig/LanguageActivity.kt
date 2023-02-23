package com.lxn.utilone.activity.uiconfig

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxn.utilone.R
import com.lxn.utilone.activity.ActivityConstans
import com.lxn.utilone.databinding.ActivityLanguageBinding
import com.lxn.utilone.databinding.TopBackBinding
import com.lxn.utilone.mvvm.base.BaseKtActivity
import com.lxn.utilone.util.Log
import com.lxn.utilone.util.ToastUtils
import java.util.*

/**
 *  @author 李晓楠
 *  功能描述: 语言设置学习的
 *  时 间： 2023/2/23 15:32
 */
@Route(path = ActivityConstans.LANGUAGE_PATH, name = "语言设置学习的")
class LanguageActivity : BaseKtActivity() {

    private lateinit var binding: ActivityLanguageBinding


    override fun attachBaseContext(newBase: Context) {
        //在页面初始化的时候 设置新的资源配置。这个地方有几个地方需要注意：
//        1、 newBase.resources.configuration  这个地方一定要这样使用,如果直接使用 resources 这个会报null
//          因为newbase是个 contextImpl 的类型但resources 直接调用获取的 AppCompatActivity 的方法
//        2、createConfigurationContext 这个方法同样需要指定调用的对象为newBase ，同时要把生成的context对象传入之后的使用中
//        确保在调用 createConfigurationContext 方法之后，使用返回的新 Context 对象来重新启动 Activity。
        val aa = (1..10).random()
        val newLocale = if (aa % 2 == 0) Locale.CHINA else Locale.ENGLISH
        val configuration = Configuration(newBase.resources.configuration)
        configuration.setLocale(newLocale)
        val context = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(context)

        /**
        ContextImpl 是 Android 中的一个核心类，它是 Context 接口的实现类之一，提供了许多 Android 应用程序所需的基本功能。下面简单介绍一下 ContextImpl 类的一些主要特点：

        生命周期管理：ContextImpl 继承自 ContextWrapper，因此它可以作为一个 Context 对象来使用，并可以嵌套包含其他 Context。它实现了 ActivityLifecycleCallbacks 接口，可以跟踪应用程序组件的生命周期状态，并在应用程序启动、停止或销毁时执行一些特定的操作。

        资源访问：ContextImpl 通过 getResources() 方法提供了对应用程序资源的访问。它实现了 Resources.Theme 接口，可以使用 getTheme() 方法来获取当前主题，使用 setTheme() 方法来更改当前主题。

        应用程序环境：ContextImpl 维护了一些关键的应用程序环境信息，如包名、进程名、应用程序版本、应用程序签名、应用程序目录、缓存目录等等。

        系统服务：ContextImpl 通过 getSystemService() 方法提供了对 Android 系统服务的访问。系统服务包括 ActivityManager、WindowManager、NotificationManager、PowerManager、TelephonyManager、ConnectivityManager 等等。

        总之，ContextImpl 类提供了许多基础功能，是 Android 应用程序开发中不可或缺的一部分。*/
    }


    override fun getUiMode(): UIType? {
        return UIType.DEFAULT
    }

    override fun getView(): View {
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun updateTopBarView(topVb: TopBackBinding, backClick: ((view: View?) -> Unit)?) {
        super.updateTopBarView(topVb, backClick)
        topVb.topText.text = "关于语言设置的学习"
    }
/**
    通常情况下，当配置发生更改时，Android 会销毁并重新创建当前活动（Activity）以适应新的配置
    。不过，你可以通过在 AndroidManifest.xml 文件中为活动设置 android:configChanges 属性来指定某些配置发生更改时不销毁活动。
    在这种情况下，当配置发生更改时，系统会调用 onConfigurationChanged() 方法，应用程序可以在该方法中进行必要的处理，例如重新加载资源、重新计算布局等。

   默认情况下在系统设置中修改语言并不会回调到 activity基本中的 onConfigurationChanged() 方法中，
    只有在  AndroidManifest.xml 文件中配置了：   android:configChanges="locale|layoutDirection"  这种属性才会回调到这个方法中，回调到了就需要自己针对资源的变动做处理。
 */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        android.util.Log.i("lxn1234567","===${newConfig.locales.toString()}")
        recreate()

    }

    override fun initView(savedInstanceState: Bundle?) {


        binding.text11.text = getString(R.string.lxn_text) + intent.getIntExtra("lxn", 0)
        binding.tvChange1111.setOnClickListener {
            updateResources()

            //调用 recreate() 方法会销毁当前 Activity 并重新创建一个新的 Activity 实例，从而重新加载布局和资源。这会导致屏幕短暂闪烁，
            // 因为在销毁旧 Activity 的同时，新 Activity 还没有完全创建完成，此时屏幕上可能会出现空白或黑色的区域。
            recreate()
        }


    }

    /**
     * 用旧方法做资源配置的更换处理,处理完成之后记得重启打开一个实例 刷新当前界面
     */
    private fun updateResources() {
        val aa = (1..10).random()
        val newLocale = if (aa % 2 == 0) Locale.CHINA else Locale.ENGLISH
        val res: Resources = resources
        val configuration = res.configuration
        configuration.locale = newLocale
        res.updateConfiguration(configuration, res.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("net", "onDestroy()==被回调")
    }
}