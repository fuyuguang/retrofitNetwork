package com.fyg.networklib

abstract class BaseResponse<T> {

    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSucces(): Boolean

    abstract fun getResult(): T?

    abstract fun getResponseCode(): String

    abstract fun getResponseMsg(): String

    abstract fun getResponseToast(): String
    abstract fun getETag(): String




}