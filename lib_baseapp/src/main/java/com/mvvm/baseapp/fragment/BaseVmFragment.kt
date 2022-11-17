package com.mvvm.baseapp.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.fyg.networklib.result.Status
import com.mvvm.baseapp.IDataSource
import com.mvvm.baseapp.ext.getDataSourceClazz
import com.mvvm.baseapp.ext.getVmClazz
import com.mvvm.baseapp.viewmodel.BaseViewModel


/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment
 */

//abstract class BaseVmFragment<VM : BaseViewModel,DataSource : IDataSource> : Fragment() {
abstract class BaseVmFragment<VM : BaseViewModel<*>> : Fragment() {


    abstract fun showLoadingDialog()
    abstract fun dismissLoadingDialog()


    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        registorDefUIChange()
        initData()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    private fun createViewModel2(): VM {
        return ViewModelProvider(this,ViewModelFactory(
            IDataSource.createDataSource(getDataSourceClazz(this))
        )).get(getVmClazz(this))
    }


    class ViewModelFactory(private val stockPriceDataSource: IDataSource) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val newInstance = modelClass.newInstance();


            return newInstance
//            return modelClass.getConstructor(IDataSource::class.java)
//                .newInstance(stockPriceDataSource)
        }
    }

    open class NewInstanceFactoryTest : Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return try {
                modelClass.newInstance()
            } catch (e: java.lang.InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }

        companion object {
            private var sInstance: NewInstanceFactoryTest? = null

            /**
             * Retrieve a singleton instance of NewInstanceFactory.
             *
             * @return A valid [NewInstanceFactory]
             */
            val instance: NewInstanceFactoryTest
                get() {
                    if (sInstance == null) {
                        sInstance = NewInstanceFactoryTest()
                    }
                    return sInstance!!
                }
        }
    }

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)


    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
    }


    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}

//    abstract fun showLoading(message: String = "请求网络中...")
//
//    abstract fun dismissLoading()

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
//        mViewModel.loadingChange.showDialog.observeInFragment(this, Observer {
//            showLoading(it)
//        })
//        mViewModel.loadingChange.dismissDialog.observeInFragment(this, Observer {
//            dismissLoading()
//        })


        mViewModel.mUiStatus.observe(this.viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING_START -> {
                    showLoadingDialog()
                }
                Status.LOADING_END -> {
                    dismissLoadingDialog()
                }

                Status.VERIFY_ERROR -> {
                    Toast.makeText(mActivity,it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}