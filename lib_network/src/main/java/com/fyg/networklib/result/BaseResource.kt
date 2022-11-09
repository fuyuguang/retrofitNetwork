package com.fyg.networklib.result

/**
 * Created by fuyuguang on 2022/10/10 10:20 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
abstract  class BaseResource(
    val status: Status,
    val message: String?,
    val throwable: Throwable?
) {


}

