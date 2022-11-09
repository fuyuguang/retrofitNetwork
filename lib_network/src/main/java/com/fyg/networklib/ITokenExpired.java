package com.fyg.networklib;

/**
 * Created by fuyuguang on 2022/9/13 3:52 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public interface ITokenExpired {

    boolean isExpired(BaseResponse<?> data);

    void tokenExpiredHandler(BaseResponse<?> data);
}
