package com.fyg.networklib;

/**
 * Created by fuyuguang on 2022/6/1 11:56 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public interface IBusinessHandler extends ITokenExpired{

    boolean onFailure(Throwable throwable);

    boolean onError(BaseResponse<?> data);

    void onNullResult(BaseResponse<?> data);

    interface Factory {
        IBusinessHandler getBusinessHandler();
    }
}
