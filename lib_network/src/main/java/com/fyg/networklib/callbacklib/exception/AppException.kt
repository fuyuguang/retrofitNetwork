package com.fyg.networklib.callbacklib.exception
/**
 * Created by fuyuguang on 2022/4/21 6:23 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */
 class AppException : Exception {

    var errorMsg: String //错误消息
    var errCode: String?  //错误码
    var errorLog: String? //错误日志
    var throwable: Throwable? = null

    constructor(errCode: String?, error: String?, errorLog: String? = "", throwable: Throwable? = null) : super(error) {
        this.errorMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode
        this.errorLog = errorLog ?: this.errorMsg
        this.throwable = throwable
    }

    constructor(error: HttpError, e: Throwable?) {
        errCode = ""
        errorMsg = error.msg
        errorLog = e?.message
        throwable = e
    }
}