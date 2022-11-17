package com.mvvm.baseapp.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fyg.networklib.result.Status
import com.mvvm.baseapp.ext.getVmClazz
import com.mvvm.baseapp.util.notNull
import com.mvvm.baseapp.viewmodel.BaseViewModel
import kotlin.properties.Delegates

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseVmActivity<VM : BaseViewModel<*>> : AppCompatActivity()  {

    var mActivity:BaseVmActivity<*> by Delegates.notNull()

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun preInit()

//    abstract fun showLoading(message: String = "请求网络中...")
//    abstract fun showLoadingDialog(message: String = "请求网络中...")
//    abstract fun dismissLoading()

    abstract fun showLoadingDialog()
    abstract fun dismissLoadingDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        initDataBind().notNull({
            setContentView(this)
        }, {
            setContentView(layoutId())
        })
        init(savedInstanceState)


    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        preInit()
        initView(savedInstanceState)
        createObserver()

    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
//        //显示弹窗
//        mViewModel.loadingChange.showDialog.observeInActivity(this, Observer {
//            showLoading(it)
//        })
//        //关闭弹窗
//        mViewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
//            dismissLoading()
//        })


        mViewModel.mUiStatus.observe(this) {
            when (it.status) {
                Status.LOADING_START -> {
                    showLoadingDialog()
                }
                Status.LOADING_END -> {
                    dismissLoadingDialog()
                }

                Status.VERIFY_ERROR -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
//                Status.ERROR -> {
//                    JXToast.showShort(it.message)
//                }
            }
        }

    }


    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind(): View? {
        return null
    }






}