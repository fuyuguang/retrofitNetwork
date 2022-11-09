package com.fyg.networklib.model.bean

import android.text.TextUtils
import com.alibaba.fastjson.annotation.JSONField
import com.fyg.networklib.BaseResponse
import com.fyg.networklib.Ktx

/**
 * Created by fuyuguang on 2022/4/2 6:01 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    这个类支持fastjson 和gson
 */
  class ApiResponse<T>(
   )  : BaseResponse<T>() {


//    @JSONField(name = "errCode")  var errorCode: String = "",
//    @JSONField(name = "errMsg") var errorMsg: String = "",
//    @JSONField(name = "success") var success	: Int = -1,
//    @JSONField(name = "result") var result: T? = null,
//    @JSONField(name = "toast") var toast:String = "",
//    @JSONField(name = "etag") var etag:String= ""



    @JSONField(name = "errCode")
    var errorCode: String = ""

    @JSONField(name = "errMsg")
    var errorMsg: String = ""

    @JSONField(name = "success")
    var success	: Int = -1

    @JvmField
    @JSONField(name = "result")
    var result: T? = null

    @JSONField(name = "toast")
    var toast:String = ""

    @JSONField(name = "etag")
    var etag:String= ""

    /**
     * 调用接口， 出问题了，可能是参数不合法， success ！= 1，要取出errCode
     */
    override fun isSucces() = success == 1

    override fun getResponseCode() = errorCode

    override fun getResult() = result

    override fun getResponseMsg() = errorMsg

    override fun getResponseToast(): String  = toast

    companion object {
        /**
         * 通信成功
         *
         * @param data
         * @return
         */
        open fun isCommunicationOk(data: ApiResponse<*>?): Boolean {
            return data != null && data.isSucces()
        }

        /**
         * 通信成功 有 业务数据
         *
         * @param data
         * @return
         */
        open fun  isBusinessOk(data: ApiResponse<*>?): Boolean {
            return isCommunicationOk(data) && data!!.getResult() != null
        }


        /**
         * 通信成功 有 业务数据
         *
         * @param data
         * @return
         */
        fun getErrorMessage(data: ApiResponse<*>): String? {
            return getErrorMessage(data, 0)
        }


        /**
         * 通信成功 有 业务数据
         *
         * @param data
         * @return
         */
        fun getErrorMessage(data: ApiResponse<*>, defaultErrorMessage: Int): String? {
            return if (data != null && !TextUtils.isEmpty(data.errorMsg)) data.errorMsg else if (defaultErrorMessage > 0) Ktx.app.getString(
                defaultErrorMessage
            ) else "网络异常,请重试"
        }


        /**
         * Token 是否过期
         *
         * @param data
         */
        fun isTokenDated(data: BaseResponse<*>?): Boolean {
            return "1200018" == data?.getResponseCode()
        }
    }

    override fun getETag(): String {
        return etag
    }


}