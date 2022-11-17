package com.mvvm.provider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.mvvm.provider.callback.OnLoginOutCallBack;
import com.mvvm.provider.data.UserInfo;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-17.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public interface IUserInfoProvider extends IProvider {
    UserInfo getUserInfo();
    boolean isLoginState();
    void loginOut(OnLoginOutCallBack callBack);
}
