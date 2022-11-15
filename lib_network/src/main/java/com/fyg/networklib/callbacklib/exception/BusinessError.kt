package com.fyg.networklib.callbacklib.exception

/**
 * Created by fuyuguang on 2022/11/15 10:27 AM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * 请求接口时，业务数据出错
 */
class BusinessError : HttpError {
    constructor(msg: String?) : super(msg) {}
    constructor(msg: String?, body: Any?) : super(msg, body) {}

    override fun toString(): String {
        return msg
    }

    companion object BusinessErrorFactory{

        @JvmStatic
        fun createBusinessError(msg: String) : BusinessError{
            return BusinessError(msg)
        }

    }
}