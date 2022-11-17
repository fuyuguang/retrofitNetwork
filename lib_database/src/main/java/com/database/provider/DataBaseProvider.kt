package com.database.provider

import android.app.Application
import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.mvvm.provider.IApplicationProvider
import com.mvvm.provider.router.RouterPath.Provider
import kotlin.properties.Delegates


/**
 * * desc : ${desc}
 * Created by fyg on 2019-07-05.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
@Route(path = Provider.INIT_LIB_DATABASE)
class DataBaseProvider : IApplicationProvider {

    override fun init(context: Context) {
        mContext = context as Application
        Log.d("init","DataBaseProvider init");
    }

    override fun attachBaseContext(context: Application) {
        Log.d("init","DataBaseProvider attachBaseContext");
    }
    override fun onCreate(application: Application, isDebug: Boolean) {
        Debug = isDebug
        Log.d("init","DataBaseProvider onCreate");
    }

    override fun onLowMemory() {

    }
    override fun onTerminate(application: Application) {}

    companion object {
        /** lateinit 关键字 使用要求 : 只能修饰 非空类型 , 并且是 引用数据类型   */
        /** 只能修饰引用数据类型 : 不能修饰 8 88 种基本数据类型 , 否则报错 'lateinit' modifier is not allowed on properties of primitive types ;*/
        /** 只能修饰非空变量 : 不能修饰可空变量 , 否则报错信息如下 'lateinit' modifier is not allowed on properties of nullable types ;  */
         /*lateinit var Debug : Boolean */
        var Debug :Boolean by Delegates.notNull()
        lateinit var  mContext: Application
    }
}