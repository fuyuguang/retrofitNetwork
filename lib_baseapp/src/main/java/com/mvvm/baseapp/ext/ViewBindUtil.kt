package com.mvvm.baseapp.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * 作者　: hegaojian
 * 时间　: 2021/12/21
 * 描述　:
 */

@JvmName("inflateWithGeneric")
 fun < VB : ViewBinding> AppCompatActivity.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
    }


//@JvmName("inflateWithGeneric")
inline fun <reified VB : ViewBinding> AppCompatActivity.inflateBindingWithGenericTest(layoutInflater: LayoutInflater): VB {



    val viewbinding = VB::class.java
    return (viewbinding.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB).also {
        if (it is ViewDataBinding){
//            it.lifecycleOwner = this
        }
    }


//    return withGenericBindingClass<VB>(this) { clazz ->
//        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
//
//    }.also { binding ->
//        if (binding is ViewDataBinding) {
//            binding.lifecycleOwner = this
//        }
//    }
}



//@JvmName("inflateWithGeneric")
inline fun <reified VB : ViewBinding> inflateBindingWithGenericTest(layoutInflater: LayoutInflater): VB {
    val viewbinding = VB::class.java
//    val viewbinding = clazz
    return (viewbinding.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB).also {
        if (it is ViewDataBinding){
//            it.lifecycleOwner = this
        }
    }

}




inline fun <reified VB : ViewBinding> inflateBindingWithGenericTest1(clazz:Class<VB> ,layoutInflater: LayoutInflater): VB {
//    val viewbinding = VB::class.java
    val viewbinding = clazz
    return (viewbinding.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB).also {
        if (it is ViewDataBinding){
//            it.lifecycleOwner = this
        }
    }
}


inline fun <reified VB : ViewBinding> AppCompatActivity.inflateBindingWithGeneric_test2(layoutInflater: LayoutInflater): VB {

//    VB.

    return withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
    }
}







@JvmName("inflateWithGeneric")
fun < VB : ViewBinding> Fragment.inflateBindingWithGeneric(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

inline  /*private */ fun <reified VB : ViewBinding> withGenericBindingClassTest(any: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
                try {
                    return block.invoke(genericSuperclass.actualTypeArguments[1] as Class<VB>)
                } catch (e: NoSuchMethodException) {
                } catch (e: ClassCastException) {
                } catch (e: InvocationTargetException) {
                    throw e.targetException
                }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}



  /*private */ fun < VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType && genericSuperclass.actualTypeArguments.size > 1) {
            try {
                return block.invoke(genericSuperclass.actualTypeArguments[1] as Class<VB>)
            } catch (e: NoSuchMethodException) {
            } catch (e: ClassCastException) {
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}