package com.mvvm.provider.router;


import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * * desc : ${desc}
 * Created by fyg on 2019-06-17.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 */

public interface RouterPath {


    /**
     * 测试模块 示例
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Test.LOGIN,
            Test.REGISTER
    })
    @interface Test {
        String LOGIN = "/login/login";
        String REGISTER = "/register/register";
    }


    /**
     * Provider 接口调用模块
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Provider.MODULE_NAME,
            Provider.USER_INFO,
            Provider.USER,
            Provider.INIT_BASE_APP,
            Provider.INIT_NET_API,
            Provider.INIT_LIB_DATABASE,
    })
    @interface Provider {
        String MODULE_NAME = "/provider/moduleName";
        String USER_INFO = "/provider2/userInfo";
        String USER = "/provider2/user";
        String INIT_BASE_APP = LIB_INIT.LIB_INIT_BASEAPP;
        String INIT_NET_API = NET_API.INIT_NET_API;
        String INIT_LIB_DATABASE = LIB_INIT.LIB_INIT_DATABASE;
    }


    /**
     * 配送模块
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            LIB_INIT.LIB_INIT_BASEAPP,
            LIB_INIT.LIB_INIT_DATABASE
    })
    @interface LIB_INIT {

        String LIB_INIT_BASEAPP = "/lib_init/init_app";

        String LIB_INIT_DATABASE= "/database/database";


    }

    /**
     * 单据号扫描
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SCANCODE.SCANCODE_PATH
    })
    @interface SCANCODE {
        /**
         * 单据号扫描
         */
        String SCANCODE_PATH = "/merchant/CaptureActivity";
    }

    /**
     * 新零售进货系统路由
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            NET_API.INIT_NET_API,

    })
    @interface NET_API {
        /**
         * 登陆页
         */
        String INIT_NET_API = "/NET_API/NETAPI";



    }

//    /**
//     * 酒快到进货系统路由
//     */
//    @interface  JKDPURCHASE{
//        /**
//         * 登陆页
//         */
//        String JKDPURCHASE_LOGIN = "/jkdpurchase/LoginActivity";
//        /**
//         * 首页
//         */
//        String JKDPURCHASE_MAIN = "/jkdpurchase/MainActivity";
//    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            ACTION.MERCHANT_TO_DELIVERY,
            ACTION.JKD_TO_DELIVERY
    })
    @interface ACTION {
        /**
         * action目标
         */
        int MERCHANT_TO_DELIVERY = 1;
        int JKD_TO_DELIVERY = 2;
    }

    /**
     * 店内管理路由
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            STORE_MANAGER.SKPI_RECEIVE,
            STORE_MANAGER.SKIP_GROUP,
            STORE_MANAGER.SKIP_ALLOTINQUIRE,
            STORE_MANAGER.SKIP_ALLOTSEARCH,
            STORE_MANAGER.SKIP_CHECK,
            STORE_MANAGER.SKIP_MOVE
    })
    @interface STORE_MANAGER {
        /**
         * 收货
         */
        String SKPI_RECEIVE = "/jkdmanager/ReceiveActivity";
        /**
         * 上架
         */
        String SKIP_GROUP = "/jkdmanager/GroupActivity";
        /**
         * 盘点
         */
        /**
         * 调拨单查询
         */
        String SKIP_ALLOTINQUIRE = "/jkdmanager/AllocationInquireActivity";
        /**
         * 调拨单搜索
         */
        String SKIP_ALLOTSEARCH = "/jkdmanager/AllotSearchActivity";
        /**
         * 调拨
         */
        String SKIP_ALLOT = "/jkdmanager/AllotActivity";
        /**
         * 调拨单搜索商品详情
         */
        String SKIP_ALLOTPRODUCTDETAIL = "/jkdmanager/AllotProductDetailActivity";

        /**
         * 盘点
         */
        String SKIP_CHECK = "/jkdmanager/CheckingActivity";
        /**
         * 货位移动
         */
        String SKIP_MOVE = "/jkdmanager/MovingActivity";
    }
}
