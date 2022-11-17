package com.mvvm.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.mvvm.baseapp.ext.inflateBindingWithGeneric
import com.mvvm.baseapp.viewmodel.BaseViewModel
import kotlin.properties.Delegates

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : BaseVmFragment<VM>() {

    override fun layoutId() = 0

    //该类绑定的ViewDataBinding
     var mDatabind: DB by Delegates.notNull()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDatabind = inflateBindingWithGeneric(inflater, container, false)
        preInit()
        return mDatabind.root
    }

    abstract fun preInit()

    override fun onDestroyView() {
        super.onDestroyView()
        mDatabind.lifecycleOwner = null
    }
}