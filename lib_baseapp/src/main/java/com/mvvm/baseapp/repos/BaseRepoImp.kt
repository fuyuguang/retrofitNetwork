package com.mvvm.baseapp.repos

/**
 * Created by fuyuguang on 2022/10/26 11:57 AM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
open class BaseRepoImp() : BaseRepository() {

    //跨模块调用，获取所有的dao 和 netAPI,由于该类是在baseapp中，如果要拿到 ApiService
    //接口的引用， 那么该接口也得定义到 baseapp中 ，ApiService 接口方法的返回值都是一些业务数据，
    //ApiService 又依赖 这些业务数据，难道要把这些业务数据页 定义到 baseapp 中吗？ 这样就达不到复用module了

//    val hs : INetAPIProvider? = null;

}


