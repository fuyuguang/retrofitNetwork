package com.mvvm.provider.callback;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-18.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public interface OnLoginOutCallBack {

    int LOGINOUT_SUCCESS = 1;
    int LOGINOUT_FAILURE = -1;
    void onLoginOut(int stateCode);

}
