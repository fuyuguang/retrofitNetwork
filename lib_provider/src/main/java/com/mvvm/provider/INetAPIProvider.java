package com.mvvm.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-17.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */
public interface INetAPIProvider extends IProvider {
    public <T> T createApiService(final Class<T> service);

//    boolean isLoginState();
//    void loginOut(OnLoginOutCallBack callBack);
}


//    val apiService: ApiService by lazy {
//        NetworkApiImp.mRetrofit.create(ApiService::class.java)
//        }
//
//        val apiProductService: ApiProductService by lazy {
//        NetworkApiImp.mRetrofit.create(ApiProductService::class.java)
//        }
//
//
//        val apiUserService: ApiUserService by lazy {
//        NetworkApiImp.mRetrofit.create(ApiUserService::class.java)
//        }
//
//        val apiHomeService: ApiHomeService  by lazy {
//        NetworkApiImp.mRetrofit.create(ApiHomeService::class.java)
//        }
//
//        val apiUploadService: ApiUploadService  by lazy {
//        NetworkApiImp.mRetrofit.create(ApiUploadService::class.java)
//        }